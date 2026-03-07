package br.com.arenco.arenco_cronjobs.mappers;

import br.com.arenco.arenco_cronjobs.entities.CadTipoClienteModel;
import br.com.arenco.arenco_cronjobs.oracle.entities.CadTipoCliente;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum CadTipoClienteMapper {
  ;

  public static void sincronizar(final CadTipoCliente source, final CadTipoClienteModel target) {
    if (source == null || target == null) {
      log.error("Source and target objects cannot be null.");
      return;
    }
    target.setCliente(source.getCliente());
    target.setTipo(source.getTipo());
  }
}
