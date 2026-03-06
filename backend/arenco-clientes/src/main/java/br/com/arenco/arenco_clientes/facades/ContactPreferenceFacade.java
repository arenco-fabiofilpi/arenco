package br.com.arenco.arenco_clientes.facades;

import br.com.arenco.arenco_clientes.dtos.user.ContactPreferenceDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContactPreferenceFacade {
  Page<ContactPreferenceDto> search(final Pageable pageable);
}
