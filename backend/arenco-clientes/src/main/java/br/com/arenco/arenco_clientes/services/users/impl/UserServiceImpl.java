package br.com.arenco.arenco_clientes.services.users.impl;

import br.com.arenco.arenco_clientes.services.users.UserService;
import br.com.arenco.arenco_clientes.utils.ServiceUtils;
import br.com.arenco.arenco_clientes.entities.UserModel;
import br.com.arenco.arenco_clientes.enums.UserType;
import br.com.arenco.arenco_clientes.repositories.UserModelRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserModelRepository userRepository;

  @Override
  public Page<UserModel> findAllCustomers(final Pageable pageable) {
    return userRepository.findAllByUserType(UserType.CLIENTE, pageable);
  }

  @Override
  public List<UserModel> findAllCustomers(final List<String> ids) {
    return userRepository.findAllById(ids);
  }

  @Override
  public UserModel findUserById(final String id, final Class<? extends Exception> exceptionClass) {
    return findUserById(id)
        .orElseThrow(
            () -> ServiceUtils.createIdNotFoundException(exceptionClass, "Usuário não encontrado"));
  }

  @Override
  public Optional<UserModel> findUserById(final String id) {
    if (id == null) {
      return Optional.empty();
    }
    return userRepository.findById(id);
  }

  @Override
  public void save(final UserModel user) {
    userRepository.save(user);
  }

  //  @Override
  //  public Page<UserModel> findCustomers(
  //      final Pageable pageable, final Specification<UserModel> customerSpecification) {
  //    log.debug("CustomerServiceImpl: Find customers");
  //    //    return customerRepository.findAll(customerSpecification, pageable);
  //    return Page.empty();
  //  }

}
