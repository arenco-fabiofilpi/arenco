package br.com.arenco.arenco_clientes.services.impl;

import br.com.arenco.arenco_clientes.config.properties.AuthenticationProperties;
import br.com.arenco.arenco_clientes.context.ArencoAuthenticatedSessionContext;
import br.com.arenco.arenco_clientes.context.RequestContext;
import br.com.arenco.arenco_clientes.dtos.authentication.SessionRecord;
import br.com.arenco.arenco_clientes.entities.SessionModel;
import br.com.arenco.arenco_clientes.entities.UserModel;
import br.com.arenco.arenco_clientes.exceptions.InvalidSessionException;
import br.com.arenco.arenco_clientes.exceptions.InvalidSessionUserException;
import br.com.arenco.arenco_clientes.repositories.SessionModelRepository;
import br.com.arenco.arenco_clientes.repositories.UserModelRepository;
import br.com.arenco.arenco_clientes.services.ArencoSessionService;
import br.com.arenco.arenco_clientes.utils.ArencoSessionUtils;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArencoSessionServiceImpl implements ArencoSessionService {
  private final AuthenticationProperties authenticationProperties;
  private final SessionModelRepository sessionModelRepository;
  private final UserModelRepository userModelRepository;

  @Override
  public UserModel getCurrentUser() {
    final Authentication authenticationContext =
        SecurityContextHolder.getContext().getAuthentication();
    if (!(authenticationContext
        instanceof ArencoAuthenticatedSessionContext arencoAuthenticatedSessionContext)) {
      throw new IllegalStateException(
          "Current SecurityContext is not an ArencoAuthenticatedSessionContext");
    }
    return arencoAuthenticatedSessionContext.getUser();
  }

  @Override
  public void removeSession(final String sessionId) {
    final String hashed = ArencoSessionUtils.hashSessionId(sessionId, authenticationProperties.getSessionPepper());
    final var sessionOptional = sessionModelRepository.findById(hashed);
    if (sessionOptional.isPresent()) {
      log.info("Removing session with id {} from session repository", hashed);
      sessionModelRepository.delete(sessionOptional.get());
    }
  }

  @Override
  public SessionRecord createSession(final String userId) {
    final var requestContext = RequestContext.get();
    final var userAgent = requestContext.userAgent();
    final var ip = requestContext.ip();
    final String rawUuid = UUID.randomUUID().toString();
    final String hashed = ArencoSessionUtils.hashSessionId(rawUuid, authenticationProperties.getSessionPepper());

    final SessionModel session =
        new SessionModel(
            hashed, ip, userId, userAgent, Instant.now(), Instant.now().plus(Duration.ofDays(1)));

    sessionModelRepository.save(session);
    return new SessionRecord(rawUuid, session);
  }

  @Override
  public UserModel getUserBySessionId(final String sessionId) {
    final String hashed = ArencoSessionUtils.hashSessionId(sessionId, authenticationProperties.getSessionPepper());
    final AtomicReference<String> userId = new AtomicReference<>();
    sessionModelRepository.findById(hashed).ifPresent(session -> userId.set(session.getUserId()));
    return getUserModel(userId);
  }

  protected UserModel getUserModel(final AtomicReference<String> userId)
      throws InvalidSessionUserException, InvalidSessionException {
    if (userId.get() == null) {
      throw new InvalidSessionException();
    }
    final AtomicReference<UserModel> user = new AtomicReference<>();
    userModelRepository.findById(userId.get()).ifPresent(user::set);
    if (user.get() == null) {
      throw new InvalidSessionUserException(userId.get());
    }
    return user.get();
  }
}
