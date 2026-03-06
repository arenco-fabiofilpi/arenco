package br.com.arenco.arenco_clientes.facades;

import br.com.arenco.arenco_clientes.dtos.user.ContactFullDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContactFacade {
  Page<ContactFullDto> search(final Pageable pageable);
}
