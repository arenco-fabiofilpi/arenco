package br.com.arenco.arenco_cronjobs.mappers;

import br.com.arenco.arenco_cronjobs.entities.CadTipoClienteModel;
import br.com.arenco.arenco_cronjobs.oracle.entities.CadTipoCliente;

public enum CadTipoClienteMapper {
  ;

  public static void sincronizar(final CadTipoCliente source, final CadTipoClienteModel target) {
    target.setCliente(source.getCliente());
    target.setTipo(source.getTipo());
  }
}
