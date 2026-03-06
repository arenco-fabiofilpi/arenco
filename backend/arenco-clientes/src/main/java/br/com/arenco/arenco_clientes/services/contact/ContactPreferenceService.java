package br.com.arenco.arenco_clientes.services.contact;

import br.com.arenco.arenco_clientes.entities.ContactPreferenceModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContactPreferenceService {
  Page<ContactPreferenceModel> findAll(final Pageable pageable);
}
