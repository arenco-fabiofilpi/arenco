package br.com.arenco.arenco_cronjobs.services.impl;

import br.com.arenco.arenco_cronjobs.oracle.entities.ClienteOracle;
import br.com.arenco.arenco_cronjobs.services.ArencoSincronizacaoEnderecosService;
import br.com.arenco.arenco_cronjobs.entities.AddressModel;
import br.com.arenco.arenco_cronjobs.entities.UserModel;
import br.com.arenco.arenco_cronjobs.repositories.AddressModelRepository;
import io.micrometer.common.util.StringUtils;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArencoSincronizacaoEnderecosServiceImpl
    implements ArencoSincronizacaoEnderecosService {

  private final AddressModelRepository addressModelRepository;

  @Value("${arenco-cronjobs.sincronizacao-clientes.sincronizar-enderecos:#{true}}")
  private boolean deveSincronizarEnderecos;

  @Override
  public void sincronizarListaDeEnderecos(
          final UserModel user, final ClienteOracle clienteOracle) {
    if (!deveSincronizarEnderecos) {
      log.info("Sincronização de endereços desativada.");
      return;
    }

    log.debug("Sincronizando endereços do cliente: {}", user.getIdErp());
    pegarEnderecoDoClienteOracle(clienteOracle)
        .ifPresentOrElse(
            enderecoRecord -> sincronizarEndereco(user, enderecoRecord),
            () ->
                log.info(
                    "Nenhum endereço encontrado para o cliente: {}", clienteOracle.getCodigo()));
  }

  private Optional<EnderecoRecord> pegarEnderecoDoClienteOracle(final ClienteOracle clienteOracle) {
    if (StringUtils.isNotEmpty(clienteOracle.getCep())
        && StringUtils.isNotEmpty(clienteOracle.getEndereco())
        && StringUtils.isNotEmpty(clienteOracle.getNumero())) {
      return Optional.of(
          new EnderecoRecord(
              clienteOracle.getEndereco(),
              clienteOracle.getNumero(),
              clienteOracle.getBairro(),
              clienteOracle.getCidade(),
              clienteOracle.getCep(),
              clienteOracle.getEstado(),
              clienteOracle.getPais()));
    }
    return Optional.empty();
  }

  private void sincronizarEndereco(final UserModel user, final EnderecoRecord enderecoRecord) {
    final var jaExistente =
        addressModelRepository.existsByStreetNameAndStreetNumberAndCepAndUserId(
            enderecoRecord.endereco,
            enderecoRecord.numero,
            enderecoRecord.cep,
                user.getId());
    if (!jaExistente) {
      final var novoEndereco = criarNovoEndereco(user, enderecoRecord);
      log.info(
          "Novo endereço criado para o cliente: {}. Endereço: {}", user.getIdErp(), novoEndereco);
    }
    log.debug("Endereço sincronizado para o cliente: {}", user.getIdErp());
  }

  private AddressModel criarNovoEndereco(
      final UserModel user, final EnderecoRecord enderecoRecord) {
    log.info("Criando endereço para o cliente: {}", user.getIdErp());

    final AddressModel novoEndereco = new AddressModel();
    novoEndereco.setStreetName(enderecoRecord.endereco);
    novoEndereco.setStreetNumber(enderecoRecord.numero);
    novoEndereco.setDistrict(enderecoRecord.bairro);
    novoEndereco.setCity(enderecoRecord.cidade);
    novoEndereco.setCep(enderecoRecord.cep);
    novoEndereco.setState(enderecoRecord.estado);
    novoEndereco.setCountry(enderecoRecord.pais);
    novoEndereco.setUserId(user.getId());

    return addressModelRepository.save(novoEndereco);
  }

  private record EnderecoRecord(
      String endereco,
      String numero,
      String bairro,
      String cidade,
      String cep,
      String estado,
      String pais) {}
}
