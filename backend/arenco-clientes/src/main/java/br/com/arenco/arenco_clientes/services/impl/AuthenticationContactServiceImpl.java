package br.com.arenco.arenco_clientes.services.impl;

import br.com.arenco.arenco_clientes.exceptions.NoContactFoundException;
import br.com.arenco.arenco_clientes.services.AuthenticationContactService;
import br.com.arenco.arenco_clientes.entities.ContactModel;
import br.com.arenco.arenco_clientes.entities.UserModel;
import br.com.arenco.arenco_clientes.repositories.ContactModelRepository;
import br.com.arenco.arenco_clientes.repositories.ContactPreferenceModelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationContactServiceImpl implements AuthenticationContactService {

  private final ContactPreferenceModelRepository contactPreferenceModelRepository;
  private final ContactModelRepository contactModelRepository;

  @Override
  public ContactModel getPreferredContact(final UserModel userModel) {
    final var optionalContactPreferenceModel =
        contactPreferenceModelRepository.findFirstByUserModelIdOrderByDateCreatedDesc(
            userModel.getId());
    if (optionalContactPreferenceModel.isPresent()) {
      return getContactById(optionalContactPreferenceModel.get().getContactModelId());
    }
    final var contacts = contactModelRepository.findByUserModelId(userModel.getId());
    if (contacts.isEmpty()) {
      throw new NoContactFoundException(userModel.getId());
    }
    return contacts.getFirst();
  }

  private ContactModel getContactById(final String id) {
    final var optional = contactModelRepository.findById(id);
    if (optional.isPresent()) {
      return optional.get();
    }
    throw new IllegalArgumentException("UUID não encontrado");
  }
}
