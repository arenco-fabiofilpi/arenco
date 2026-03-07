package br.com.arenco.arenco_clientes.facades.impl;

import br.com.arenco.arenco_clientes.dtos.ClienteExportDTO;
import br.com.arenco.arenco_clientes.dtos.user.UserDto;
import br.com.arenco.arenco_clientes.entities.UserModel;
import br.com.arenco.arenco_clientes.facades.CustomerFacade;
import br.com.arenco.arenco_clientes.facades.UsersFacade;
import br.com.arenco.arenco_clientes.factories.UserDtoFactory;
import br.com.arenco.arenco_clientes.repositories.*;
import br.com.arenco.arenco_clientes.services.users.ExportarClienteService;
import br.com.arenco.arenco_clientes.services.users.UserService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerFacadeImpl implements CustomerFacade {
  private final UsersFacade usersFacade;
  private final UserService userService;
  private final ExportarClienteService excelService;
  private final AgreementModelRepository agreementModelRepository;
  private final ReceivedTitleModelRepository receivedTitleModelRepository;
  private final CadTipoClienteModelRepository cadTipoClienteModelRepository;
  private final ReceivableTitleModelRepository receivableTitleModelRepository;
  private final CadClientesSocioModelRepository cadClientesSocioModelRepository;
  private final CadClientesOutrosModelRepository cadClientesOutrosModelRepository;
  private final CadClientesRefBancariasModelRepository cadClientesRefBancariasModelRepository;
  private final CadClientesRefComerciaisModelRepository cadClientesRefComerciaisModelRepository;

  @Override
  public Page<@NonNull UserDto> findCustomers(final Pageable pageable) {
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
  public byte[] exportar(final List<String> ids) throws IOException {
    final var listaDeClientes = userService.findAllCustomers(ids);
    final var dtoList = new ArrayList<ClienteExportDTO>();
    for (final UserModel userModel : listaDeClientes) {
      final var contratos = agreementModelRepository.findAllByUserId(userModel.getId());
      final var recebidosList =
          receivedTitleModelRepository.findByCliente(String.valueOf(userModel.getIdErp()));
      final var aReceberList = receivableTitleModelRepository.findByCliente(userModel.getIdErp());
      final var tipoClienteModelList =
          cadTipoClienteModelRepository.findAllByCliente(userModel.getIdErp());
      final var outrosDadosClienteList =
          cadClientesOutrosModelRepository.findAllByCliente(userModel.getIdErp());
      final var socios = cadClientesSocioModelRepository.findAllByCliente(userModel.getIdErp());
      final var refBancarias =
          cadClientesRefBancariasModelRepository.findAllByCliente(userModel.getIdErp());
      final var refComerciais =
          cadClientesRefComerciaisModelRepository.findAllByCliente(userModel.getIdErp());
      final var dto =
          new ClienteExportDTO(
              userModel,
              tipoClienteModelList,
              outrosDadosClienteList,
              socios,
              refBancarias,
              refComerciais,
              contratos,
              aReceberList,
              recebidosList);
      dtoList.add(dto);
    }
    return excelService.gerarPlanilhaComDados(dtoList);
  }
}
