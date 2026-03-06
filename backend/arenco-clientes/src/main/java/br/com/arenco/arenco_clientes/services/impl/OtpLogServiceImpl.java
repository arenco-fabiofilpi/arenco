package br.com.arenco.arenco_clientes.services.impl;

import br.com.arenco.arenco_clientes.exceptions.MultipleOtpRequests;
import br.com.arenco.arenco_clientes.services.OtpLogService;
import br.com.arenco.arenco_clientes.context.RequestContext;
import br.com.arenco.arenco_clientes.utils.ArencoDateUtils;
import br.com.arenco.arenco_clientes.entities.OtpLogModel;
import br.com.arenco.arenco_clientes.enums.OtpType;
import br.com.arenco.arenco_clientes.repositories.OtpLogModelRepository;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpLogServiceImpl implements OtpLogService {
  private final OtpLogModelRepository repository;

  @Override
  public void register(
      final String userModelId,
      final String deliveredTo,
      final Instant expiration,
      final OtpType type) {
    final var requestContext = RequestContext.get();
    final var model = new OtpLogModel();
    model.setUserModelId(userModelId);
    model.setDeliveredTo(deliveredTo);
    model.setIpAddress(requestContext.ip());
    model.setUserAgent(requestContext.userAgent());
    model.setExpiresAt(expiration);
    model.setType(type);
    repository.save(model);
  }

  @Override
  public void check(final String userModelId, final String deliveredTo, final OtpType type) {
    final var optional =
        repository.findFirstByUserModelIdAndDeliveredToAndTypeOrderByExpiresAtAsc(
            userModelId, deliveredTo, type);
    if (optional.isPresent()) {
      final var model = optional.get();
      final var expirationInstant = model.getExpiresAt();
      final var seconds =
          ArencoDateUtils.calcularDiferencaEmSegundos(Instant.now(), expirationInstant, 30);
      throw new MultipleOtpRequests(seconds);
    }
  }
}
