package br.com.arenco.arenco_clientes.services.contact.impl;

import br.com.arenco.arenco_clientes.services.contact.ContactPreferenceService;
import br.com.arenco.arenco_clientes.entities.ContactPreferenceModel;
import br.com.arenco.arenco_clientes.repositories.ContactPreferenceModelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactPreferenceServiceImpl implements ContactPreferenceService {
  private final ContactPreferenceModelRepository contactPreferenceModelRepository;

  @Override
  public Page<ContactPreferenceModel> findAll(final Pageable pageable) {
    return contactPreferenceModelRepository.findAll(pageable);
  }
}
