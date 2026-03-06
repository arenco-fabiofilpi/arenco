package br.com.arenco.arenco_clientes.facades.impl;

import br.com.arenco.arenco_clientes.facades.AddressFacade;
import br.com.arenco.arenco_clientes.services.address.AddressService;
import br.com.arenco.arenco_clientes.services.users.UserService;
import br.com.arenco.arenco_clientes.dtos.user.AddressDto;
import br.com.arenco.arenco_clientes.exceptions.IdNotFoundException;
import br.com.arenco.arenco_clientes.factories.AddressDtoFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AddressFacadeImpl implements AddressFacade {
  private final AddressService addressService;
  private final UserService userService;

  @Override
  public Page<AddressDto> search(final Pageable pageable) {
    final var modelPage = addressService.findAll(pageable);
    return modelPage.map(
        addressModel -> {
          final var userId = addressModel.getUserId();
          final var userModel = userService.findUserById(userId, IdNotFoundException.class);
          return new AddressDtoFactory(addressModel, userModel).create();
        });
  }

  @Override
  public Optional<AddressDto> findById(final String id) {
    return Optional.empty();
  }
}
