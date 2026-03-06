package br.com.arenco.dataseeder.strategies.impl;

import br.com.arenco.dataseeder.strategies.DataSeederStrategy;
import br.com.arenco.dataseeder.entities.AgreementModel;
import br.com.arenco.dataseeder.entities.UserGroupModel;
import br.com.arenco.dataseeder.entities.UserModel;
import br.com.arenco.dataseeder.repositories.*;
import com.fasterxml.jackson.databind.JsonNode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public abstract class AbstractDataSeederStrategy implements DataSeederStrategy {
  @Autowired private UserModelRepository userModelRepository;
  @Autowired private UserGroupModelRepository userGroupModelRepository;
  @Autowired private AgreementModelRepository agreementModelRepository;

  abstract void create(final JsonNode doc);

  @Override
  public void seed(final JsonNode documents) {
    for (final JsonNode doc : documents) {
      try {
        create(doc);
      } catch (final RuntimeException e) {
        log.error("Error trying to save", e);
      }
    }
  }

  protected UserModel getUserModel(final JsonNode doc) {
    final String username = doc.path("userModel").path("$username").asText();
    final var userModelOpt = userModelRepository.findByUsername(username);
    if (userModelOpt.isEmpty()) {
      throw new IllegalArgumentException(String.format("Username %s not found", username));
    }
    return userModelOpt.get();
  }

  protected UserGroupModel getGroupModel(final JsonNode doc, final String path) {
    final String grupoCode = doc.path(path).path("$code").asText();

    final var superGrupoOpt = userGroupModelRepository.findByCode(grupoCode);
    if (superGrupoOpt.isEmpty()) {
      throw new IllegalArgumentException(String.format("Grupo code %s not found", grupoCode));
    }
    return superGrupoOpt.get();
  }

  protected UserGroupModel getGroupModel(final JsonNode doc) {
    return getGroupModel(doc, "grupoDeUsuarioModel");
  }

  protected AgreementModel getAgreementModel(final JsonNode doc) {
    final String numeContrato = doc.path("agreementModel").path("$numeContrato").asText();
    final var agreementOptional = agreementModelRepository.findByNumeContrato(numeContrato);
    if (agreementOptional.isEmpty()) {
      throw new IllegalArgumentException(
          String.format("Agreement code %s not found", numeContrato));
    }
    return agreementOptional.get();
  }

  protected static LocalDate toLocalDate(final JsonNode doc, final String path) {
    final var isoString = doc.path(path).path("$date").asText();
    return Instant.parse(isoString)
        .atZone(ZoneId.systemDefault()) // Converte para o fuso local da máquina
        .toLocalDate();
  }
}
