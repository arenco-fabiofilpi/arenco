package br.com.arenco.arenco_clientes.dtos.authentication;

import br.com.arenco.arenco_clientes.entities.AuthenticatedPreSessionModel;

public record PreSessionRecord(
    String rawUuid, AuthenticatedPreSessionModel authenticatedPreSessionModel) {}
