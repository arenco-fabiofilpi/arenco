package br.com.arenco.arenco_clientes.services.impl;

import br.com.arenco.arenco_clientes.context.RequestContext;
import br.com.arenco.arenco_clientes.services.ArencoUserAuthService;
import br.com.arenco.arenco_clientes.entities.UserModel;
import br.com.arenco.arenco_clientes.repositories.UserModelRepository;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArencoUserAuthServiceImpl implements ArencoUserAuthService {
  private static final BCryptPasswordEncoder B_CRYPT_PASSWORD_ENCODER = new BCryptPasswordEncoder();

  private final AuthenticationManager authenticationManager;
  private final UserModelRepository userModelRepository;

  @Override
  public Optional<UserModel> findUserByUsername(final String username) {
    return userModelRepository.findByUsername(username);
  }

  @Override
  public UserModel findUserById(final String id, final Class<? extends Exception> exceptionClass) {
    return userModelRepository
        .findById(id)
        .orElseThrow(() -> createUserNotFoundException(exceptionClass));
  }

  @Override
  public UserModel findUserByUsername(
      final String username, final Class<? extends Exception> exceptionClass) {
    return findUserByUsername(username)
        .orElseThrow(() -> createUserNotFoundException(exceptionClass));
  }

  @Override
  public void setNewPassword(final UserModel user, final String password) {
    final var encodedPassword = B_CRYPT_PASSWORD_ENCODER.encode(password);
    user.setPassword(encodedPassword);
    userModelRepository.save(user);
  }

  @Override
  public void authenticateUserAndPassword(final UserModel userModel, final String password) {
    final Authentication authenticationRequest =
        new UsernamePasswordAuthenticationToken(userModel.getUsername(), password);
    final Authentication authentication = authenticationManager.authenticate(authenticationRequest);
    log.debug(authentication.toString());
    final var context = RequestContext.get();
    log.info(
        "Authenticated successfully Agent {} IP {} and User {}",
        context.userAgent(),
        context.ip(),
        userModel.getId());
  }

  private RuntimeException createUserNotFoundException(
      final Class<? extends Exception> exceptionClass) {
    try {
      final Exception exception =
          exceptionClass.getDeclaredConstructor(String.class).newInstance("Usuário não encontrado");
      if (exception instanceof final RuntimeException runtimeException) {
        return runtimeException;
      } else {
        return new IllegalArgumentException(
            "A exceção fornecida não é uma RuntimeException", exception);
      }
    } catch (final Exception e) {
      throw new IllegalArgumentException("Erro ao instanciar a exceção fornecida", e);
    }
  }
}
