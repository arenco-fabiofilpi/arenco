package br.com.arenco.arenco_clientes.services.impl;

import br.com.arenco.arenco_clientes.entities.EmailModel;
import br.com.arenco.arenco_clientes.repositories.EmailModelRepository;
import br.com.arenco.arenco_clientes.exceptions.SendMessageException;
import br.com.arenco.arenco_clientes.services.ArencoEmailService;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArencoEmailServiceImpl implements ArencoEmailService {

  private static final String SEND_ENDPOINT = "mail/send";
  private static final int MAX_BODY_TO_SAVE = 8_192; // evita inflar a base
  private static final int MAX_RETRIES = 3;
  private static final Set<Integer> RETRYABLE_STATUS = Set.of(429, 500, 502, 503, 504);

  private final SendGrid sendGrid;
  private final EmailModelRepository repository;

  @Override
  public void send(
          final Email to,
          final Email from,
          final String subject,
          final String templateId,
          final List<Personalization> personalizations
  ) {
    validateInput(to, from, subject, templateId, personalizations);

    final Mail mail = buildMail(from, subject, templateId, personalizations);
    final Request request = buildRequest(mail);

    final Response response = executeWithRetry(request);

    log.debug("SendGrid status={} headers={} body={}",
            response.getStatusCode(), response.getHeaders(), safeBody(response.getBody()));

    save(mail, request, to.getEmail(), response);
  }

  @Override
  public Page<EmailModel> findAll(final Pageable pageable) {
    return repository.findAll(pageable);
  }

  /* ======================= Internals ======================= */

  private static void validateInput(
          final Email to,
          final Email from,
          final String subject,
          final String templateId,
          final List<Personalization> personalizations
  ) {
    if (from == null || !StringUtils.hasText(from.getEmail())) {
      throw new SendMessageException("From email is required");
    }
    if (to == null || !StringUtils.hasText(to.getEmail())) {
      throw new SendMessageException("Destination (to) email is required");
    }
    if (!StringUtils.hasText(subject)) {
      throw new SendMessageException("Subject is required");
    }
    if (!StringUtils.hasText(templateId)) {
      throw new SendMessageException("TemplateId is required");
    }
    if (personalizations == null || personalizations.isEmpty()) {
      throw new SendMessageException("At least one personalization is required");
    }
  }

  private static Mail buildMail(
          final Email from,
          final String subject,
          final String templateId,
          final List<Personalization> personalizations
  ) {
    final Mail mail = new Mail();
    mail.setFrom(from);
    mail.setSubject(subject);
    mail.setTemplateId(templateId);
    for (final Personalization p : personalizations) {
      mail.addPersonalization(p);
    }
    return mail;
  }

  private static Request buildRequest(final Mail mail) {
    final Request request = new Request();
    request.setMethod(Method.POST);
    request.setEndpoint(SEND_ENDPOINT);
    try {
      request.setBody(mail.build());
    } catch (final IOException e) {
      throw new SendMessageException("Error creating SendGrid request body", e);
    }
    return request;
  }

  /** Execução com retry exponencial + jitter para 429/5xx. */
  private Response executeWithRetry(final Request request) {
    int attempt = 0;
    IOException lastIo = null;
    Response lastResponse = null;

    while (attempt <= MAX_RETRIES) {
      try {
        final Response response = sendGrid.api(request);
        lastResponse = response;

        if (isSuccess(response)) return response;

        if (!isRetryable(response)) {
          throw failure("Error sending email. Status Code %s. Body: %s",
                  response.getStatusCode(), safeBody(response.getBody()));
        }

        // retryable: respeita Retry-After quando existir
        final long delayMs = computeDelayMs(response, attempt);
        log.warn("Retryable SendGrid failure (status={}), attempt={} delayMs={}",
                response.getStatusCode(), attempt + 1, delayMs);
        sleep(delayMs);
      } catch (final IOException io) {
        lastIo = io;
        final long delayMs = computeDelayMs(null, attempt);
        log.warn("IO error talking to SendGrid (attempt={}): {}. Retrying in {}ms",
                attempt + 1, io.getMessage(), delayMs);
        sleep(delayMs);
      }
      attempt++;
    }

    if (lastResponse != null) {
      throw failure("Give up after %d attempts. Last status=%s body=%s",
              MAX_RETRIES + 1, lastResponse.getStatusCode(), safeBody(lastResponse.getBody()));
    }
    throw new SendMessageException("Give up after retries due to IO error", lastIo);
  }

  private boolean isSuccess(final Response r) {
    final int s = r.getStatusCode();
    return s >= 200 && s < 300;
  }

  private boolean isRetryable(final Response r) {
    return RETRYABLE_STATUS.contains(r.getStatusCode());
  }

  /** Backoff exponencial com jitter. Respeita Retry-After (segundos) quando presente. */
  private long computeDelayMs(final Response response, final int attempt) {
    // base: 250ms, exponencial até ~4s + jitter 0..250ms
    long base = (long) (250 * Math.pow(2, Math.max(0, attempt)));
    base = Math.min(base, 4_000);

    long jitter = ThreadLocalRandom.current().nextLong(0, 250);

    if (response != null) {
      final Map<String, String> headers = response.getHeaders();
      if (headers != null) {
        final String retryAfter = headers.getOrDefault("Retry-After", headers.get("retry-after"));
        if (retryAfter != null) {
          try {
            long retrySec = Long.parseLong(retryAfter.trim());
            return Duration.ofSeconds(Math.max(1, retrySec)).toMillis();
          } catch (NumberFormatException ignored) {
            // se não for número (às vezes vem data), usamos o exponencial mesmo
          }
        }
      }
    }
    return base + jitter;
  }

  private void sleep(final long ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException ie) {
      Thread.currentThread().interrupt();
    }
  }

  private SendMessageException failure(final String fmt, final Object... args) {
    final String msg = String.format(Locale.ROOT, fmt, args);
    log.error(msg);
    return new SendMessageException(msg);
  }

  private void save(final Mail data, final Request request, final String deliveredTo, final Response response) {
    final EmailModel model = new EmailModel();

    model.setSubject(data.getSubject());
    model.setFrom(data.getFrom() != null ? data.getFrom().getEmail() : null);
    model.setDeliveredTo(deliveredTo);
    model.setTemplateId(data.getTemplateId());

    // Guarda request/response com truncamento
    model.setData(truncate(request.getBody()));
    model.setStatusCode(response != null ? response.getStatusCode() : null);
    model.setResponseBody(response != null ? truncate(response.getBody()) : null);

    // Extrai algum header útil (ex.: X-Message-Id)
    if (response != null && response.getHeaders() != null) {
      final String messageId = Optional.ofNullable(response.getHeaders().get("X-Message-Id"))
              .orElse(response.getHeaders().get("x-message-id"));
      model.setMessageId(messageId);
      model.setResponseHeaders(truncate(response.getHeaders().toString()));
    }

    try {
      repository.save(model);
    } catch (RuntimeException e) {
      // não derruba o fluxo de envio por falha de persistência
      log.error("Failed to persist EmailModel: {}", e.getMessage(), e);
    }
  }

  private static String truncate(final String s) {
    if (s == null) return null;
    if (s.length() <= MAX_BODY_TO_SAVE) return s;
    return s.substring(0, MAX_BODY_TO_SAVE) + "...[truncated]";
  }

  private static String safeBody(final String s) {
    if (s == null) return null;
    // evita despejar corpo gigante em logs
    return s.length() <= 2_048 ? s : (s.substring(0, 2_048) + "...[truncated]");
  }
}
