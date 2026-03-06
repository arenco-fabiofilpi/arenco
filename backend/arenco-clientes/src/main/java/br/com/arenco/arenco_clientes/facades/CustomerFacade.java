package br.com.arenco.arenco_clientes.facades;

import br.com.arenco.arenco_clientes.dtos.ClienteExportDTO;
import br.com.arenco.arenco_clientes.dtos.user.UserDto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerFacade {
  Page<UserDto> findCustomers(final Pageable pageable);

  Optional<UserDto> findCustomerById(final String id);

  List<ClienteExportDTO> buscarParaExportacao(final List<String> ids);
}
