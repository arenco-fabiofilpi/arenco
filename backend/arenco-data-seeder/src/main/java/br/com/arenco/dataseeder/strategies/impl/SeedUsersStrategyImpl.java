package br.com.arenco.dataseeder.strategies.impl;

import br.com.arenco.dataseeder.entities.UserModel;
import br.com.arenco.dataseeder.enums.LoginMethod;
import br.com.arenco.dataseeder.enums.UserType;
import br.com.arenco.dataseeder.repositories.UserModelRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeedUsersStrategyImpl extends AbstractDataSeederStrategy {
  private static final BCryptPasswordEncoder B_CRYPT_PASSWORD_ENCODER = new BCryptPasswordEncoder();

  private static final String COLLECTION_CODE = "usuarios";

  private final UserModelRepository userModelRepository;

  @Override
  public String getCollectionCode() {
    return COLLECTION_CODE;
  }

  void create(final JsonNode doc) {
    final String username = doc.get("username").asText();

    if (userModelRepository.existsByUsername(username)) {
      log.info("Usuário já existente");
      return;
    }

    var userModel = new UserModel();
    userModel.setUsername(doc.get("username").asText());
    userModel.setName(doc.get("name").asText());
    setLoginMethod(doc, userModel);
    setIdErp(doc, userModel);
    setGrupoContabil(doc, userModel);
    setPassword(doc, userModel);
    setType(userModel, doc.get("type").asText());

    userModelRepository.save(userModel);
  }

  private void setGrupoContabil(final JsonNode doc, final UserModel userModel) {
    if (doc.has("grupoContabil")) {
      userModel.setGrupoContabil(doc.get("grupoContabil").asText());
    }
  }

  private void setIdErp(final JsonNode doc, final UserModel userModel) {
    if (doc.has("idErp")) {
      userModel.setIdErp(doc.get("idErp").asInt());
    }
  }

  private void setPassword(final JsonNode doc, final UserModel userModel) {
    if (doc.has("password")) {
      setNewPassword(userModel, doc.get("password").asText());
    }
  }

  private void setNewPassword(final UserModel user, final String password) {
    final var encodedPassword = B_CRYPT_PASSWORD_ENCODER.encode(password);
    user.setPassword(encodedPassword);
    userModelRepository.save(user);
  }

  private void setLoginMethod(final JsonNode doc, final UserModel userModel) {
    final var loginMethodAttributeName = "loginMethod";
    if (doc.has(loginMethodAttributeName)) {
      final var loginMethod = doc.get(loginMethodAttributeName).asText();
      try {
        final var loginMethodEnum = LoginMethod.valueOf(loginMethod.toUpperCase());
        userModel.setLoginMethod(loginMethodEnum);
      } catch (final IllegalArgumentException e) {
        log.error("Invalid login method {}", loginMethodAttributeName);
      }
    }
  }

  private void setType(final UserModel userModel, final String typeAsString) {
    final var type = UserType.valueOf(typeAsString);
    userModel.setUserType(type);
  }
}
