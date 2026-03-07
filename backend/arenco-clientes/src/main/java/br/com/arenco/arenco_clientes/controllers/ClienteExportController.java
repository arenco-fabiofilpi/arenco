package br.com.arenco.arenco_clientes.controllers;

import br.com.arenco.arenco_clientes.dtos.ClienteExportDTO;
import br.com.arenco.arenco_clientes.facades.CustomerFacade;
import br.com.arenco.arenco_clientes.services.users.ExportarClienteService;
import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClienteExportController {

    private final ExportarClienteService excelService;
    private final CustomerFacade customerFacade;

    public ClienteExportController(final ExportarClienteService excelService,
                                   final CustomerFacade customerFacade) {
        this.excelService = excelService;
        this.customerFacade = customerFacade;
    }

    /**
     * Baixa a planilha template vazia (com header + linha de exemplo).
     * GET /api/clientes/template-importacao
     */
    @GetMapping("/template-importacao")
    public ResponseEntity<byte[]> downloadTemplate() throws IOException {
        final byte[] arquivo = excelService.gerarPlanilhaTemplate();
        return buildExcelResponse(arquivo, "Planilha_de_Importacao_de_Clientes.xlsx");
    }

    /**
     * Exporta clientes existentes no formato da planilha de importação.
     * GET /api/clientes/exportar
     */
    @GetMapping("/exportar")
    public ResponseEntity<byte[]> exportarClientes(@RequestParam final List<String> ids) throws IOException {
        // TODO: substituir por chamada ao seu repository/service real
        final byte[] arquivo = customerFacade.exportar(ids);
        return buildExcelResponse(arquivo, "Clientes_Exportados.xlsx");
    }

    private ResponseEntity<byte[]> buildExcelResponse(final byte[] bytes, final String filename) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .contentLength(bytes.length)
                .body(bytes);
    }
}