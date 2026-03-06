package br.com.arenco.arenco_cronjobs.strategies.impl;

import br.com.arenco.arenco_cronjobs.entities.*;
import br.com.arenco.arenco_cronjobs.enums.Bank;
import br.com.arenco.arenco_cronjobs.exceptions.ArencoSincronizacaoBoletosInterrompida;
import br.com.arenco.arenco_cronjobs.exceptions.FalhaNoUploadDoBoleto;
import br.com.arenco.arenco_cronjobs.services.S3Service;
import br.com.arenco.arenco_cronjobs.strategies.GerarBoletoStrategy;
import br.com.arenco.arenco_cronjobs.utils.BradescoCnab400Utils;
import br.com.caelum.stella.boleto.*;
import br.com.caelum.stella.boleto.bancos.Bradesco;
import br.com.caelum.stella.boleto.transformer.GeradorDeBoleto;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;

@Slf4j
@Service
public abstract class AbstractGerarBoletoStrategyImpl implements GerarBoletoStrategy {
  abstract S3Service getS3Service();

  protected void uploadFromBytes(final String key, final byte[] bytes, final String contentType)
      throws ArencoSincronizacaoBoletosInterrompida {
    if (bytes == null) throw new IllegalArgumentException("bytes não pode ser null");

    long contentLength = bytes.length; // int -> long (widening OK)

    try (final InputStream in = new ByteArrayInputStream(bytes)) {
      getS3Service().upload(key, in, contentLength, contentType);
    } catch (final NoSuchBucketException e) {
      throw new ArencoSincronizacaoBoletosInterrompida("O Bucket não existe");
    } catch (final IOException e) {
      log.error(e.getMessage(), e);
      throw new FalhaNoUploadDoBoleto("Falha no upload", e);
    }
  }

  protected static GeradorDeBoleto getGeradorDeBoleto(
      final PaymentSlipSettingsModel paymentSlipSettingsModel,
      final UserModel customer,
      final AddressModel addressModel,
      final ReceivableTitleModel receivableTitleModel) {
    final var dataBase = receivableTitleModel.getDataBase();
    if (dataBase == null) {
      throw new IllegalStateException(
          "Data base nao pode ser nula para ReceivableTitleModel " + receivableTitleModel.getId());
    }
    final var vencimento = receivableTitleModel.getVencimento();
    if (vencimento == null) {
      throw new IllegalStateException(
          "Vencimento nao pode ser nulo para ReceivableTitleModel " + receivableTitleModel.getId());
    }

    final var numeroDoDoc =
        "00000" + receivableTitleModel.getNumeDoc() + receivableTitleModel.getSequencia();
    final var digitoNossoNumero =
        BradescoCnab400Utils.dvNossoNumero(
            "0" + paymentSlipSettingsModel.getCarteira(), numeroDoDoc);
    log.debug("NumeroDoDoc: {}. DigitoNossoNumero: {}", numeroDoDoc, digitoNossoNumero);

    final Datas datas =
        Datas.novasDatas()
            .comDocumento(dataBase.getDayOfMonth(), dataBase.getMonthValue(), dataBase.getYear())
            .comVencimento(
                vencimento.getDayOfMonth(), vencimento.getMonthValue(), vencimento.getYear());

    final Endereco enderecoBeneficiario =
        Endereco.novoEndereco()
            .comLogradouro(paymentSlipSettingsModel.getLogradouro())
            .comBairro(paymentSlipSettingsModel.getBairro())
            .comCep(paymentSlipSettingsModel.getCep())
            .comCidade(paymentSlipSettingsModel.getCidade())
            .comUf(paymentSlipSettingsModel.getUf());

    // Quem emite o boleto
    final Beneficiario beneficiario =
        Beneficiario.novoBeneficiario()
            .comNomeBeneficiario(paymentSlipSettingsModel.getNomeBeneficiario())
            .comDocumento(paymentSlipSettingsModel.getDocumento())
            .comAgencia(paymentSlipSettingsModel.getAgencia())
            .comNossoNumero(numeroDoDoc)
            .comDigitoNossoNumero(digitoNossoNumero)
            .comCodigoBeneficiario(paymentSlipSettingsModel.getCodigoBeneficiario())
            .comDigitoCodigoBeneficiario(paymentSlipSettingsModel.getDigitoCodigoBeneficiario())
            .comCarteira(paymentSlipSettingsModel.getCarteira())
            .comEndereco(enderecoBeneficiario);

    final Endereco enderecoPagador =
        Endereco.novoEndereco()
            .comLogradouro(addressModel.getStreetName() + "," + addressModel.getStreetNumber())
            .comBairro(addressModel.getDistrict())
            .comCep(addressModel.getCep())
            .comCidade(addressModel.getCity())
            .comUf(addressModel.getState());

    // Quem paga o boleto
    final Pagador pagador =
        Pagador.novoPagador()
            .comNome(customer.getName())
            .comDocumento(customer.getCpf() != null ? customer.getCpf() : customer.getCnpj())
            .comEndereco(enderecoPagador);

    final Banco banco = getBanco(paymentSlipSettingsModel);

    final Boleto boleto =
        Boleto.novoBoleto()
            .comBanco(banco)
            .comDatas(datas)
            .comPagador(pagador)
            .comBeneficiario(beneficiario)
            .comValorBoleto(receivableTitleModel.getValor())
            .comNumeroDoDocumento(numeroDoDoc)
            .comInstrucoes(paymentSlipSettingsModel.getInstrucoes())
            .comLocaisDePagamento(paymentSlipSettingsModel.getLocalDePagamento())
            .comEspecieDocumento(paymentSlipSettingsModel.getEspecieDocumento());

    final InputStream jasper =
        AbstractGerarBoletoStrategyImpl.class.getResourceAsStream(
            "/META-INF/jasper/arenco-boleto.jasper");
    final InputStream logoArenco =
        Objects.requireNonNull(
            AbstractGerarBoletoStrategyImpl.class.getResourceAsStream(
                "/META-INF/jasper/logo-arenco.png"));
    return new GeradorDeBoleto(jasper, Map.of("logo_arenco", logoArenco), boleto);
  }

  private static Banco getBanco(final PaymentSlipSettingsModel paymentSlipSettingsModel) {
    if (paymentSlipSettingsModel.getBanco() == Bank.BRADESCO) {
      return new Bradesco();
    }
    throw new NotImplementedException("Other banks are not supported");
  }
}
