package br.com.arenco.arenco_clientes.dtos;

import br.com.arenco.arenco_clientes.entities.*;
import java.util.List;

/**
 * DTO para exportação de clientes na planilha de importação. Todos os campos são String para manter
 * a formatação original (ex: CPF com pontos).
 */
public record ClienteExportDTO(UserModel userModel, List<CadTipoClienteModel> cadTipoClienteList, List<CadClientesOutrosModel> cadClientesOutrosModelList,
                               List<CadClientesSocioModel> cadClientesSocioModelList, List<CadClientesRefBancariasModel> cadClientesRefBancariasModelList,
                               List<CadClientesRefComerciaisModel> cadClientesRefComerciaisModelList) {
  }
