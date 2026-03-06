package br.com.arenco.arenco_clientes.services.address.impl;

import br.com.arenco.arenco_clientes.services.address.AddressService;
import br.com.arenco.arenco_clientes.entities.AddressModel;
import br.com.arenco.arenco_clientes.repositories.AddressModelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
  private final AddressModelRepository addressRepository;

  @Override
  public Page<AddressModel> findAll(final Pageable pageable) {
    return addressRepository.findAll(pageable);
  }
}
