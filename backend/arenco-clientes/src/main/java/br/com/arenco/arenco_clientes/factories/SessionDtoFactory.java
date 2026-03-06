package br.com.arenco.arenco_clientes.factories;

import br.com.arenco.arenco_clientes.dtos.SessionDto;
import br.com.arenco.arenco_clientes.utils.ArencoDateUtils;
import br.com.arenco.arenco_clientes.entities.SessionModel;

public record SessionDtoFactory(SessionModel model, String username) {
  public SessionDto create() {
    return new SessionDto(
        model.getHashedId(),
        username,
        model.getIp(),
        model.getUserAgent(),
        ArencoDateUtils.fromInstantToString(model.getCreatedAt()),
        ArencoDateUtils.fromInstantToString(model.getExpiresAt()));
  }
}
