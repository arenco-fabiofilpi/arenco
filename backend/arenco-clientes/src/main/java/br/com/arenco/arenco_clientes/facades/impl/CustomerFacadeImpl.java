package br.com.arenco.arenco_clientes.facades.impl;

import br.com.arenco.arenco_clientes.dtos.ClienteExportDTO;
import br.com.arenco.arenco_clientes.facades.CustomerFacade;
import br.com.arenco.arenco_clientes.facades.UsersFacade;
import br.com.arenco.arenco_clientes.mappers.ClienteExportDTOMapper;
import br.com.arenco.arenco_clientes.services.users.UserService;
import br.com.arenco.arenco_clientes.dtos.user.UserDto;
import br.com.arenco.arenco_clientes.factories.UserDtoFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerFacadeImpl implements CustomerFacade {
  private final UsersFacade usersFacade;
  private final UserService userService;

  @Override
  public Page<UserDto> findCustomers(final Pageable pageable) {
    final var modelPage = userService.findAllCustomers(pageable);
    return modelPage.map(model -> new UserDtoFactory(model).create());
  }

  @Override
  public Optional<UserDto> findCustomerById(final String id) {
    final var model = usersFacade.findUser(id);
    if (model == null) {
      return Optional.empty();
    }
    final var customerDto = new UserDtoFactory(model).create();
    return Optional.of(customerDto);
  }

  @Override
  public List<ClienteExportDTO> buscarParaExportacao(final List<String> ids) {
    final var listaDeClientes = userService.findAllCustomers(ids);
    final var dtoList = new ArrayList<ClienteExportDTO>();
    for (final var cliente : listaDeClientes) {
      final var userDto = new ClienteExportDTOMapper(cliente).create();
      dtoList.add(userDto);
    }
    return dtoList;
  }
}
