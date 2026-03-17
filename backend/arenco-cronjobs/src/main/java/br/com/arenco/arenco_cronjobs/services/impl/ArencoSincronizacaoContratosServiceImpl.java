package br.com.arenco.arenco_cronjobs.services.impl;

import br.com.arenco.arenco_cronjobs.entities.AgreementModel;
import br.com.arenco.arenco_cronjobs.entities.BoletoAProcessarModel;
import br.com.arenco.arenco_cronjobs.entities.UserModel;
import br.com.arenco.arenco_cronjobs.mappers.ContratoMapper;
import br.com.arenco.arenco_cronjobs.oracle.entities.ContratoOracle;
import br.com.arenco.arenco_cronjobs.oracle.entities.TituloAReceberOracle;
import br.com.arenco.arenco_cronjobs.oracle.entities.TituloRecebidoOracle;
import br.com.arenco.arenco_cronjobs.repositories.AgreementModelRepository;
import br.com.arenco.arenco_cronjobs.services.ArencoSincronizacaoContratosService;
import br.com.arenco.arenco_cronjobs.services.ArencoSincronizacaoTitulosService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArencoSincronizacaoContratosServiceImpl
    implements ArencoSincronizacaoContratosService {

  private final AgreementModelRepository agreementModelRepository;
  private final ArencoSincronizacaoTitulosService arencoSincronizacaoTitulosService;

  @Override
  public List<BoletoAProcessarModel> sincronizarContrato(
      final UserModel clienteModel,
      final ContratoOracle contratoOracle,
      final List<TituloAReceberOracle> tituloAReceberOracleList,
      final List<TituloRecebidoOracle> tituloRecebidoOracleList) {
    final var boletosAProcessar = new ArrayList<BoletoAProcessarModel>();
    final var numeroContrato = contratoOracle.getNumeContrato();

    final var empresa = contratoOracle.getEmpresa();

    log.debug("Iniciando sincronização do contrato {} para empresa {}", numeroContrato, empresa);

    if (clienteModel == null) {
      throw new IllegalArgumentException(
          String.format("Cliente nulo para o contrato %s da empresa %s", numeroContrato, empresa));
    }

    final var contratoMongo = pegarAgreementModel(empresa, contratoOracle, clienteModel);

    log.debug("Atualizando contrato {} com dados do Oracle", numeroContrato);
    ContratoMapper.atualizarContrato(contratoMongo, contratoOracle);
    agreementModelRepository.save(contratoMongo);

    log.debug("Sincronizando títulos do contrato {} para empresa {}", numeroContrato, empresa);
    arencoSincronizacaoTitulosService.sincronizarTitulos(
        contratoMongo, tituloAReceberOracleList, tituloRecebidoOracleList);
    arencoSincronizacaoTitulosService.baixarTitulosRecebidos(
        contratoMongo, tituloAReceberOracleList, boletosAProcessar);

    log.debug("🔄 Sincronização concluída do contrato {} para empresa {}", numeroContrato, empresa);
    return boletosAProcessar;
  }

  private AgreementModel pegarAgreementModel(
      final String empresa, final ContratoOracle contratoOracle, final UserModel userModel) {
    final var numeroContrato = contratoOracle.getNumeContrato();

    log.debug("Verificando existência do contrato {} para empresa {}", numeroContrato, empresa);

    final var contratoExistenteMongoOpcional =
        agreementModelRepository.findByNumeContratoAndEmpresaAndUserId(
            numeroContrato, empresa, userModel.getId());

    if (contratoExistenteMongoOpcional.isPresent()) {
      log.debug("Contrato {} para empresa {} já existe no MongoDB", numeroContrato, empresa);
      return contratoExistenteMongoOpcional.get();
    }

    log.info(
        "Contrato {} não encontrado para empresa {}. Criando novo...", numeroContrato, empresa);
    final var novoAgreementModel = new AgreementModel();
    novoAgreementModel.setUserId(userModel.getId());
    novoAgreementModel.setNumeContrato(numeroContrato);
    novoAgreementModel.setEmpresa(empresa);

    agreementModelRepository.save(novoAgreementModel);
    log.info(
        "Contrato {} criado e armazenado com sucesso para empresa {}", numeroContrato, empresa);

    return novoAgreementModel;
  }
}
