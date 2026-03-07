package br.com.arenco.arenco_clientes.services.users.impl;

import br.com.arenco.arenco_clientes.dtos.ClienteExportDTO;
import br.com.arenco.arenco_clientes.entities.*;
import br.com.arenco.arenco_clientes.services.users.ExportarClienteService;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
public class ExportarClienteServiceImpl implements ExportarClienteService {

  private static final String[] HEADERS_CAD_CLIENTES = {
    "id",
    "dateCreated",
    "lastUpdated",
    "idErp",
    "grupoContabil",
    "cnpj",
    "cpf",
    "name",
    "username",
    "password",
    "enabled",
    "state",
    "loginMethod",
    "userType",
    "endereco",
    "numero",
    "bairro",
    "cidade",
    "cep1",
    "cep2",
    "estado",
    "pais",
    "ddd",
    "fone1",
    "fone2",
    "fonefax",
    "emailInternet",
    "cgcNumero",
    "cgcFilial",
    "cgcDac",
    "cicNume",
    "cicDac",
    "rgNume",
    "rgEmissao",
    "rgUf",
    "inscricao",
    "inss",
    "contato",
    "wwwInternet",
    "emUso",
    "observacao",
    "categoria",
    "vendedor",
    "enderecoCobranca",
    "numeroCobranca",
    "cidadeCobranca",
    "bairroCobranca",
    "cep1Cobranca",
    "cep2Cobranca",
    "estadoCobranca",
    "dataNascimento",
    "profissao",
    "participacao",
    "assina",
    "tipoProcuracao",
    "parecerVendedor",
    "cadastrante",
    "numeroJcoml",
    "numeroAltJcoml",
    "dataRegistroJcoml",
    "dataAlteracaoJcoml",
    "capitalSocial",
    "dataAlteracao",
    "dataCadastramento",
    "faxCobranca",
    "foneCobranca",
    "caixaPostalCobranca",
    "dddCobranca",
    "matriz",
    "caixaPostal",
    "nomeFantasia",
    "complende",
    "utilizaCepComoPadrao",
    "formulaCredito",
    "enderecoEntrega",
    "numeroEntrega",
    "cidadeEntrega",
    "bairroEntrega",
    "cep1Entrega",
    "cep2Entrega",
    "estadoEntrega",
    "caixaPostalEntrega",
    "dddEntrega",
    "foneEntrega",
    "faxEntrega",
    "complendeEntrega",
    "complendeCobranca",
    "cnaeAtividade",
    "cnaeDacativ",
    "cnaeFiscalAtiv",
    "dataExpedicaoRg",
    "utilizaGeraParcelasReceber",
    "nascimento",
    "fone4",
    "fone3",
    "celular1",
    "rgDacConjuge",
    "emailComercial",
    "rgNumeroConjuge",
    "dacCpfConjuge",
    "nomeConjuge",
    "numeroCpfConjuge",
    "celular2",
    "celular3",
    "consumidorFinal",
    "codigoMunicipio"
  };

  private static final String[] HEADERS_CAD_TIPO_CLTE = {
    "id", "dateCreated", "lastUpdated", "cliente", "tipo"
  };

  private static final String[] HEADERS_CAD_CLIENTES_SOCIOS = {
    "id",
    "dateCreated",
    "lastUpdated",
    "cliente",
    "nome",
    "ddd",
    "fone1",
    "fone2",
    "cicNume",
    "cicDac",
    "rgNume",
    "rgEmissao",
    "rgUf",
    "dataNascimento",
    "profissao",
    "cargoFuncao",
    "participacao",
    "estado",
    "assina",
    "tipoProcuracao",
    "cidade",
    "cep1",
    "cep2",
    "bairro",
    "numero",
    "endereco",
    "nomeFantasia",
    "codigoRegimeCasamento",
    "codigoEstadoCivil",
    "nacionalidadeSocio"
  };

  private static final String[] HEADERS_CAD_CLIENTES_OUTROS = {
    "id",
    "dateCreated",
    "lastUpdated",
    "cliente",
    "clienteNascimento",
    "clienteProfissao",
    "conjugeNome",
    "conjugeNascimento",
    "conjugeCicNume",
    "conjugeCicDac",
    "conjugeRgNume",
    "conjugeRgEmissao",
    "conjugeRgUf",
    "conjugeProfissao",
    "regimeCasamento",
    "nacionalidadeCliente",
    "codigoRegimeCasamento",
    "codigoEstadoCivil",
    "rgCliente",
    "clienteSexo",
    "clienteNomeMae",
    "clienteNomePai",
    "clienteNaturalidade"
  };

  private static final String[] HEADERS_CAD_CLIENTES_REF_BANCARIAS = {
    "id",
    "dateCreated",
    "lastUpdated",
    "cliente",
    "banco",
    "agencia",
    "endereco",
    "numero",
    "bairro",
    "cidade",
    "cep1",
    "cep2",
    "estado",
    "fone1",
    "fone2"
  };

  private static final String[] HEADERS_CAD_CLIENTES_REF_COMERCIAIS = {
    "id",
    "dateCreated",
    "lastUpdated",
    "cliente",
    "nome",
    "endereco",
    "numero",
    "bairro",
    "cidade",
    "cep1",
    "cep2",
    "estado",
    "ddd",
    "fone1",
    "fone2"
  };

  @Override
  public byte[] gerarPlanilhaTemplate() throws IOException {
    try (XSSFWorkbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream()) {

      CellStyle headerStyle = criarEstiloHeader(workbook, "C0C0C0");
      CellStyle dataStyle = criarEstiloData(workbook);

      criarSheet(
          workbook,
          "CAD_CLIENTES",
          HEADERS_CAD_CLIENTES,
          Collections.emptyList(),
          headerStyle,
          dataStyle);
      criarSheet(
          workbook,
          "CAD_TIPO_CLTE",
          HEADERS_CAD_TIPO_CLTE,
          Collections.emptyList(),
          headerStyle,
          dataStyle);
      criarSheet(
          workbook,
          "CAD_CLIENTES_SOCIOS",
          HEADERS_CAD_CLIENTES_SOCIOS,
          Collections.emptyList(),
          headerStyle,
          dataStyle);
      criarSheet(
          workbook,
          "CAD_CLIENTES_OUTROS",
          HEADERS_CAD_CLIENTES_OUTROS,
          Collections.emptyList(),
          headerStyle,
          dataStyle);
      criarSheet(
          workbook,
          "CAD_CLIENTES_REF_BANCARIAS",
          HEADERS_CAD_CLIENTES_REF_BANCARIAS,
          Collections.emptyList(),
          headerStyle,
          dataStyle);
      criarSheet(
          workbook,
          "CAD_CLIENTES_REF_COMERCIAIS",
          HEADERS_CAD_CLIENTES_REF_COMERCIAIS,
          Collections.emptyList(),
          headerStyle,
          dataStyle);

      workbook.write(out);
      return out.toByteArray();
    }
  }

  @Override
  public byte[] gerarPlanilhaComDados(List<ClienteExportDTO> clientes) throws IOException {
    try (XSSFWorkbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream()) {

      CellStyle headerStyle = criarEstiloHeader(workbook, "C0C0C0");
      CellStyle dataStyle = criarEstiloData(workbook);

      List<String[]> rowsCadClientes = new ArrayList<>();
      List<String[]> rowsTipoClte = new ArrayList<>();
      List<String[]> rowsSocios = new ArrayList<>();
      List<String[]> rowsOutros = new ArrayList<>();
      List<String[]> rowsRefBancarias = new ArrayList<>();
      List<String[]> rowsRefComerciais = new ArrayList<>();

      for (ClienteExportDTO dto : clientes) {
        if (dto.userModel() != null) {
          rowsCadClientes.add(userModelToRow(dto.userModel()));
        }
        if (dto.cadTipoClienteList() != null) {
          for (CadTipoClienteModel cadTipoCliente : dto.cadTipoClienteList()) {
            rowsTipoClte.add(cadTipoClienteToRow(cadTipoCliente));
          }
        }
        if (dto.cadClientesOutrosModelList() != null) {
          for (CadClientesOutrosModel s : dto.cadClientesOutrosModelList()) {
            rowsOutros.add(cadClientesOutrosToRow(s));
          }
        }
        if (dto.cadClientesSocioModelList() != null) {
          for (CadClientesSocioModel s : dto.cadClientesSocioModelList()) {
            rowsSocios.add(cadClientesSocioToRow(s));
          }
        }
        if (dto.cadClientesRefBancariasModelList() != null) {
          for (CadClientesRefBancariasModel b : dto.cadClientesRefBancariasModelList()) {
            rowsRefBancarias.add(cadClientesRefBancariasToRow(b));
          }
        }
        if (dto.cadClientesRefComerciaisModelList() != null) {
          for (CadClientesRefComerciaisModel c : dto.cadClientesRefComerciaisModelList()) {
            rowsRefComerciais.add(cadClientesRefComerciaisToRow(c));
          }
        }
      }

      criarSheet(
          workbook, "CAD_CLIENTES", HEADERS_CAD_CLIENTES, rowsCadClientes, headerStyle, dataStyle);
      criarSheet(
          workbook, "CAD_TIPO_CLTE", HEADERS_CAD_TIPO_CLTE, rowsTipoClte, headerStyle, dataStyle);
      criarSheet(
          workbook,
          "CAD_CLIENTES_SOCIOS",
          HEADERS_CAD_CLIENTES_SOCIOS,
          rowsSocios,
          headerStyle,
          dataStyle);
      criarSheet(
          workbook,
          "CAD_CLIENTES_OUTROS",
          HEADERS_CAD_CLIENTES_OUTROS,
          rowsOutros,
          headerStyle,
          dataStyle);
      criarSheet(
          workbook,
          "CAD_CLIENTES_REF_BANCARIAS",
          HEADERS_CAD_CLIENTES_REF_BANCARIAS,
          rowsRefBancarias,
          headerStyle,
          dataStyle);
      criarSheet(
          workbook,
          "CAD_CLIENTES_REF_COMERCIAIS",
          HEADERS_CAD_CLIENTES_REF_COMERCIAIS,
          rowsRefComerciais,
          headerStyle,
          dataStyle);

      workbook.write(out);
      return out.toByteArray();
    }
  }

  private void criarSheet(
      XSSFWorkbook workbook,
      String sheetName,
      String[] headers,
      List<String[]> rows,
      CellStyle headerStyle,
      CellStyle dataStyle) {
    Sheet sheet = workbook.createSheet(sheetName);

    Row headerRow = sheet.createRow(0);
    headerRow.setHeightInPoints(15f);
    for (int i = 0; i < headers.length; i++) {
      Cell cell = headerRow.createCell(i);
      cell.setCellValue(headers[i]);
      cell.setCellStyle(headerStyle);
      sheet.setColumnWidth(i, 20 * 256);
    }

    int rowIdx = 1;
    for (String[] rowData : rows) {
      Row row = sheet.createRow(rowIdx++);
      row.setHeightInPoints(15f);
      for (int i = 0; i < rowData.length; i++) {
        Cell cell = row.createCell(i);
        cell.setCellValue(rowData[i]);
        cell.setCellStyle(dataStyle);
      }
    }
  }

  private String[] userModelToRow(UserModel u) {
    return new String[] {
      toStr(u.getId()),
      toStr(u.getDateCreated()),
      toStr(u.getLastUpdated()),
      toStr(u.getIdErp()),
      toStr(u.getGrupoContabil()),
      toStr(u.getCnpj()),
      toStr(u.getCpf()),
      toStr(u.getName()),
      toStr(u.getUsername()),
      toStr(u.getPassword()),
      toStr(u.isEnabled()),
      toStr(u.getState()),
      toStr(u.getLoginMethod()),
      toStr(u.getUserType()),
      toStr(u.getEndereco()),
      toStr(u.getNumero()),
      toStr(u.getBairro()),
      toStr(u.getCidade()),
      toStr(u.getCep1()),
      toStr(u.getCep2()),
      toStr(u.getEstado()),
      toStr(u.getPais()),
      toStr(u.getDdd()),
      toStr(u.getFone1()),
      toStr(u.getFone2()),
      toStr(u.getFonefax()),
      toStr(u.getEmailInternet()),
      toStr(u.getCgcNumero()),
      toStr(u.getCgcFilial()),
      toStr(u.getCgcDac()),
      toStr(u.getCicNume()),
      toStr(u.getCicDac()),
      toStr(u.getRgNume()),
      toStr(u.getRgEmissao()),
      toStr(u.getRgUf()),
      toStr(u.getInscricao()),
      toStr(u.getInss()),
      toStr(u.getContato()),
      toStr(u.getWwwInternet()),
      toStr(u.getEmUso()),
      toStr(u.getObservacao()),
      toStr(u.getCategoria()),
      toStr(u.getVendedor()),
      toStr(u.getEnderecoCobranca()),
      toStr(u.getNumeroCobranca()),
      toStr(u.getCidadeCobranca()),
      toStr(u.getBairroCobranca()),
      toStr(u.getCep1Cobranca()),
      toStr(u.getCep2Cobranca()),
      toStr(u.getEstadoCobranca()),
      toStr(u.getDataNascimento()),
      toStr(u.getProfissao()),
      toStr(u.getParticipacao()),
      toStr(u.getAssina()),
      toStr(u.getTipoProcuracao()),
      toStr(u.getParecerVendedor()),
      toStr(u.getCadastrante()),
      toStr(u.getNumeroJcoml()),
      toStr(u.getNumeroAltJcoml()),
      toStr(u.getDataRegistroJcoml()),
      toStr(u.getDataAlteracaoJcoml()),
      toStr(u.getCapitalSocial()),
      toStr(u.getDataAlteracao()),
      toStr(u.getDataCadastramento()),
      toStr(u.getFaxCobranca()),
      toStr(u.getFoneCobranca()),
      toStr(u.getCaixaPostalCobranca()),
      toStr(u.getDddCobranca()),
      toStr(u.getMatriz()),
      toStr(u.getCaixaPostal()),
      toStr(u.getNomeFantasia()),
      toStr(u.getComplende()),
      toStr(u.getUtilizaCepComoPadrao()),
      toStr(u.getFormulaCredito()),
      toStr(u.getEnderecoEntrega()),
      toStr(u.getNumeroEntrega()),
      toStr(u.getCidadeEntrega()),
      toStr(u.getBairroEntrega()),
      toStr(u.getCep1Entrega()),
      toStr(u.getCep2Entrega()),
      toStr(u.getEstadoEntrega()),
      toStr(u.getCaixaPostalEntrega()),
      toStr(u.getDddEntrega()),
      toStr(u.getFoneEntrega()),
      toStr(u.getFaxEntrega()),
      toStr(u.getComplendeEntrega()),
      toStr(u.getComplendeCobranca()),
      toStr(u.getCnaeAtividade()),
      toStr(u.getCnaeDacativ()),
      toStr(u.getCnaeFiscalAtiv()),
      toStr(u.getDataExpedicaoRg()),
      toStr(u.getUtilizaGeraParcelasReceber()),
      toStr(u.getNascimento()),
      toStr(u.getFone4()),
      toStr(u.getFone3()),
      toStr(u.getCelular1()),
      toStr(u.getRgDacConjuge()),
      toStr(u.getEmailComercial()),
      toStr(u.getRgNumeroConjuge()),
      toStr(u.getDacCpfConjuge()),
      toStr(u.getNomeConjuge()),
      toStr(u.getNumeroCpfConjuge()),
      toStr(u.getCelular2()),
      toStr(u.getCelular3()),
      toStr(u.getConsumidorFinal()),
      toStr(u.getCodigoMunicipio())
    };
  }

  private String[] cadTipoClienteToRow(CadTipoClienteModel m) {
    return new String[] {
      toStr(m.getId()),
      toStr(m.getDateCreated()),
      toStr(m.getLastUpdated()),
      toStr(m.getCliente()),
      toStr(m.getTipo())
    };
  }

  private String[] cadClientesSocioToRow(CadClientesSocioModel m) {
    return new String[] {
      toStr(m.getId()),
      toStr(m.getDateCreated()),
      toStr(m.getLastUpdated()),
      toStr(m.getCliente()),
      toStr(m.getNome()),
      toStr(m.getDdd()),
      toStr(m.getFone1()),
      toStr(m.getFone2()),
      toStr(m.getCicNume()),
      toStr(m.getCicDac()),
      toStr(m.getRgNume()),
      toStr(m.getRgEmissao()),
      toStr(m.getRgUf()),
      toStr(m.getDataNascimento()),
      toStr(m.getProfissao()),
      toStr(m.getCargoFuncao()),
      toStr(m.getParticipacao()),
      toStr(m.getEstado()),
      toStr(m.getAssina()),
      toStr(m.getTipoProcuracao()),
      toStr(m.getCidade()),
      toStr(m.getCep1()),
      toStr(m.getCep2()),
      toStr(m.getBairro()),
      toStr(m.getNumero()),
      toStr(m.getEndereco()),
      toStr(m.getNomeFantasia()),
      toStr(m.getCodigoRegimeCasamento()),
      toStr(m.getCodigoEstadoCivil()),
      toStr(m.getNacionalidadeSocio())
    };
  }

  private String[] cadClientesOutrosToRow(CadClientesOutrosModel m) {
    return new String[] {
      toStr(m.getId()),
      toStr(m.getDateCreated()),
      toStr(m.getLastUpdated()),
      toStr(m.getCliente()),
      toStr(m.getClienteNascimento()),
      toStr(m.getClienteProfissao()),
      toStr(m.getConjugeNome()),
      toStr(m.getConjugeNascimento()),
      toStr(m.getConjugeCicNume()),
      toStr(m.getConjugeCicDac()),
      toStr(m.getConjugeRgNume()),
      toStr(m.getConjugeRgEmissao()),
      toStr(m.getConjugeRgUf()),
      toStr(m.getConjugeProfissao()),
      toStr(m.getRegimeCasamento()),
      toStr(m.getNacionalidadeCliente()),
      toStr(m.getCodigoRegimeCasamento()),
      toStr(m.getCodigoEstadoCivil()),
      toStr(m.getRgCliente()),
      toStr(m.getClienteSexo()),
      toStr(m.getClienteNomeMae()),
      toStr(m.getClienteNomePai()),
      toStr(m.getClienteNaturalidade())
    };
  }

  private String[] cadClientesRefBancariasToRow(CadClientesRefBancariasModel m) {
    return new String[] {
      toStr(m.getId()),
      toStr(m.getDateCreated()),
      toStr(m.getLastUpdated()),
      toStr(m.getCliente()),
      toStr(m.getBanco()),
      toStr(m.getAgencia()),
      toStr(m.getEndereco()),
      toStr(m.getNumero()),
      toStr(m.getBairro()),
      toStr(m.getCidade()),
      toStr(m.getCep1()),
      toStr(m.getCep2()),
      toStr(m.getEstado()),
      toStr(m.getFone1()),
      toStr(m.getFone2())
    };
  }

  private String[] cadClientesRefComerciaisToRow(CadClientesRefComerciaisModel m) {
    return new String[] {
      toStr(m.getId()),
      toStr(m.getDateCreated()),
      toStr(m.getLastUpdated()),
      toStr(m.getCliente()),
      toStr(m.getNome()),
      toStr(m.getEndereco()),
      toStr(m.getNumero()),
      toStr(m.getBairro()),
      toStr(m.getCidade()),
      toStr(m.getCep1()),
      toStr(m.getCep2()),
      toStr(m.getEstado()),
      toStr(m.getDdd()),
      toStr(m.getFone1()),
      toStr(m.getFone2())
    };
  }

  private CellStyle criarEstiloHeader(Workbook workbook, String hexColor) {
    CellStyle style = workbook.createCellStyle();

    Font font = workbook.createFont();
    font.setFontName("Calibri");
    font.setFontHeightInPoints((short) 11);
    font.setColor(IndexedColors.BLACK.getIndex());
    style.setFont(font);

    style.setFillForegroundColor(
        new org.apache.poi.xssf.usermodel.XSSFColor(hexToBytes(hexColor), null));
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
    return new byte[] {
      (byte) Integer.parseInt(hex.substring(0, 2), 16),
      (byte) Integer.parseInt(hex.substring(2, 4), 16),
      (byte) Integer.parseInt(hex.substring(4, 6), 16)
    };
  }

  private String toStr(Object value) {
    return value == null ? "" : value.toString();
  }
}
