package br.com.arenco.arenco_clientes.factories;

import br.com.arenco.arenco_clientes.dtos.settings.PaymentSlipSettingsDto;
import br.com.arenco.arenco_clientes.utils.ArencoDateUtils;
import br.com.arenco.arenco_clientes.entities.PaymentSlipSettingsModel;

public record PaymentSlipSettingsDtoFactory(PaymentSlipSettingsModel model) {
  public PaymentSlipSettingsDto create() {
    final PaymentSlipSettingsDto dto = new PaymentSlipSettingsDto();
    dto.setId(model.getId());
    dto.setVersion(model.getVersion());
    dto.setDateCreated(ArencoDateUtils.fromInstantToString(model.getDateCreated()));
    dto.setLastUpdated(ArencoDateUtils.fromInstantToString(model.getLastUpdated()));
    dto.setLocalDePagamento(model.getLocalDePagamento());
    dto.setNomeBeneficiario(model.getNomeBeneficiario());
    dto.setInstrucoes(model.getInstrucoes());
    dto.setBanco(model.getBanco());
    dto.setDocumento(model.getDocumento());
    dto.setAgencia(model.getAgencia());
    dto.setCodigoBeneficiario(model.getCodigoBeneficiario());
    dto.setDigitoCodigoBeneficiario(model.getDigitoCodigoBeneficiario());
    dto.setCarteira(model.getCarteira());
    dto.setLogradouro(model.getLogradouro());
    dto.setBairro(model.getBairro());
    dto.setCep(model.getCep());
    dto.setCidade(model.getCidade());
    dto.setUf(model.getUf());
    dto.setEspecieDocumento(model.getEspecieDocumento());
    return dto;
  }
}
