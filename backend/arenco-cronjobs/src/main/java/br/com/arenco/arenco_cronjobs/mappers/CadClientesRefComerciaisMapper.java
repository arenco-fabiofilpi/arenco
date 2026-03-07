package br.com.arenco.arenco_cronjobs.mappers;

import br.com.arenco.arenco_cronjobs.entities.CadClientesRefComerciaisModel;
import br.com.arenco.arenco_cronjobs.oracle.entities.CadClientesRefComerciais;

public enum CadClientesRefComerciaisMapper {
  ;

  public static void sincronizar(
      final CadClientesRefComerciais source, final CadClientesRefComerciaisModel target) {
    target.setCliente(source.getCliente());
    target.setNome(source.getNome());
    target.setEndereco(source.getEndereco());
    target.setNumero(source.getNumero());
    target.setBairro(source.getBairro());
    target.setCidade(source.getCidade());
    target.setCep1(source.getCep1());
    target.setCep2(source.getCep2());
    target.setEstado(source.getEstado());
    target.setDdd(source.getDdd());
    target.setFone1(source.getFone1());
    target.setFone2(source.getFone2());
  }
}
