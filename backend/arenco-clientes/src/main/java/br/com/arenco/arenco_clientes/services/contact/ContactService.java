package br.com.arenco.arenco_clientes.services.contact;

import br.com.arenco.arenco_clientes.entities.ContactModel;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContactService {
  Page<ContactModel> findAll(final Pageable pageable);

  ContactModel findById(final String id, Class<? extends Exception> exceptionClass);
}
