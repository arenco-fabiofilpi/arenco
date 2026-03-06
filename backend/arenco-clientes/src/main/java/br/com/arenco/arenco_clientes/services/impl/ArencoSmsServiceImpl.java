package br.com.arenco.arenco_clientes.services.impl;

import br.com.arenco.arenco_clientes.config.properties.MessengerProperties;
import br.com.arenco.arenco_clientes.entities.SMSModel;
import br.com.arenco.arenco_clientes.repositories.SMSModelRepository;
import br.com.arenco.arenco_clientes.exceptions.SendMessageException;
import br.com.arenco.arenco_clientes.services.ArencoSmsService;
import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArencoSmsServiceImpl implements ArencoSmsService {

  private static final int MAX_RETRIES = 3;
  private static final int MAX_DATA_TO_SAVE = 4096;
  private static final Set<Integer> RETRYABLE = Set.of(429, 500, 502, 503, 504);
  private static final Pattern E164 = Pattern.compile("^\\+[1-9]\\d{6,14}$"); // simples e efetivo

  private final MessengerProperties messengerProperties;
  private final SMSModelRepository repository;

  private volatile TwilioRestClient client; // inicializado via registerTwilioAccount()

  @Override
  public void registerTwilioAccount() {
    log.info("Registering Twilio account (sid={})", messengerProperties.getTwilioAccountSid());
    Twilio.init(messengerProperties.getTwilioAccountSid(), messengerProperties.getTwilioAuthToken());
    this.client = Twilio.getRestClient();
  }

  @Override
  public void sendSms(final String to, final String message, final String from) {
    ensureClient();

    validateInput(to, message, from);

    log.debug("Sending SMS to {}", to);

    final Message response =
        executeWithRetry(
            () ->
                Message.creator(new PhoneNumber(to), new PhoneNumber(from), message)
                    .create(client));

    log.info("SMS sent sid={} to={} status={}", response.getSid(), to, response.getStatus());

    save(from, message, to, response);
  }

  @Override
  public Page<SMSModel> findAll(final Pageable pageable) {
    return repository.findAll(pageable);
  }

  /* ======================= Internals ======================= */

  private void ensureClient() {
    if (client == null) {
      synchronized (this) {
        if (client == null) {
          registerTwilioAccount();
        }
      }
    }
  }

  private static void validateInput(final String to, final String message, final String from) {
    if (!StringUtils.hasText(message)) {
      throw new SendMessageException("Message content is required");
    }
    if (!StringUtils.hasText(to) || !E164.matcher(to).matches()) {
      throw new SendMessageException("Destination phone must be E.164 (e.g. +5511999999999)");
    }
    if (!StringUtils.hasText(from) || !E164.matcher(from).matches()) {
      throw new SendMessageException("Sender phone must be E.164 (e.g. +15551234567)");
    }
  }

  /** Executa a operação do Twilio com retry exponencial + jitter para 429/5xx. */
  private <T> T executeWithRetry(final ThrowingSupplier<T> supplier) {
    ApiException lastApi = null;
    RuntimeException lastRt = null;

    for (int attempt = 0; attempt <= MAX_RETRIES; attempt++) {
      try {
        return supplier.get();
      } catch (final ApiException ae) {
        lastApi = ae;
        final int status = ae.getStatusCode();
        if (!RETRYABLE.contains(status)) {
          throw wrap("Twilio error (status=%s code=%s): %s", status, ae.getCode(), ae.getMessage());
        }
        final long delayMs = computeDelayMs(ae, attempt);
        log.warn(
            "Retryable Twilio error (status={} code={} attempt={} delayMs={}): {}",
            status,
            ae.getCode(),
            attempt + 1,
            delayMs,
            ae.getMessage());
        sleep(delayMs);
      } catch (final RuntimeException re) {
        lastRt = re;
        final long delayMs = computeDelayMs(null, attempt);
        log.warn(
            "Runtime error talking to Twilio (attempt={} delayMs={}): {}",
            attempt + 1,
            delayMs,
            re.getMessage());
        sleep(delayMs);
      }
    }

    if (lastApi != null) {
      throw wrap(
          "Give up after %d attempts. Last Twilio error (status=%s code=%s): %s",
          MAX_RETRIES + 1, lastApi.getStatusCode(), lastApi.getCode(), lastApi.getMessage());
    }
    throw new SendMessageException("Give up after retries due to runtime error", lastRt);
  }

  /** Respeita Retry-After quando presente (em segundos), senão usa backoff exponencial + jitter. */
  private long computeDelayMs(final ApiException ae, final int attempt) {
    if (ae != null) {
      final String retryAfter =
          ae.getMoreInfo() != null && ae.getMoreInfo().contains("Retry-After")
              ? extractRetryAfter(ae.getMoreInfo())
              : null;
      if (retryAfter != null) {
        try {
          return Duration.ofSeconds(Math.max(1, Long.parseLong(retryAfter))).toMillis();
        } catch (NumberFormatException ignored) {
          /* cai no exponencial */
        }
      }
    }
    long base = (long) (250 * Math.pow(2, Math.max(0, attempt))); // 250ms, 500ms, 1s, 2s...
    base = Math.min(base, 4_000);
    long jitter = ThreadLocalRandom.current().nextLong(0, 250);
    return base + jitter;
  }

  private static String extractRetryAfter(final String moreInfo) {
    // Simples: procura "Retry-After: N"
    final var m = Pattern.compile("Retry-After:\\s*(\\d+)").matcher(moreInfo);
    return m.find() ? m.group(1) : null;
  }

  private void save(
      final String from, final String data, final String deliveredTo, final Message twilioMsg) {
    final SMSModel model = new SMSModel();

    model.setFrom(from);
    model.setDeliveredTo(deliveredTo);
    model.setData(truncate(data));

    // Enriquecimento com dados da resposta Twilio
    try {
      model.setProvider("twilio");
      model.setProviderSid(twilioMsg.getSid());
      model.setStatus(twilioMsg.getStatus() != null ? twilioMsg.getStatus().toString() : null);
      model.setErrorCode(twilioMsg.getErrorCode());
      model.setErrorMessage(twilioMsg.getErrorMessage());
      model.setSegments(twilioMsg.getNumSegments());
      model.setPrice(twilioMsg.getPrice());
      model.setPriceUnit(twilioMsg.getPriceUnit());
      model.setDateSent(twilioMsg.getDateSent());
    } catch (final RuntimeException e) {
      // não falha a gravação por campos opcionais
      log.warn("Could not enrich SMSModel with Twilio response: {}", e.getMessage());
    }

    try {
      repository.save(model);
    } catch (RuntimeException e) {
      log.error("Failed to persist SMSModel: {}", e.getMessage(), e);
    }
  }

  private static String truncate(final String s) {
    if (s == null) return null;
    if (s.length() <= MAX_DATA_TO_SAVE) return s;
    return s.substring(0, MAX_DATA_TO_SAVE) + "...[truncated]";
  }

  private SendMessageException wrap(final String fmt, final Object... args) {
    final String msg = String.format(Locale.ROOT, fmt, args);
    log.error(msg);
    return new SendMessageException(msg);
  }

  @FunctionalInterface
  private interface ThrowingSupplier<T> {
    T get() throws RuntimeException;
  }

  private void sleep(final long ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException ie) {
      Thread.currentThread().interrupt();
    }
  }
}
