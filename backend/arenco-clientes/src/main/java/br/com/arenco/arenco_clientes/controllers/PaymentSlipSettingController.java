package br.com.arenco.arenco_clientes.controllers;

import br.com.arenco.arenco_clientes.facades.PaymentSlipSettingsFacade;
import br.com.arenco.arenco_clientes.validations.PaymentSlipSettingsValidator;
import br.com.arenco.arenco_clientes.dtos.misc.PageResponse;
import br.com.arenco.arenco_clientes.dtos.settings.PaymentSlipSettingsDto;
import br.com.arenco.arenco_clientes.utils.PageableUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/payment-slip-settings")
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
@Tag(
    name = "Consultar lista de Configurações para Geração de Boletos",
    description =
        "Retorna a lista de configurações disponíveis para serem utilizadas na geração de boletos")
public class PaymentSlipSettingController {
  private final PaymentSlipSettingsFacade paymentSlipSettingsFacade;
  private final PaymentSlipSettingsValidator paymentSlipSettingsValidator;

  @InitBinder
  public void initBinder(final WebDataBinder binder) {
    binder.addValidators(paymentSlipSettingsValidator);
  }

  @GetMapping
  @Operation(
      summary = "Consultar Configurações de Boleto",
      description = "Consultar Configurações para serem utilizadas na geração de Boleto")
  public ResponseEntity<PageResponse<PaymentSlipSettingsDto>> getListOfPaymentSlipSettings(
      @RequestParam(value = "page", defaultValue = "0") final int page,
      @RequestParam(value = "size", defaultValue = "20") final int size,
      final Pageable pageable) {
    final var result =
        paymentSlipSettingsFacade.getListOfPaymentSlipSettings(
            PageableUtils.adjustPageable(pageable, page, size));
    final PageResponse<PaymentSlipSettingsDto> pageResponse = new PageResponse<>(result);
    return ResponseEntity.status(HttpStatus.OK).body(pageResponse);
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Consultar Configurações de Boleto",
      description = "Consultar Configurações para serem utilizadas na geração de Boleto")
  public ResponseEntity<PaymentSlipSettingsDto> getPaymentSlipSettings(
      @PathVariable final String id) {
    final var result = paymentSlipSettingsFacade.getPaymentSlipSettings(id);
    return ResponseEntity.ok().body(result);
  }

  @PutMapping("/{id}")
  @Operation(
      summary = "Atualizar Configurações de Boleto",
      description = "Atualizar Configurações para serem utilizadas na geração de Boleto")
  public ResponseEntity<HttpStatus> updatePaymentSlipSettings(
      @PathVariable final String id, @RequestBody @Validated final PaymentSlipSettingsDto dto) {
    paymentSlipSettingsFacade.updatePaymentSlipSettings(id, dto);
    return ResponseEntity.accepted().build();
  }

  @PostMapping
  @Operation(
      summary = "Criar Configurações de Boleto",
      description = "Criar Configurações para serem utilizadas na geração de Boleto")
  public ResponseEntity<HttpStatus> createPaymentSlipSettings(
      @RequestBody @Validated final PaymentSlipSettingsDto dto) {
    final var result = paymentSlipSettingsFacade.createPaymentSlipSettings(dto);
    log.info("Criada Configuracao para geracao de boleto {}", result);
    return ResponseEntity.created(result).build();
  }

  @DeleteMapping("/{id}")
  @Operation(
      summary = "Remover Configurações de Boleto",
      description = "Remover Configurações de geração de Boleto")
  public ResponseEntity<HttpStatus> removePaymentSlipSettings(@PathVariable final String id) {
    paymentSlipSettingsFacade.deletePaymentSlipSettings(id);
    log.info("Removida configuração para geração de boleto");
    return ResponseEntity.accepted().build();
  }
}
