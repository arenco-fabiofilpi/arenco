package br.com.arenco.arenco_cronjobs.services.impl;

import br.com.arenco.arenco_cronjobs.exceptions.ClienteSemDocumentoException;
import br.com.arenco.arenco_cronjobs.oracle.entities.ClienteOracle;
import br.com.arenco.arenco_cronjobs.services.ArencoSincronizacaoClientesService;
import br.com.arenco.arenco_cronjobs.services.ArencoSincronizacaoContatosService;
import br.com.arenco.arenco_cronjobs.services.ArencoSincronizacaoEnderecosService;
import br.com.arenco.arenco_cronjobs.entities.*;
import br.com.arenco.arenco_cronjobs.enums.LoginMethod;
import br.com.arenco.arenco_cronjobs.enums.UserGroups;
import br.com.arenco.arenco_cronjobs.enums.UserType;
import br.com.arenco.arenco_cronjobs.repositories.UserGroupModelRepository;
import br.com.arenco.arenco_cronjobs.repositories.UserModelRepository;
import br.com.arenco.arenco_cronjobs.repositories.UsersToGroupsRelationModelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArencoSincronizacaoClientesServiceImpl implements ArencoSincronizacaoClientesService {
  private final UserModelRepository userModelRepository;
  private final UserGroupModelRepository userGroupModelRepository;
  private final ArencoSincronizacaoContatosService arencoSincronizacaoContatosService;
  private final ArencoSincronizacaoEnderecosService arencoSincronizacaoEnderecosService;
  private final UsersToGroupsRelationModelRepository usersToGroupsRelationModelRepository;

  @Override
  public UserModel replicateCustomerFromOracleToMongo(final ClienteOracle clienteOracle) {
    final UserModel user = pegarClienteModel(clienteOracle);
    sincronizarDados(user, clienteOracle);
    userModelRepository.save(user);
    return user;
  }

  private UserModel pegarClienteModel(final ClienteOracle clienteOracle) {
    return userModelRepository
        .findFirstByIdErp(Integer.parseInt(clienteOracle.getCodigo()))
        .orElseGet(
            () -> {
              log.info("Criando cliente com idErp {}", clienteOracle.getCodigo());
              return iniciarClienteModel(clienteOracle);
            });
  }

  private void sincronizarDados(final UserModel user, final ClienteOracle clienteOracle) {
    arencoSincronizacaoContatosService.sincronizarListaDeContatos(user, clienteOracle);
    arencoSincronizacaoEnderecosService.sincronizarListaDeEnderecos(user, clienteOracle);
    user.setName(clienteOracle.getNome() != null ? clienteOracle.getNome() : "");
  }

  private UserModel iniciarClienteModel(final ClienteOracle customer) {
    final boolean isLegalEntity = validateIfCustomerIsLegalEntity(customer);
    final boolean isNaturalPerson = validateIfCustomerIsNaturalPerson(customer);

    if (isLegalEntity == isNaturalPerson) {
      throw new ClienteSemDocumentoException(
          String.format(
              "Cliente com idErp %s não possui CNPJ ou CPF válido", customer.getCodigo()));
    }

    return isLegalEntity ? criarPessoaJuridica(customer) : criarPessoaFisica(customer);
  }

  private UserModel criarPessoaJuridica(final ClienteOracle customer) {
    final UserModel clientePessoaJuridica = new UserModel();
    final String cnpj = convertCnpj(customer);
    clientePessoaJuridica.setCnpj(cnpj);
    clientePessoaJuridica.setUsername(cnpj);
    setCommonInfo(customer, clientePessoaJuridica);
    setLegalEntityGroup(clientePessoaJuridica);
    userModelRepository.save(clientePessoaJuridica);
    return clientePessoaJuridica;
  }

  private UserModel criarPessoaFisica(final ClienteOracle customer) {
    final UserModel clientePessoaFisica = new UserModel();
    final var cpf = convertCpf(customer);
    clientePessoaFisica.setCpf(cpf);
    clientePessoaFisica.setUsername(cpf);
    setCommonInfo(customer, clientePessoaFisica);
    setNaturalPersonGroup(clientePessoaFisica);
    userModelRepository.save(clientePessoaFisica);
    return clientePessoaFisica;
  }

  private boolean validateIfCustomerIsLegalEntity(final ClienteOracle customer) {
    return isValidNumber(customer.getCgcNumero())
        && isValidNumber(customer.getCgcFilial())
        && isValidNumber(customer.getCgcDac());
  }

  private boolean validateIfCustomerIsNaturalPerson(final ClienteOracle customer) {
    return isValidNumber(customer.getCicNume()) && isValidNumber(customer.getCicDac());
  }

  private boolean isValidNumber(final String number) {
    return number != null && !"0".equals(number);
  }

  private String convertCpf(final ClienteOracle cliente) {
    final String cicDac = String.format("%02d", Long.parseLong(cliente.getCicDac()));
    return formatCpf(cliente.getCicNume() + cicDac);
  }

  private String convertCnpj(final ClienteOracle cliente) {
    return formatCnpj(cliente.getCgcNumero(), cliente.getCgcFilial(), cliente.getCgcDac());
  }

  private void setNaturalPersonGroup(final UserModel naturalPersonUser) {
    final var userGroupOptional =
        userGroupModelRepository.findByCode(UserGroups.NATURAL_PERSONS.getCode());
    if (userGroupOptional.isEmpty()) {
      throw new IllegalArgumentException(
          String.format("UserGroup %s not found", UserGroups.NATURAL_PERSONS.getCode()));
    }
    final var userGroup = userGroupOptional.get();
    final var isUserAlreadyAtNaturalPersonGroup =
        usersToGroupsRelationModelRepository.existsByUserIdAndUserGroupId(
            naturalPersonUser.getId(), userGroup.getId());
    if (isUserAlreadyAtNaturalPersonGroup) {
      log.debug("Customer already has Natural Persons group");
      return;
    }
    final var userToUserGroupRelation = new UsersToGroupsRelationModel();
    userToUserGroupRelation.setUserId(naturalPersonUser.getId());
    userToUserGroupRelation.setUserGroupId(userGroup.getId());
    usersToGroupsRelationModelRepository.save(userToUserGroupRelation);
  }

  private void setLegalEntityGroup(final UserModel legalEntityUser) {
    final var userGroupOptional =
        userGroupModelRepository.findByCode(UserGroups.LEGAL_ENTITIES.getCode());
    if (userGroupOptional.isEmpty()) {
      throw new IllegalArgumentException(
          String.format("UserGroup %s not found", UserGroups.LEGAL_ENTITIES.getCode()));
    }
    final var userGroup = userGroupOptional.get();
    final var isUserAlreadyAtLegalEntityGroup =
        usersToGroupsRelationModelRepository.existsByUserIdAndUserGroupId(
            legalEntityUser.getId(), userGroup.getId());
    if (isUserAlreadyAtLegalEntityGroup) {
      log.debug("Customer already has legal entity group");
      return;
    }
    final var userToUserGroupRelation = new UsersToGroupsRelationModel();
    userToUserGroupRelation.setUserId(legalEntityUser.getId());
    userToUserGroupRelation.setUserGroupId(userGroup.getId());
    usersToGroupsRelationModelRepository.save(userToUserGroupRelation);
  }

  private static String formatCnpj(String cgcNumero, String cgcFilial, String cgcDac) {
    if (cgcNumero == null || cgcFilial == null || cgcDac == null) {
      throw new ClienteSemDocumentoException("CNPJ, filial ou DAC não podem ser nulos");
    }
    if (cgcNumero.length() != 8) {
      cgcNumero = String.format("%08d", Long.parseLong(cgcNumero));
    }
    if (cgcFilial.length() != 4) {
      cgcFilial = String.format("%04d", Long.parseLong(cgcFilial));
    }
    if (cgcDac.length() != 2) {
      cgcFilial = String.format("%02d", Long.parseLong(cgcFilial));
    }

    return String.format(
        "%02d.%03d.%03d/%04d-%02d",
        Integer.parseInt(cgcNumero.substring(0, 2)),
        Integer.parseInt(cgcNumero.substring(2, 5)),
        Integer.parseInt(cgcNumero.substring(5, 8)),
        Integer.parseInt(cgcFilial),
        Integer.parseInt(cgcDac));
  }

  private static String formatCpf(final String cpf) {
    final String cleanedCpf = cpf.replaceAll("\\D", "");
    final String formattedCpf = String.format("%011d", Long.parseLong(cleanedCpf));
    return String.format(
        "%s.%s.%s-%s",
        formattedCpf.substring(0, 3),
        formattedCpf.substring(3, 6),
        formattedCpf.substring(6, 9),
        formattedCpf.substring(9, 11));
  }

  private static void setCommonInfo(final ClienteOracle clienteOracle, final UserModel userModel) {
    userModel.setIdErp(Integer.parseInt(clienteOracle.getCodigo()));
    userModel.setGrupoContabil(clienteOracle.getGrupoContabil());
    userModel.setUserType(UserType.CLIENTE);
    userModel.setLoginMethod(LoginMethod.USERNAME_ONLY);
  }
}
