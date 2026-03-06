package br.com.arenco.arenco_clientes.mappers;

import br.com.arenco.arenco_clientes.dtos.settings.PaymentSlipSettingsDto;
import br.com.arenco.arenco_clientes.entities.PaymentSlipSettingsModel;

public enum PaymentSlipSettingsMapper {
  ;

  public static void updateModelFromDto(
      final PaymentSlipSettingsDto dto, final PaymentSlipSettingsModel model) {
    if (dto == null || model == null) {
      return;
    }

    if (dto.getLocalDePagamento() != null) model.setLocalDePagamento(dto.getLocalDePagamento());
    if (dto.getNomeBeneficiario() != null) model.setNomeBeneficiario(dto.getNomeBeneficiario());
    if (dto.getInstrucoes() != null) model.setInstrucoes(dto.getInstrucoes());
    if (dto.getBanco() != null) model.setBanco(dto.getBanco());
    if (dto.getDocumento() != null) model.setDocumento(dto.getDocumento());
    if (dto.getAgencia() != null) model.setAgencia(dto.getAgencia());
    if (dto.getCodigoBeneficiario() != null)
      model.setCodigoBeneficiario(dto.getCodigoBeneficiario());
    if (dto.getDigitoCodigoBeneficiario() != null)
      model.setDigitoCodigoBeneficiario(dto.getDigitoCodigoBeneficiario());
    if (dto.getCarteira() != null) model.setCarteira(dto.getCarteira());
    if (dto.getLogradouro() != null) model.setLogradouro(dto.getLogradouro());
    if (dto.getBairro() != null) model.setBairro(dto.getBairro());
    if (dto.getCep() != null) model.setCep(dto.getCep());
    if (dto.getCidade() != null) model.setCidade(dto.getCidade());
    if (dto.getUf() != null) model.setUf(dto.getUf());
    if (dto.getEspecieDocumento() != null) model.setEspecieDocumento(dto.getEspecieDocumento());
  }
}
