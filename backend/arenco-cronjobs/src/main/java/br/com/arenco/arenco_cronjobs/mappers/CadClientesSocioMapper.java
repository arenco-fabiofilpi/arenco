package br.com.arenco.arenco_cronjobs.mappers;

import br.com.arenco.arenco_cronjobs.entities.CadClientesSocioModel;
import br.com.arenco.arenco_cronjobs.oracle.entities.CadClientesSocio;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum CadClientesSocioMapper {
  ;

  public static void sincronizar(
      final CadClientesSocio source, final CadClientesSocioModel target) {
    if (source == null || target == null) {
      log.error("Source and target objects cannot be null.");
      return;
    }

    target.setCliente(source.getCliente());
    target.setNome(source.getNome());
    target.setDdd(source.getDdd() != null ? source.getDdd().toString() : null);
    target.setFone1(source.getFone1() != null ? source.getFone1().toString() : null);
    target.setFone2(source.getFone2() != null ? source.getFone2().toString() : null);
    target.setCicNume(source.getCicNume() != null ? source.getCicNume().toString() : null);
    target.setCicDac(source.getCicDac() != null ? source.getCicDac().toString() : null);
    target.setRgNume(source.getRgNume() != null ? source.getRgNume().toString() : null);
    target.setRgEmissao(source.getRgEmissao());
    target.setRgUf(source.getRgUf());
    target.setDataNascimento(source.getDataNascimento());
    target.setProfissao(source.getProfissao());
    target.setCargoFuncao(source.getCargoFuncao());
    target.setParticipacao(
        source.getParticipacao() != null ? source.getParticipacao().doubleValue() : null);
    target.setEstado(source.getEstado());
    target.setAssina(source.getAssina() != null ? source.getAssina().toString() : null);
    target.setTipoProcuracao(
        source.getTipoProcuracao() != null ? source.getTipoProcuracao().toString() : null);
    target.setCidade(source.getCidade());
    target.setCep1(source.getCep1() != null ? source.getCep1().toString() : null);
    target.setCep2(source.getCep2() != null ? source.getCep2().toString() : null);
    target.setBairro(source.getBairro());
    target.setNumero(source.getNumero() != null ? source.getNumero().toString() : null);
    target.setEndereco(source.getEndereco());
    target.setNomeFantasia(source.getNomeFantasia());
    target.setCodigoRegimeCasamento(
        source.getCodigoRegimeCasamento() != null
            ? source.getCodigoRegimeCasamento().toString()
            : null);
    target.setCodigoEstadoCivil(
        source.getCodigoEstadoCivil() != null ? source.getCodigoEstadoCivil().toString() : null);
    target.setNacionalidadeSocio(source.getNacionalidadeSocio());
  }
}
