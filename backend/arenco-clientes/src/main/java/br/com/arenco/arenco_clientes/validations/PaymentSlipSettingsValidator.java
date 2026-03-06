package br.com.arenco.arenco_clientes.validations;

import br.com.arenco.arenco_clientes.dtos.settings.PaymentSlipSettingsDto;
import java.util.Set;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Slf4j
@Component
public class PaymentSlipSettingsValidator implements Validator {
  private static final Set<String> UFS =
      Set.of(
          "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB",
          "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO");

  @Override
  public boolean supports(@NonNull final Class<?> clazz) {
    return PaymentSlipSettingsDto.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(@NonNull final Object target, @NonNull final Errors errors) {
    final PaymentSlipSettingsDto dto = (PaymentSlipSettingsDto) target;

    rejectIfBlank(
        errors,
        "nomeBeneficiario",
        dto.getNomeBeneficiario(),
        "Nome do beneficiário é obrigatório");
    if (dto.getBanco() == null) {
      errors.rejectValue("banco", "banco.obrigatorio", "Banco é obrigatório");
    }
    rejectIfBlank(errors, "documento", dto.getDocumento(), "Documento (CNPJ) é obrigatório");
    rejectIfBlank(errors, "agencia", dto.getAgencia(), "Agência é obrigatória");
    rejectIfBlank(
        errors,
        "codigoBeneficiario",
        dto.getCodigoBeneficiario(),
        "Código do beneficiário é obrigatório");
    rejectIfBlank(
        errors,
        "digitoCodigoBeneficiario",
        dto.getDigitoCodigoBeneficiario(),
        "Dígito do beneficiário é obrigatório");
    rejectIfBlank(errors, "carteira", dto.getCarteira(), "Carteira é obrigatória");
    rejectIfBlank(errors, "logradouro", dto.getLogradouro(), "Logradouro é obrigatório");
    rejectIfBlank(errors, "bairro", dto.getBairro(), "Bairro é obrigatório");
    rejectIfBlank(errors, "cidade", dto.getCidade(), "Cidade é obrigatória");
    rejectIfBlank(errors, "uf", dto.getUf(), "UF é obrigatória");
    rejectIfBlank(errors, "cep", dto.getCep(), "CEP é obrigatório");
    rejectIfBlank(
        errors,
        "especieDocumento",
        dto.getEspecieDocumento(),
        "Espécie do documento é obrigatória");

    // --------- VALIDAÇÕES DE FORMATO (quando valor for informado) ----------
    // documento (CNPJ)
    if (StringUtils.hasText(dto.getDocumento())) {
      final String digits = onlyDigits(dto.getDocumento());
      if (!isCnpj(digits) | !isValidCnpj(digits)) {
        errors.rejectValue(
            "documento", "documento.invalido", "Documento deve ser CNPJ (14 dígitos) válido");
      }
    }

    // agencia: apenas dígitos, 1–10 (deixe flexível)
    if (StringUtils.hasText(dto.getAgencia())) {
      if (!dto.getAgencia().matches("^\\d{1,10}$")) {
        errors.rejectValue(
            "agencia", "agencia.formato", "Agência deve conter apenas dígitos (1 a 10)");
      }
    }

    // codigoBeneficiario: apenas dígitos, 1–20
    if (StringUtils.hasText(dto.getCodigoBeneficiario())) {
      if (!dto.getCodigoBeneficiario().matches("^\\d{1,20}$")) {
        errors.rejectValue(
            "codigoBeneficiario",
            "codigoBeneficiario.formato",
            "Código do beneficiário deve conter apenas dígitos (1 a 20)");
      }
    }

    // digitoCodigoBeneficiario: 1 dígito (alguns bancos usam 'X'—ajuste se precisar)
    if (StringUtils.hasText(dto.getDigitoCodigoBeneficiario())) {
      if (!dto.getDigitoCodigoBeneficiario().matches("^\\d$")) {
        errors.rejectValue(
            "digitoCodigoBeneficiario",
            "digito.formato",
            "Dígito do beneficiário deve ter 1 dígito");
      }
    }

    // carteira: geralmente 2–3 dígitos em muitos bancos (deixamos 1–3)
    if (StringUtils.hasText(dto.getCarteira())) {
      if (!dto.getCarteira().matches("^\\d{1,3}$")) {
        errors.rejectValue("carteira", "carteira.formato", "Carteira deve conter 1 a 3 dígitos");
      }
    }

    // UF: 2 letras válidas
    if (StringUtils.hasText(dto.getUf())) {
      final String uf = dto.getUf().toUpperCase();
      if (!UFS.contains(uf)) {
        errors.rejectValue("uf", "uf.invalida", "UF inválida");
      }
    }

    // CEP: 8 dígitos ou NNNNN-NNN
    if (StringUtils.hasText(dto.getCep())) {
      if (!dto.getCep().matches("^\\d{8}$|^\\d{5}-\\d{3}$")) {
        errors.rejectValue("cep", "cep.formato", "CEP deve ser 8 dígitos ou no formato NNNNN-NNN");
      }
    }

    // Tamanhos máximos razoáveis para campos de texto
    rejectIfExceeds(
        errors,
        "localDePagamento",
        dto.getLocalDePagamento(),
        255,
        "Local de pagamento muito longo (máx 255)");
    rejectIfExceeds(
        errors,
        "nomeBeneficiario",
        dto.getNomeBeneficiario(),
        120,
        "Nome do beneficiário muito longo (máx 120)");
    rejectIfExceeds(
        errors, "instrucoes", dto.getInstrucoes(), 1000, "Instruções muito longas (máx 1000)");
    rejectIfExceeds(
        errors, "logradouro", dto.getLogradouro(), 255, "Logradouro muito longo (máx 255)");
    rejectIfExceeds(errors, "bairro", dto.getBairro(), 120, "Bairro muito longo (máx 120)");
    rejectIfExceeds(errors, "cidade", dto.getCidade(), 120, "Cidade muito longa (máx 120)");
    rejectIfExceeds(
        errors,
        "especieDocumento",
        dto.getEspecieDocumento(),
        30,
        "Espécie de documento muito longa (máx 30)");
  }

  private static void rejectIfBlank(
      final Errors errors, final String field, final String value, final String message) {
    if (!StringUtils.hasText(value)) {
      errors.rejectValue(field, field + ".obrigatorio", message);
    }
  }

  private static void rejectIfExceeds(
      final Errors errors,
      final String field,
      final String value,
      final int max,
      final String message) {
    if (value != null && value.length() > max) {
      errors.rejectValue(field, field + ".tamanho", message);
    }
  }

  // ----------------- Helpers de CPF/CNPJ -----------------
  private static String onlyDigits(final String s) {
    return s.replaceAll("\\D", "");
  }

  private static boolean isCnpj(final String digits) {
    return digits != null && digits.length() == 14;
  }

  private static boolean isValidCnpj(final String cnpj) {
    if (cnpj == null || cnpj.length() != 14 || cnpj.chars().distinct().count() == 1) return false;
    int d1 = cnpjDigit(cnpj, new int[] {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2});
    int d2 = cnpjDigit(cnpj, new int[] {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2});
    return (cnpj.charAt(12) - '0') == d1 && (cnpj.charAt(13) - '0') == d2;
  }

  private static int cnpjDigit(final String cnpj, final int[] weights) {
    int sum = 0;
    for (int i = 0; i < weights.length; i++) sum += (cnpj.charAt(i) - '0') * weights[i];
    int mod = sum % 11;
    return (mod < 2) ? 0 : 11 - mod;
  }
}
