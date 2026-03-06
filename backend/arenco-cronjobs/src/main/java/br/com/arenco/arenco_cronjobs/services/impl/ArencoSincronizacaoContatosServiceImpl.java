package br.com.arenco.arenco_cronjobs.services.impl;

import br.com.arenco.arenco_cronjobs.oracle.entities.ClienteOracle;
import br.com.arenco.arenco_cronjobs.services.ArencoSincronizacaoContatosService;
import br.com.arenco.arenco_cronjobs.entities.ContactModel;
import br.com.arenco.arenco_cronjobs.entities.UserModel;
import br.com.arenco.arenco_cronjobs.enums.ContactType;
import br.com.arenco.arenco_cronjobs.repositories.ContactModelRepository;
import io.micrometer.common.util.StringUtils;
import java.util.EnumMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArencoSincronizacaoContatosServiceImpl implements ArencoSincronizacaoContatosService {

  private final ContactModelRepository contactModelRepository;

  @Value("${arenco-cronjobs.sincronizacao-clientes.sincronizar-contatos:#{true}}")
  private boolean deveSincronizarContatos;

  @Override
  public void sincronizarListaDeContatos(final UserModel user, final ClienteOracle clienteOracle) {
    if (!deveSincronizarContatos) {
      log.info("Sincronização de contatos desativada.");
      return;
    }

    final Map<ContactType, String> contatosASincronizar =
        pegarContatosDoClienteOracle(clienteOracle);
    if (contatosASincronizar.isEmpty()) {
      log.info("Nenhum contato encontrado para o cliente: {}", user.getIdErp());
      return;
    }

    log.debug("Sincronizando contatos do cliente: {}", user.getIdErp());
    contatosASincronizar.forEach(
        (final ContactType contactMethodEnum, final String contactValue) ->
            processarContato(user, contactMethodEnum, contactValue));

    log.debug("Sincronização de contatos concluída para o cliente: {}", user.getIdErp());
  }

  private void processarContato(
      final UserModel user, final ContactType contactMethodEnum, final String contactValue) {
    final boolean contatoExistente =
        contactModelRepository.existsByValueAndUserModelIdAndContactType(
            contactValue, user.getId(), contactMethodEnum);

    if (contatoExistente) {
      log.debug(
          "Contato já existe para o cliente: {}. Método: {}, Valor: {}",
          user.getIdErp(),
          contactMethodEnum,
          contactValue);
      return;
    }

    final ContactModel novoContato = new ContactModel();
    novoContato.setValue(contactValue);
    novoContato.setUserModelId(user.getId());
    novoContato.setContactType(contactMethodEnum);
    contactModelRepository.save(novoContato);

    log.debug(
        "Contato sincronizado para o cliente: {}. Método: {}, Valor: {}",
        user.getIdErp(),
        contactMethodEnum,
        contactValue);
  }

  private Map<ContactType, String> pegarContatosDoClienteOracle(final ClienteOracle clienteOracle) {
    final Map<ContactType, String> contatosDoCliente = new EnumMap<>(ContactType.class);

    adicionarContato(contatosDoCliente, ContactType.EMAIL, clienteOracle.getEmailInternet());
    adicionarTelefone(contatosDoCliente, clienteOracle.getDdd(), clienteOracle.getFone1());
    adicionarTelefone(contatosDoCliente, clienteOracle.getDdd(), clienteOracle.getFone2());
    adicionarTelefone(contatosDoCliente, clienteOracle.getDdd(), clienteOracle.getFonefax());

    return contatosDoCliente;
  }

  private void adicionarContato(
      final Map<ContactType, String> contatos, final ContactType tipo, final String valor) {
    if (StringUtils.isNotEmpty(valor)) {
      contatos.put(tipo, valor);
    }
  }

  private void adicionarTelefone(
      final Map<ContactType, String> contatos, final String ddd, final String telefone) {
    if (StringUtils.isNotEmpty(ddd) && StringUtils.isNotEmpty(telefone)) {
      final String telefoneFormatado = "+55" + ddd + telefone;
      final ContactType tipoContato =
          telefoneFormatado.length() > 13 ? ContactType.CELLPHONE : ContactType.TELEPHONE;
      adicionarContato(contatos, tipoContato, telefoneFormatado);
    }
  }
}
