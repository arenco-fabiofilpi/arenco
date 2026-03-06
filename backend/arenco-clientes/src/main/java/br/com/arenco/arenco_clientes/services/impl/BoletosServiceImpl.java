package br.com.arenco.arenco_clientes.services.impl;

import br.com.arenco.arenco_clientes.entities.*;
import br.com.arenco.arenco_clientes.enums.FileType;
import br.com.arenco.arenco_clientes.enums.TipoProcessamentoBoleto;
import br.com.arenco.arenco_clientes.exceptions.ArencoLeituraDeBoletoException;
import br.com.arenco.arenco_clientes.exceptions.BoletoNaoEncontrado;
import br.com.arenco.arenco_clientes.exceptions.FalhaNaRespostaDoBoleto;
import br.com.arenco.arenco_clientes.repositories.*;
import br.com.arenco.arenco_clientes.services.BoletosService;
import br.com.arenco.arenco_clientes.services.S3Service;
import br.com.arenco.arenco_clientes.strategies.BoletoResponseStrategy;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoletosServiceImpl implements BoletosService {
  // ====== Configurações ======
  private static final int S3_MAX_RETRY = 3;
  private static final long S3_BACKOFF_MS = 400L;

  // ====== Dependências ======
  private final S3Service s3Service;
  private final BoletoFileModelRepository boletoFileModelRepository;
  private final List<BoletoResponseStrategy> estrategiasDeRespostaDeBoleto;
  private final BoletoAProcessarModelRepository boletoAProcessarModelRepository;

  /** Cache lazy de estratégias por tipo de arquivo (double-checked locking correto). */
  private volatile Map<FileType, BoletoResponseStrategy> responseStrategyByType;

  // ================== API ==================

  @Override
  public void gerarBoletoAProcessar(
      final TipoProcessamentoBoleto tipoProcessamentoBoleto, final String receivableTitleId) {
    final var boletoAProcessar = new BoletoAProcessarModel();
    boletoAProcessar.setTipoProcessamentoBoleto(tipoProcessamentoBoleto);
    boletoAProcessar.setReceivableTitleId(receivableTitleId);
    boletoAProcessarModelRepository.save(boletoAProcessar);
    log.info(
        "Salvo Boleto a Processar ID {}, Tipo {}",
        boletoAProcessar.getId(),
        tipoProcessamentoBoleto);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<@NonNull BoletoFileModel> findAll(final Pageable pageable) {
    return boletoFileModelRepository.findAll(pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public void colocarBoletoNaResposta(final String id, final HttpServletResponse response) {
    final var boletoFileModel =
        boletoFileModelRepository.findById(id).orElseThrow(() -> new BoletoNaoEncontrado(id));

    final var fileType =
        Objects.requireNonNull(
            boletoFileModel.getFileType(), "fileType do BoletoFileModel não pode ser null");

    final var strategy = getBoletoResponseStrategy(fileType);

    // Pequeno retry em leitura S3 (rede)
    final byte[] bytes =
        s3WithRetry(
            () -> {
              try (final var download = s3Service.download(boletoFileModel.getId())) {
                return download.readAllBytes();
              } catch (IOException e) {
                throw new ArencoLeituraDeBoletoException(e.getMessage(), e);
              }
            });

    try {
      strategy.colocarBoletoNaResposta(response, bytes);
    } catch (final IOException e) {
      throw new FalhaNaRespostaDoBoleto(e.getMessage(), e);
    }
  }

  // ================== Internals ==================

  private BoletoResponseStrategy getBoletoResponseStrategy(final FileType fileType) {
    Map<FileType, BoletoResponseStrategy> map = responseStrategyByType;
    if (map == null) {
      synchronized (this) {
        map = responseStrategyByType;
        if (map == null) {
          map =
              estrategiasDeRespostaDeBoleto.stream()
                  .collect(
                      Collectors.toMap(
                          BoletoResponseStrategy::supportedType,
                          s -> s,
                          (a, b) -> a,
                          () -> new EnumMap<>(FileType.class)));
          responseStrategyByType = map; // importante: atribuir ao campo e também manter no local
        }
      }
    }
    final var strategy = map.get(fileType);
    if (strategy == null) {
      throw new IllegalArgumentException("Strategy para fileType não encontrada: " + fileType);
    }
    return strategy;
  }

  private <T> T s3WithRetry(final Supplier<T> action) {
    RuntimeException last = null;
    for (int i = 1; i <= S3_MAX_RETRY; i++) {
      try {
        return action.get();
      } catch (RuntimeException e) {
        last = e;
        if (i < S3_MAX_RETRY) {
          sleep(S3_BACKOFF_MS * i);
        }
      }
    }
    throw last;
  }

  private static void sleep(final long ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException ie) {
      Thread.currentThread().interrupt();
    }
  }
}
