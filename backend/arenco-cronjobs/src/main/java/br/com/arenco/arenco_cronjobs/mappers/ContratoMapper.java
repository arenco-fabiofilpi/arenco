package br.com.arenco.arenco_cronjobs.mappers;

import br.com.arenco.arenco_cronjobs.oracle.entities.ContratoOracle;
import br.com.arenco.arenco_cronjobs.oracle.entities.TituloAReceberOracle;
import br.com.arenco.arenco_cronjobs.oracle.entities.TituloRecebidoOracle;
import br.com.arenco.arenco_cronjobs.entities.AgreementModel;
import br.com.arenco.arenco_cronjobs.entities.ReceivableTitleModel;
import br.com.arenco.arenco_cronjobs.entities.ReceivedTitleModel;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum ContratoMapper {
  ;
  private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  public static void atualizarContrato(
      final AgreementModel contratoMongo, final ContratoOracle contratoOracle) {
    contratoMongo.setCliente(contratoOracle.getCliente());
    contratoMongo.setNomeEmpresa(contratoOracle.getNomeEmpresa());
    contratoMongo.setNomeCliente(contratoOracle.getNomeCliente());
    contratoMongo.setCcusto(contratoOracle.getCcusto());
    contratoMongo.setNomeCcusto(contratoOracle.getNomeCcusto());
    contratoMongo.setEnderecoCcusto(contratoOracle.getEnderecoCcusto());
    contratoMongo.setUnidadeDeCusto(contratoOracle.getUnidadeDeCusto());
    contratoMongo.setReceita(contratoOracle.getReceita());
    contratoMongo.setDenominacao(contratoOracle.getDenominacao());
    contratoMongo.setDtEmissao(convertToLocalDate(contratoOracle.getDtEmissao()));
    contratoMongo.setDataBase(convertToLocalDate(contratoOracle.getDataBase()));
    contratoMongo.setSequenciaInicial(contratoOracle.getSequenciaInicial());
    contratoMongo.setValorContrato(convertToDouble(contratoOracle.getValorContrato()));
    contratoMongo.setValorContratoExtenso(contratoOracle.getValorContratoExtenso());
    contratoMongo.setNumeContratoAnterior(contratoOracle.getNumeContratoAnterior());
    contratoMongo.setTemLanctoContab(contratoOracle.getTemLanctoContab());
    contratoMongo.setTipoDoc(contratoOracle.getTipoDoc());
    contratoMongo.setDescricao(contratoOracle.getDescricao());
    contratoMongo.setObservacaoContrato(contratoOracle.getObservacaoContrato());
    contratoMongo.setNumeRenegociacao(contratoOracle.getNumeRenegociacao());
    contratoMongo.setDataRenegociacao(contratoOracle.getDataRenegociacao());
    contratoMongo.setObservacaoRenegociacao(contratoOracle.getObservacaoRenegociacao());
    contratoMongo.setDiferencaRenegociacao(contratoOracle.getDiferencaRenegociacao());
    contratoMongo.setDataRescisao(convertToLocalDate(contratoOracle.getDataRescisao()));
    contratoMongo.setObservacaoRescisao(contratoOracle.getObservacaoRescisao());
    contratoMongo.setJuros1(contratoOracle.getJuros1());
    contratoMongo.setDataJuros1(contratoOracle.getDataJuros1());
    contratoMongo.setJuros2(contratoOracle.getJuros2());
    contratoMongo.setDataJuros2(contratoOracle.getDataJuros2());
    contratoMongo.setJuros3(contratoOracle.getJuros3());
    contratoMongo.setDataJuros3(contratoOracle.getDataJuros3());
    contratoMongo.setCic(contratoOracle.getCic());
    contratoMongo.setCgcCliente(contratoOracle.getCgcCliente());
    contratoMongo.setInscricaoEstadualCliente(contratoOracle.getInscricaoEstadualCliente());
    contratoMongo.setEnderecoCompleto(contratoOracle.getEnderecoCompleto());
    contratoMongo.setCidade(contratoOracle.getCidade());
    contratoMongo.setEstado(contratoOracle.getEstado());
    contratoMongo.setCepCompleto(contratoOracle.getCepCompleto());
    contratoMongo.setVendedor(contratoOracle.getVendedor());
    contratoMongo.setNomeVendedor(contratoOracle.getNomeVendedor());
    contratoMongo.setTipoCcusto(contratoOracle.getTipoCcusto());
    contratoMongo.setQuadra(contratoOracle.getQuadra());
    contratoMongo.setLote(contratoOracle.getLote());
    contratoMongo.setIndicacaoSituacaoJuridica(contratoOracle.getIndicacaoSituacaoJuridica());
    contratoMongo.setParcelasLiberadaApos(contratoOracle.getParcelasLiberadaApos());
    contratoMongo.setCidadeEmpresa(contratoOracle.getCidadeEmpresa());
    contratoMongo.setCnpjEmpresa(contratoOracle.getCnpjEmpresa());
    contratoMongo.setEnderecoCompletoEmpresa(contratoOracle.getEnderecoCompletoEmpresa());
    contratoMongo.setBairro(contratoOracle.getBairro());
    contratoMongo.setMetragem(contratoOracle.getMetragem());
    contratoMongo.setTipoCorrecaoContrato(contratoOracle.getTipoCorrecaoContrato());
    contratoMongo.setCpfConjugue(contratoOracle.getCpfConjugue());
    contratoMongo.setCodigoNomeCliente(contratoOracle.getCodigoNomeCliente());
    contratoMongo.setEnderecoCompletoCliente(contratoOracle.getEnderecoCompletoCliente());
    contratoMongo.setTelefones(contratoOracle.getTelefones());
    contratoMongo.setUnidadeQuadraLote(contratoOracle.getUnidadeQuadraLote());
    contratoMongo.setTipoUnidade(contratoOracle.getTipoUnidade());
    contratoMongo.setConjugueNome(contratoOracle.getConjugueNome());
    contratoMongo.setUsuarioInclusao(contratoOracle.getUsuarioInclusao());
    contratoMongo.setDataInclusao(convertToLocalDate(contratoOracle.getDataInclusao()));
    contratoMongo.setPercentualUaCsn(contratoOracle.getPercentualUaCsn());
    contratoMongo.setNumeroContratoInterno(contratoOracle.getNumeroContratoInterno());
    contratoMongo.setClienteNascimento(convertToLocalDate(contratoOracle.getClienteNascimento()));
    contratoMongo.setRgCliente(contratoOracle.getRgCliente());
    contratoMongo.setPercentualSeguro(contratoOracle.getPercentualSeguro());
    contratoMongo.setSeguroEmbutidoParcela(contratoOracle.getSeguroEmbutidoParcela());
    contratoMongo.setTaxaJurosValorPresente(contratoOracle.getTaxaJurosValorPresente());
    contratoMongo.setNumeroContratoMatriz(contratoOracle.getNumeroContratoMatriz());
    contratoMongo.setNumeContratoOriginal(contratoOracle.getNumeContratoOriginal());
    contratoMongo.setUnidadeDeCustoOriginal(contratoOracle.getUnidadeDeCustoOriginal());
    contratoMongo.setQuadraOriginal(contratoOracle.getQuadraOriginal());
    contratoMongo.setLoteOriginal(contratoOracle.getLoteOriginal());
    contratoMongo.setContratoAnteriorCessao(contratoOracle.getContratoAnteriorCessao());
    contratoMongo.setDataEntregaUnidade(contratoOracle.getDataEntregaUnidade());
    contratoMongo.setNomeCorretor(contratoOracle.getNomeCorretor());
    contratoMongo.setTotalSaldoEmAberto(contratoOracle.getTotalSaldoEmAberto());
    contratoMongo.setCodJurosCobrancaIndividual(contratoOracle.getCodJurosCobrancaIndividual());
    contratoMongo.setDescJurosCobrancaIndividual(contratoOracle.getDescJurosCobrancaIndividual());
    contratoMongo.setPercMultaCobrancaIndividual(contratoOracle.getPercMultaCobrancaIndividual());
    contratoMongo.setSemCarenciaArredJurosMulta(contratoOracle.getSemCarenciaArredJurosMulta());
    contratoMongo.setNomeUltimoComprador(contratoOracle.getNomeUltimoComprador());
    contratoMongo.setObservacaoCadastroCliente(contratoOracle.getObservacaoCadastroCliente());
  }

  public static void atualizarTituloAReceber(
      final ReceivableTitleModel tituloAReceberMongo,
      final TituloAReceberOracle tituloAReceberOracle) {
    tituloAReceberMongo.setEmpresa(tituloAReceberOracle.getEmpresa());
    tituloAReceberMongo.setCliente(Integer.parseInt(tituloAReceberOracle.getCliente()));
    tituloAReceberMongo.setNumeDoc(tituloAReceberOracle.getNumeDoc());
    tituloAReceberMongo.setSequencia(tituloAReceberOracle.getSequencia());
    tituloAReceberMongo.setDtEmissao(convertToLocalDate(tituloAReceberOracle.getDtEmissao()));
    tituloAReceberMongo.setDataBase(convertToLocalDate(tituloAReceberOracle.getDataBase()));
    tituloAReceberMongo.setFatura(tituloAReceberOracle.getFatura());
    tituloAReceberMongo.setNomeClte(tituloAReceberOracle.getNomeClte());
    tituloAReceberMongo.setNumeFatura(tituloAReceberOracle.getNumeFatura());
    tituloAReceberMongo.setObservacao(tituloAReceberOracle.getObservacao());
    tituloAReceberMongo.setSaldo(convertToDouble(tituloAReceberOracle.getSaldo()));
    tituloAReceberMongo.setSerie(tituloAReceberOracle.getSerie());
    tituloAReceberMongo.setUnidadeDeCusto(tituloAReceberOracle.getUnidadeDeCusto());
    tituloAReceberMongo.setTipoDoc(tituloAReceberOracle.getTipoDoc());
    tituloAReceberMongo.setValor(convertToDouble(tituloAReceberOracle.getValor()));
    tituloAReceberMongo.setVencimento(convertToLocalDate(tituloAReceberOracle.getVencimento()));
    tituloAReceberMongo.setDescricao(tituloAReceberOracle.getDescricao());
    tituloAReceberMongo.setNomeCcusto(tituloAReceberOracle.getNomeCcusto());
    tituloAReceberMongo.setCcusto(tituloAReceberOracle.getCcusto());
    tituloAReceberMongo.setReceita(tituloAReceberOracle.getReceita());
    tituloAReceberMongo.setDenominacao(tituloAReceberOracle.getDenominacao());
    tituloAReceberMongo.setDataDeHoje(convertToLocalDate(tituloAReceberOracle.getDataDeHoje()));
    tituloAReceberMongo.setMoeda(tituloAReceberOracle.getMoeda());
    tituloAReceberMongo.setDescricaoMoeda(tituloAReceberOracle.getDescricaoMoeda());
    tituloAReceberMongo.setQtdeMoeda(convertToDouble(tituloAReceberOracle.getQtdeMoeda()));
    tituloAReceberMongo.setSaldoQtdeMoeda(
        convertToDouble(tituloAReceberOracle.getSaldoQtdeMoeda()));
    tituloAReceberMongo.setValorCorrigidoVencimento(
        convertToDouble(tituloAReceberOracle.getValorCorrigidoVencimento()));
    tituloAReceberMongo.setValorCorrigidoHoje(
        convertToDouble(tituloAReceberOracle.getValorCorrigidoHoje()));
    tituloAReceberMongo.setValorCorrigidoDataRef(
        convertToDouble(tituloAReceberOracle.getValorCorrigidoDataRef()));
    tituloAReceberMongo.setCodigoPortador(tituloAReceberOracle.getCodigoPortador());
    tituloAReceberMongo.setTemCobrancaBoleto(tituloAReceberOracle.getTemCobrancaBoleto());
    tituloAReceberMongo.setQuadra(tituloAReceberOracle.getQuadra());
    tituloAReceberMongo.setLote(tituloAReceberOracle.getLote());
    tituloAReceberMongo.setIndicacaoSituacaoJuridica(
        tituloAReceberOracle.getIndicacaoSituacaoJuridica());
    tituloAReceberMongo.setParcelasLiberadaApos(tituloAReceberOracle.getParcelasLiberadaApos());
    tituloAReceberMongo.setTipoCorrecaoContrato(tituloAReceberOracle.getTipoCorrecaoContrato());
    tituloAReceberMongo.setTaxaJurosValorPresente(tituloAReceberOracle.getTaxaJurosValorPresente());
    tituloAReceberMongo.setSaldoValorPresente(
        convertToDouble(tituloAReceberOracle.getSaldoValorPresente()));
    tituloAReceberMongo.setSeguroEmbutidoParcela(tituloAReceberOracle.getSeguroEmbutidoParcela());
    tituloAReceberMongo.setPercentualSeguro(tituloAReceberOracle.getPercentualSeguro());
    tituloAReceberMongo.setCodigoCobranca(tituloAReceberOracle.getCodigoCobranca());
    tituloAReceberMongo.setCnpjCpfDoCliente(tituloAReceberOracle.getCnpjCpfDoCliente());
    tituloAReceberMongo.setSequenciaReparcela(tituloAReceberOracle.getSequenciaReparcela());
    tituloAReceberMongo.setDataReparcela(tituloAReceberOracle.getDataReparcela());
    tituloAReceberMongo.setTotalReparcela(tituloAReceberOracle.getTotalReparcela());
  }

  public static void atualizarTituloRecebido(
      final ReceivedTitleModel tituloRecebidoMongo,
      final TituloRecebidoOracle tituloRecebidoOracle) {
    tituloRecebidoMongo.setEmpresa(tituloRecebidoOracle.getEmpresa());
    tituloRecebidoMongo.setCliente(tituloRecebidoOracle.getCliente());
    tituloRecebidoMongo.setNumeDoc(tituloRecebidoOracle.getNumeDoc());
    tituloRecebidoMongo.setTipoDoc(tituloRecebidoOracle.getTipoDoc());
    tituloRecebidoMongo.setDescricao(tituloRecebidoOracle.getDescricao());
    tituloRecebidoMongo.setDataBase(convertToLocalDate(tituloRecebidoOracle.getDataBase()));
    tituloRecebidoMongo.setDtEmissao(convertToLocalDate(tituloRecebidoOracle.getDtEmissao()));
    tituloRecebidoMongo.setDenominacao(tituloRecebidoOracle.getDenominacao());
    tituloRecebidoMongo.setNomeCcusto(tituloRecebidoOracle.getNomeCcusto());
    tituloRecebidoMongo.setCcusto(tituloRecebidoOracle.getCcusto());
    tituloRecebidoMongo.setReceita(tituloRecebidoOracle.getReceita());
    tituloRecebidoMongo.setQuadra(tituloRecebidoOracle.getQuadra());
    tituloRecebidoMongo.setLote(tituloRecebidoOracle.getLoteLoteamento());
    tituloRecebidoMongo.setUnidadeDeCusto(tituloRecebidoOracle.getUnidadeDeCusto());
    tituloRecebidoMongo.setObservacaoContrato(tituloRecebidoOracle.getObservacaoContrato());
    tituloRecebidoMongo.setSerie(tituloRecebidoOracle.getSerie());
    tituloRecebidoMongo.setDtDeposito(convertToLocalDate(tituloRecebidoOracle.getDtDeposito()));
    tituloRecebidoMongo.setVencimento(convertToLocalDate(tituloRecebidoOracle.getVencimento()));
    tituloRecebidoMongo.setMoeda(tituloRecebidoOracle.getMoeda());
    tituloRecebidoMongo.setValor(convertToDouble(tituloRecebidoOracle.getValor()));
    tituloRecebidoMongo.setValorJuros(convertToDouble(tituloRecebidoOracle.getValorJuros()));
    tituloRecebidoMongo.setValorDesc(convertToDouble(tituloRecebidoOracle.getValorDesc()));
    tituloRecebidoMongo.setValorPago(convertToDouble(tituloRecebidoOracle.getValorPago()));
    tituloRecebidoMongo.setValorVm(convertToDouble(tituloRecebidoOracle.getValorVm()));
    tituloRecebidoMongo.setValorMulta(convertToDouble(tituloRecebidoOracle.getValorMulta()));
    tituloRecebidoMongo.setObservacao(tituloRecebidoOracle.getObservacao());
    tituloRecebidoMongo.setNomeClte(tituloRecebidoOracle.getNomeClte());
    tituloRecebidoMongo.setFatura(tituloRecebidoOracle.getFatura());
    tituloRecebidoMongo.setNumeFatura(tituloRecebidoOracle.getNumeFatura());
    tituloRecebidoMongo.setNumeDeposito(tituloRecebidoOracle.getNumeDeposito());
    tituloRecebidoMongo.setBanco(tituloRecebidoOracle.getBanco());
    tituloRecebidoMongo.setValorDeposito(convertToDouble(tituloRecebidoOracle.getValorDeposito()));
    tituloRecebidoMongo.setCodigoBanco(tituloRecebidoOracle.getCodigoBanco());
    tituloRecebidoMongo.setAgencia(tituloRecebidoOracle.getAgencia());
    tituloRecebidoMongo.setConta(tituloRecebidoOracle.getConta());
    tituloRecebidoMongo.setNomeBanco(tituloRecebidoOracle.getNomeBanco());
    tituloRecebidoMongo.setTipoBaixa(tituloRecebidoOracle.getTipoBaixa());
    tituloRecebidoMongo.setDescricaoTipoBaixa(tituloRecebidoOracle.getDescricaoTipoBaixa());
    tituloRecebidoMongo.setDiasAtraso(tituloRecebidoOracle.getDiasAtraso());
    tituloRecebidoMongo.setQuadraOriginal(tituloRecebidoOracle.getQuadraOriginal());
    tituloRecebidoMongo.setLoteOriginal(tituloRecebidoOracle.getLoteOriginal());
    tituloRecebidoMongo.setUnidadeDeCustoOriginal(tituloRecebidoOracle.getUnidadeDeCustoOriginal());
    tituloRecebidoMongo.setValorSeguro(convertToDouble(tituloRecebidoOracle.getValorSeguro()));
    tituloRecebidoMongo.setTipoDeposito(tituloRecebidoOracle.getTipoDeposito());
    tituloRecebidoMongo.setDescricaoTipoDeposito(tituloRecebidoOracle.getDescricaoTipoDeposito());
    tituloRecebidoMongo.setSequenciaReparcela(tituloRecebidoOracle.getSequenciaReparcela());
    tituloRecebidoMongo.setTotalReparcela(
        convertToDouble(tituloRecebidoOracle.getTotalReparcela()));
    tituloRecebidoMongo.setDataReparcela(tituloRecebidoOracle.getDataReparcela());
    tituloRecebidoMongo.setObservacaoContrato(tituloRecebidoOracle.getObservacaoContrato());
  }

  private static LocalDate convertToLocalDate(final String dateAsString) {
    if (dateAsString == null || dateAsString.isEmpty()) {
      return null;
    }
    return LocalDateTime.parse(dateAsString, FMT).toLocalDate();
  }

  private static Double convertToDouble(final String valueAsString) {
    if (valueAsString == null || valueAsString.isEmpty()) {
      return null;
    }
    return Double.parseDouble(valueAsString);
  }
}
