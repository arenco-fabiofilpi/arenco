package br.com.arenco.arenco_clientes.dtos.authentication;

import br.com.arenco.arenco_clientes.entities.SessionModel;

public record SessionRecord(String rawUuid, SessionModel sessionModel) {}
