package br.com.arenco.arenco_clientes.dtos;

import br.com.arenco.arenco_clientes.entities.*;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * DTO para exportação de clientes na planilha de importação. Todos os campos são String para manter
 * a formatação original (ex: CPF com pontos).
 */
@Getter
@RequiredArgsConstructor
public class ClienteExportDTO {
  private final UserModel userModel;
  private final CadTipoClienteModel cadTipoCliente;
  private final CadClientesOutrosModel cadClientesOutrosModel;
  private final List<CadClientesSocioModel> cadClientesSocioModelList;
  private final List<CadClientesRefBancariasModel> cadClientesRefBancariasModelList;
  private final List<CadClientesRefComerciaisModel> cadClientesRefComerciaisModelList;
}
