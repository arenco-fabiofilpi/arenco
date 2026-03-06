package br.com.arenco.arenco_clientes.services.impl;

import br.com.arenco.arenco_clientes.services.ArencoAuthLibContactService;
import br.com.arenco.arenco_clientes.entities.ContactModel;
import br.com.arenco.arenco_clientes.entities.UserModel;
import br.com.arenco.arenco_clientes.repositories.ContactModelRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArencoAuthLibContactServiceImpl implements ArencoAuthLibContactService {
  private final ContactModelRepository repository;

  @Override
  public List<ContactModel> findByUser(final UserModel user) {
    return repository.findByUserModelId(user.getId());
  }
}
