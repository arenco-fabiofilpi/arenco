package br.com.arenco.arenco_clientes.services.impl;

import br.com.arenco.arenco_clientes.config.properties.AuthenticationProperties;
import br.com.arenco.arenco_clientes.context.ArencoAuthenticatedPreSessionContext;
import br.com.arenco.arenco_clientes.context.RequestContext;
import br.com.arenco.arenco_clientes.exceptions.InvalidSessionUserException;
import br.com.arenco.arenco_clientes.services.ArencoPreSessionService;
import br.com.arenco.arenco_clientes.utils.ArencoSessionUtils;
import br.com.arenco.arenco_clientes.dtos.authentication.PreSessionRecord;
import br.com.arenco.arenco_clientes.entities.AuthenticatedPreSessionModel;
import br.com.arenco.arenco_clientes.entities.PreSessionOtpModel;
import br.com.arenco.arenco_clientes.entities.UserModel;
import br.com.arenco.arenco_clientes.repositories.AuthenticatedPreSessionModelRepository;
import br.com.arenco.arenco_clientes.repositories.UserModelRepository;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArencoPreSessionServiceImpl implements ArencoPreSessionService {
  private final AuthenticationProperties authenticationProperties;
  private final UserModelRepository userModelRepository;
  private final AuthenticatedPreSessionModelRepository authenticatedPreSessionModelRepository;

  @Override
  public PreSessionRecord createPreSession(final String userId) {
    final var requestContext = RequestContext.get();
    final var userAgent = requestContext.userAgent();
    final var ip = requestContext.ip();
    final String rawUuid = UUID.randomUUID().toString();
    final String hashed = ArencoSessionUtils.hashSessionId(rawUuid, authenticationProperties.getPreSessionPepper());

    final AuthenticatedPreSessionModel authenticatedPreSessionModel =
        new AuthenticatedPreSessionModel(
            hashed, ip, userAgent, userId, Instant.now(), Instant.now().plus(Duration.ofHours(2)));

    authenticatedPreSessionModelRepository.save(authenticatedPreSessionModel);
    return new PreSessionRecord(rawUuid, authenticatedPreSessionModel);
  }

  @Override
  public void removePreSession(final String id) {
    final String hashed = ArencoSessionUtils.hashSessionId(id, authenticationProperties.getPreSessionPepper());
    final var sessionOptional = authenticatedPreSessionModelRepository.findById(hashed);
    if (sessionOptional.isPresent()) {
      log.info("Removing pre-session with id {} from session repository", hashed);
      authenticatedPreSessionModelRepository.delete(sessionOptional.get());
    }
  }

  @Override
  public Optional<AuthenticatedPreSessionModel> findPreSessionModel(final String preSessionId) {
    final String hashed = ArencoSessionUtils.hashSessionId(preSessionId, authenticationProperties.getPreSessionPepper());
    return authenticatedPreSessionModelRepository.findById(hashed);
  }

  @Override
  public UserModel getUserBySessionModel(
      final AuthenticatedPreSessionModel authenticatedPreSessionModel) {
    final var userId = authenticatedPreSessionModel.getUserId();
    return userModelRepository
        .findById(userId)
        .orElseThrow(
            () ->
                new IllegalStateException(
                    String.format(
                        "Nao encontrado usuario %s para pre-sessao %s",
                        userId, authenticatedPreSessionModel.getHashedId())));
  }

  @Override
  public ArencoAuthenticatedPreSessionContext getCurrentPreSessionContext() {
    final var preSessionSecurityGenericContext =
        SecurityContextHolder.getContext().getAuthentication();
    if (!(preSessionSecurityGenericContext
        instanceof ArencoAuthenticatedPreSessionContext arencoAuthenticatedPreSessionContext)) {
      throw new IllegalStateException(
          "Current SecurityContext is not an ArencoAuthenticatedPreSessionContext");
    }
    return arencoAuthenticatedPreSessionContext;
  }

  @Override
  public UserModel getUserModelFromPreSessionContext(
      final ArencoAuthenticatedPreSessionContext preSessionContext) {
    final var principal = preSessionContext.getPrincipal();
    if (!(principal instanceof UserModel userModel)) {
      throw new InvalidSessionUserException("Principal is not a UserModel");
    }
    return userModel;
  }

  @Override
  public UserModel getUserModelFromPreSessionContext() {
    final var context = getCurrentPreSessionContext();
    return getUserModelFromPreSessionContext(context);
  }

  @Override
  public AuthenticatedPreSessionModel getPreSessionModelFromPreSessionContext(
      final ArencoAuthenticatedPreSessionContext preSessionContext) {
    final var model = preSessionContext.getAuthenticatedPreSessionModel();
    if (model == null) {
      throw new IllegalStateException("AuthenticatedPreSessionModel is null");
    }
    return model;
  }

  @Override
  public AtomicReference<PreSessionOtpModel> getOtpFromPreSessionContext(
      final ArencoAuthenticatedPreSessionContext preSessionContext) {
    final var atomicReference = preSessionContext.getOneTimePasswordModel();
    if (atomicReference == null) {
      throw new IllegalStateException("AuthenticatedPreSessionModel is null");
    }
    return atomicReference;
  }
}
