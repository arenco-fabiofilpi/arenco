package br.com.arenco.arenco_cronjobs.mappers;

import br.com.arenco.arenco_cronjobs.entities.CadClientesRefBancariasModel;
import br.com.arenco.arenco_cronjobs.oracle.entities.CadClientesRefBancarias;

public enum CadClientesRefBancariasMapper {
  ;

  public static void sincronizar(
      final CadClientesRefBancarias source, final CadClientesRefBancariasModel target) {
    target.setCliente(source.getCliente());
    target.setBanco(source.getBanco());
    target.setAgencia(source.getAgencia());
    target.setEndereco(source.getEndereco());
    target.setNumero(source.getNumero());
    target.setBairro(source.getBairro());
    target.setCidade(source.getCidade());
    target.setCep1(source.getCep1());
    target.setCep2(source.getCep2());
    target.setEstado(source.getEstado());
    target.setFone1(source.getFone1());
    target.setFone2(source.getFone2());
  }
}
