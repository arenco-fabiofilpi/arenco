package br.com.arenco.arenco_cronjobs.mappers;

import br.com.arenco.arenco_cronjobs.entities.CadClientesOutrosModel;
import br.com.arenco.arenco_cronjobs.oracle.entities.CadClientesOutros;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum CadClientesOutrosMapper {
  ;

  public static void sincronizar(
      final CadClientesOutros source, final CadClientesOutrosModel target) {
    if (source == null || target == null) {
      log.error("Source and target objects cannot be null.");
      return;
    }

    target.setCliente(source.getCliente());
    target.setClienteNascimento(source.getClienteNascimento());
    target.setClienteProfissao(source.getClienteProfissao());
    target.setConjugeNome(source.getConjugeNome());
    target.setConjugeNascimento(source.getConjugeNascimento());
    target.setConjugeCicNume(source.getConjugeCicNume());
    target.setConjugeCicDac(source.getConjugeCicDac());
    target.setConjugeRgNume(source.getConjugeRgNume());
    target.setConjugeRgEmissao(source.getConjugeRgEmissao());
    target.setConjugeRgUf(source.getConjugeRgUf());
    target.setConjugeProfissao(source.getConjugeProfissao());
    target.setRegimeCasamento(source.getRegimeCasamento());
    target.setNacionalidadeCliente(source.getNacionalidadeCliente());
    target.setCodigoRegimeCasamento(source.getCodigoRegimeCasamento());
    target.setCodigoEstadoCivil(source.getCodigoEstadoCivil());
    target.setRgCliente(source.getRgCliente());
    target.setClienteSexo(source.getClienteSexo());
    target.setClienteNomeMae(source.getClienteNomeMae());
    target.setClienteNomePai(source.getClienteNomePai());
    target.setClienteNaturalidade(source.getClienteNaturalidade());
  }
}
