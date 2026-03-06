package br.com.arenco.arenco_clientes.facades;

import br.com.arenco.arenco_clientes.dtos.settings.BetaUserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

public interface ArencoMessengerBetaUsersFacade {
  Page<BetaUserDto> findAll(final Pageable pageable);

  HttpStatus addBetaUser(final String userModelId);

  void removeBetaUser(final String betaUserId);
}
