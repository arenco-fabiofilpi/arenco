package br.com.arenco.arenco_cronjobs.services.impl;

import br.com.arenco.arenco_cronjobs.entities.*;
import br.com.arenco.arenco_cronjobs.enums.LoginMethod;
import br.com.arenco.arenco_cronjobs.enums.UserGroups;
import br.com.arenco.arenco_cronjobs.enums.UserType;
import br.com.arenco.arenco_cronjobs.exceptions.ClienteSemDocumentoException;
import br.com.arenco.arenco_cronjobs.oracle.entities.ClienteOracle;
import br.com.arenco.arenco_cronjobs.repositories.UserGroupModelRepository;
import br.com.arenco.arenco_cronjobs.repositories.UserModelRepository;
import br.com.arenco.arenco_cronjobs.repositories.UsersToGroupsRelationModelRepository;
import br.com.arenco.arenco_cronjobs.services.ArencoSincronizacaoClientesService;
import br.com.arenco.arenco_cronjobs.services.ArencoSincronizacaoContatosService;
import br.com.arenco.arenco_cronjobs.services.ArencoSincronizacaoEnderecosService;
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
    setAdditionalInfo(clienteOracle, userModel);
  }

  private static void setAdditionalInfo(
      final ClienteOracle clienteOracle, final UserModel userModel) {
    userModel.setAssina(clienteOracle.getAssina());
    userModel.setBairro(clienteOracle.getBairro());
    userModel.setBairroCobranca(clienteOracle.getBairroCobranca());
    userModel.setBairroEntrega(clienteOracle.getBairroEntrega());
    userModel.setCadastrante(clienteOracle.getCadastrante());
    userModel.setCaixaPostal(clienteOracle.getCaixaPostal());
    userModel.setCaixaPostalCobranca(clienteOracle.getCaixaPostalCobranca());
    userModel.setCaixaPostalEntrega(clienteOracle.getCaixaPostalEntrega());
    userModel.setCapitalSocial(clienteOracle.getCapitalSocial());
    userModel.setCategoria(clienteOracle.getCategoria());
    userModel.setCelular1(clienteOracle.getCelular1());
    userModel.setCelular2(clienteOracle.getCelular2());
    userModel.setCelular3(clienteOracle.getCelular3());
    userModel.setCep1(clienteOracle.getCep1());
    userModel.setCep1Cobranca(clienteOracle.getCep1Cobranca());
    userModel.setCep1Entrega(clienteOracle.getCep1Entrega());
    userModel.setCep2(clienteOracle.getCep2());
    userModel.setCep2Cobranca(clienteOracle.getCep2Cobranca());
    userModel.setCep2Entrega(clienteOracle.getCep2Entrega());
    userModel.setCgcDac(clienteOracle.getCgcDac());
    userModel.setCgcFilial(clienteOracle.getCgcFilial());
    userModel.setCgcNumero(clienteOracle.getCgcNumero());
    userModel.setCicDac(clienteOracle.getCicDac());
    userModel.setCicNume(clienteOracle.getCicNume());
    userModel.setCidade(clienteOracle.getCidade());
    userModel.setCidadeCobranca(clienteOracle.getCidadeCobranca());
    userModel.setCidadeEntrega(clienteOracle.getCidadeEntrega());
    userModel.setCnaeAtividade(clienteOracle.getCnaeAtividade());
    userModel.setCnaeDacativ(clienteOracle.getCnaeDacativ());
    userModel.setCnaeFiscalAtiv(clienteOracle.getCnaeFiscalAtiv());
    userModel.setCodigoMunicipio(clienteOracle.getCodigoMunicipio());
    userModel.setComplende(clienteOracle.getComplende());
    userModel.setComplendeCobranca(clienteOracle.getComplendeCobranca());
    userModel.setComplendeEntrega(clienteOracle.getComplendeEntrega());
    userModel.setConsumidorFinal(clienteOracle.getConsumidorFinal());
    userModel.setContato(clienteOracle.getContato());
    userModel.setDacCpfConjuge(clienteOracle.getDacCpfConjuge());
    userModel.setDataAlteracao(clienteOracle.getDataAlteracao());
    userModel.setDataAlteracaoJcoml(clienteOracle.getDataAlteracaoJcoml());
    userModel.setDataCadastramento(clienteOracle.getDataCadastramento());
    userModel.setDataExpedicaoRg(clienteOracle.getDataExpedicaoRg());
    userModel.setDataNascimento(clienteOracle.getDataNascimento());
    userModel.setDataRegistroJcoml(clienteOracle.getDataRegistroJcoml());
    userModel.setDdd(clienteOracle.getDdd());
    userModel.setDddCobranca(clienteOracle.getDddCobranca());
    userModel.setDddEntrega(clienteOracle.getDddEntrega());
    userModel.setEmailComercial(clienteOracle.getEmailComercial());
    userModel.setEmailInternet(clienteOracle.getEmailInternet());
    userModel.setEmUso(clienteOracle.getEmUso());
    userModel.setEndereco(clienteOracle.getEndereco());
    userModel.setEnderecoCobranca(clienteOracle.getEnderecoCobranca());
    userModel.setEnderecoEntrega(clienteOracle.getEnderecoEntrega());
    userModel.setEstado(clienteOracle.getEstado());
    userModel.setEstadoCobranca(clienteOracle.getEstadoCobranca());
    userModel.setEstadoEntrega(clienteOracle.getEstadoEntrega());
    userModel.setFaxCobranca(clienteOracle.getFaxCobranca());
    userModel.setFaxEntrega(clienteOracle.getFaxEntrega());
    userModel.setFone1(clienteOracle.getFone1());
    userModel.setFone2(clienteOracle.getFone2());
    userModel.setFone3(clienteOracle.getFone3());
    userModel.setFone4(clienteOracle.getFone4());
    userModel.setFoneCobranca(clienteOracle.getFoneCobranca());
    userModel.setFoneEntrega(clienteOracle.getFoneEntrega());
    userModel.setFonefax(clienteOracle.getFonefax());
    userModel.setFormulaCredito(clienteOracle.getFormulaCredito());
    userModel.setInscricao(clienteOracle.getInscricao());
    userModel.setInss(clienteOracle.getInss());
    userModel.setMatriz(clienteOracle.getMatriz());
    userModel.setNascimento(clienteOracle.getNascimento());
    userModel.setNomeConjuge(clienteOracle.getNomeConjuge());
    userModel.setNomeFantasia(clienteOracle.getNomeFantasia());
    userModel.setNumero(clienteOracle.getNumero());
    userModel.setNumeroAltJcoml(clienteOracle.getNumeroAltJcoml());
    userModel.setNumeroCobranca(clienteOracle.getNumeroCobranca());
    userModel.setNumeroCpfConjuge(clienteOracle.getNumeroCpfConjuge());
    userModel.setNumeroEntrega(clienteOracle.getNumeroEntrega());
    userModel.setNumeroJcoml(clienteOracle.getNumeroJcoml());
    userModel.setObservacao(clienteOracle.getObservacao());
    userModel.setPais(clienteOracle.getPais());
    userModel.setParecerVendedor(clienteOracle.getParecerVendedor());
    userModel.setParticipacao(clienteOracle.getParticipacao());
    userModel.setProfissao(clienteOracle.getProfissao());
    userModel.setRgDacConjuge(clienteOracle.getRgDacConjuge());
    userModel.setRgEmissao(clienteOracle.getRgEmissao());
    userModel.setRgNume(clienteOracle.getRgNume());
    userModel.setRgNumeroConjuge(clienteOracle.getRgNumeroConjuge());
    userModel.setRgUf(clienteOracle.getRgUf());
    userModel.setTipoProcuracao(clienteOracle.getTipoProcuracao());
    userModel.setUtilizaCepComoPadrao(clienteOracle.getUtilizaCepComoPadrao());
    userModel.setUtilizaGeraParcelasReceber(clienteOracle.getUtilizaGeraParcelasReceber());
    userModel.setVendedor(clienteOracle.getVendedor());
    userModel.setWwwInternet(clienteOracle.getWwwInternet());
  }
}
