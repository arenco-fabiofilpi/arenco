package br.com.arenco.arenco_clientes.services.contact.impl;

import br.com.arenco.arenco_clientes.services.contact.ContactService;
import br.com.arenco.arenco_clientes.utils.ServiceUtils;
import br.com.arenco.arenco_clientes.entities.ContactModel;
import br.com.arenco.arenco_clientes.repositories.ContactModelRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {
  private final ContactModelRepository contactRepository;

  @Override
  public Page<ContactModel> findAll(final Pageable pageable) {
    return contactRepository.findAll(pageable);
  }

  @Override
  public ContactModel findById(
      @NonNull final String id, final Class<? extends Exception> exceptionClass) {
    return contactRepository
        .findById(id)
        .orElseThrow(
            () -> ServiceUtils.createIdNotFoundException(exceptionClass, "Contato não encontrado"));
  }
}
