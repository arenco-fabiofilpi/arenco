package br.com.arenco.arenco_clientes.facades;

import br.com.arenco.arenco_clientes.dtos.user.AddressDto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AddressFacade {
  Page<AddressDto> search(final Pageable pageable);

  Optional<AddressDto> findById(final String id);
}
