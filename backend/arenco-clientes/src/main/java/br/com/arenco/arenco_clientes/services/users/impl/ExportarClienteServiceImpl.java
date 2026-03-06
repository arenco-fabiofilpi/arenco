package br.com.arenco.arenco_clientes.services.users.impl;

import br.com.arenco.arenco_clientes.dtos.ClienteExportDTO;
import br.com.arenco.arenco_clientes.services.users.ExportarClienteService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


@Service
public class ExportarClienteServiceImpl implements ExportarClienteService {
    // Colunas obrigatórias (amarelo) vs opcionais (cinza)
    private static final String[] HEADERS = {
            "Nome*", "Tipo de pessoa*", "Sexo*", "CPF/CNPJ", "Data nascimento",
            "nacionalidade", "estado civil", "Regime casamento (caso casado)",
            "Nome Cônjuge", "CPF Conjuge", "Data nascimento do cônjuge",
            "Endereço Residencial", "Número do Endereço Residencial",
            "Complemento do Endereço Residencial", "Bairro do endereço residencial",
            "Município do endereço residencial", "CEP do endereço residencial",
            "Telefone celular", "Email"
    };

    private static final double[] COLUMN_WIDTHS = {
            15.0, 15.6, 16.0, 15.7, 17.6, 16.8, 17.6, 28.6, 17.9, 15.4,
            26.4, 22.3, 33.0, 37.6, 29.4, 32.4, 27.7, 18.0, 22.0
    };

    // Índices 0,1,2 são obrigatórios (amarelo), restante opcional (cinza)
    private static final int REQUIRED_COLUMNS_COUNT = 3;

    public byte[] gerarPlanilhaTemplate() throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Clientes");

            // --- Estilos ---
            CellStyle headerObrigatorio = criarEstiloHeader(workbook, "FFFF99"); // amarelo
            CellStyle headerOpcional = criarEstiloHeader(workbook, "C0C0C0");    // cinza
            CellStyle dataStyle = criarEstiloData(workbook);

            // --- Header (Row 0) ---
            Row headerRow = sheet.createRow(0);
            headerRow.setHeightInPoints(15f);

            for (int i = 0; i < HEADERS.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(HEADERS[i]);
                cell.setCellStyle(i < REQUIRED_COLUMNS_COUNT ? headerObrigatorio : headerOpcional);
            }

            // --- Linha de exemplo (Row 1) ---
            Row exemploRow = sheet.createRow(1);
            exemploRow.setHeightInPoints(15f);

            String[] exemploData = {
                    "Cliente Fictício", "F", "M", "766.553.690-53", "", "",
                    "", "", "", "", "", "Rua A", "500", "Bloco A", "Lima",
                    "Florianópolis", "88056590", "996394758", "exemplo@gmail.com"
            };

            for (int i = 0; i < exemploData.length; i++) {
                Cell cell = exemploRow.createCell(i);
                cell.setCellValue(exemploData[i]);
                cell.setCellStyle(dataStyle);
            }

            // --- Larguras das colunas ---
            for (int i = 0; i < COLUMN_WIDTHS.length; i++) {
                sheet.setColumnWidth(i, (int) (COLUMN_WIDTHS[i] * 256));
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    /**
     * Gera planilha preenchida com dados de clientes existentes (para exportação).
     */
    public byte[] gerarPlanilhaComDados(List<ClienteExportDTO> clientes) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Clientes");

            CellStyle headerObrigatorio = criarEstiloHeader(workbook, "FFFF99");
            CellStyle headerOpcional = criarEstiloHeader(workbook, "C0C0C0");
            CellStyle dataStyle = criarEstiloData(workbook);

            // Header
            Row headerRow = sheet.createRow(0);
            headerRow.setHeightInPoints(15f);
            for (int i = 0; i < HEADERS.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(HEADERS[i]);
                cell.setCellStyle(i < REQUIRED_COLUMNS_COUNT ? headerObrigatorio : headerOpcional);
            }

            // Dados
            int rowIdx = 1;
            for (ClienteExportDTO cliente : clientes) {
                Row row = sheet.createRow(rowIdx++);
                row.setHeightInPoints(15f);

                String[] valores = {
                        emptyIfNull(cliente.getNome()),
                        emptyIfNull(cliente.getTipoPessoa()),
                        emptyIfNull(cliente.getSexo()),
                        emptyIfNull(cliente.getCpfCnpj()),
                        emptyIfNull(cliente.getDataNascimento()),
                        emptyIfNull(cliente.getNacionalidade()),
                        emptyIfNull(cliente.getEstadoCivil()),
                        emptyIfNull(cliente.getRegimeCasamento()),
                        emptyIfNull(cliente.getNomeConjuge()),
                        emptyIfNull(cliente.getCpfConjuge()),
                        emptyIfNull(cliente.getDataNascimentoConjuge()),
                        emptyIfNull(cliente.getEnderecoResidencial()),
                        emptyIfNull(cliente.getNumeroEndereco()),
                        emptyIfNull(cliente.getComplementoEndereco()),
                        emptyIfNull(cliente.getBairro()),
                        emptyIfNull(cliente.getMunicipio()),
                        emptyIfNull(cliente.getCep()),
                        emptyIfNull(cliente.getTelefoneCelular()),
                        emptyIfNull(cliente.getEmail())
                };

                for (int i = 0; i < valores.length; i++) {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(valores[i]);
                    cell.setCellStyle(dataStyle);
                }
            }

            for (int i = 0; i < COLUMN_WIDTHS.length; i++) {
                sheet.setColumnWidth(i, (int) (COLUMN_WIDTHS[i] * 256));
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    // --- Estilos privados ---

    private CellStyle criarEstiloHeader(Workbook workbook, String hexColor) {
        CellStyle style = workbook.createCellStyle();

        Font font = workbook.createFont();
        font.setFontName("Calibri");
        font.setFontHeightInPoints((short) 11);
        font.setColor(IndexedColors.BLACK.getIndex());
        style.setFont(font);

        style.setFillForegroundColor(new org.apache.poi.xssf.usermodel.XSSFColor(
                hexToBytes(hexColor), null));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);

        return style;
    }

    private CellStyle criarEstiloData(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();

        Font font = workbook.createFont();
        font.setFontName("Calibri");
        font.setFontHeightInPoints((short) 11);
        font.setColor(IndexedColors.BLACK.getIndex());
        style.setFont(font);

        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);

        return style;
    }

    private byte[] hexToBytes(String hex) {
        return new byte[]{
                (byte) Integer.parseInt(hex.substring(0, 2), 16),
                (byte) Integer.parseInt(hex.substring(2, 4), 16),
                (byte) Integer.parseInt(hex.substring(4, 6), 16)
        };
    }

    private String emptyIfNull(String value) {
        return value == null ? "" : value;
    }
}
