package br.com.arenco.arenco_clientes.services.users;

import br.com.arenco.arenco_clientes.dtos.ClienteExportDTO;

import java.io.IOException;
import java.util.List;

public interface ExportarClienteService {
    byte[] gerarPlanilhaTemplate() throws IOException;

    byte[] gerarPlanilhaComDados(List<ClienteExportDTO> clientes) throws IOException;
}
