package br.com.arenco.arenco_clientes.facades;

import br.com.arenco.arenco_clientes.dtos.user.UserDto;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerFacade {
  Page<@NonNull UserDto> findCustomers(final Pageable pageable);

  Optional<UserDto> findCustomerById(final String id);

  byte[] exportar(final List<String> ids) throws IOException;
}
