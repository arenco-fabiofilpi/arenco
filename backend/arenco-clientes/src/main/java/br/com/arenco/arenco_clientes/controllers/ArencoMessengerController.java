package br.com.arenco.arenco_clientes.controllers;

import br.com.arenco.arenco_clientes.dtos.misc.PageResponse;
import br.com.arenco.arenco_clientes.utils.PageableUtils;
import br.com.arenco.arenco_clientes.dtos.EmailDto;
import br.com.arenco.arenco_clientes.dtos.SmsDto;
import br.com.arenco.arenco_clientes.facades.ArencoMessengerFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
@Tag(name = "Mensagens", description = "Operações relacionadas a mensagerias")
public class ArencoMessengerController {
  private final ArencoMessengerFacade arencoMessengerFacade;

  @Operation(summary = "Mensagens SMS enviadas", description = "Consultar mensagens SMS enviadas")
  @GetMapping("/sms-sent")
  public ResponseEntity<PageResponse<SmsDto>> getSentSms(
      @RequestParam(value = "page", defaultValue = "1") final int page,
      @RequestParam(value = "size", defaultValue = "20") final int size,
      final Pageable pageable) {
    final var response = arencoMessengerFacade.findAllSentSms(PageableUtils.adjustPageable(pageable, page, size));
    return ResponseEntity.ok().body(new PageResponse<>(response));
  }

  @Operation(
      summary = "Mensagens E-mail enviadas",
      description = "Consultar mensagens E-mail enviadas")
  @GetMapping("/emails-sent")
  public ResponseEntity<PageResponse<EmailDto>> getSentEmails(
      @RequestParam(value = "page", defaultValue = "1") final int page,
      @RequestParam(value = "size", defaultValue = "20") final int size,
      final Pageable pageable) {
    final var response =
        arencoMessengerFacade.findAllSentEmails(PageableUtils.adjustPageable(pageable, page, size));
    return ResponseEntity.ok().body(new PageResponse<>(response));
  }
}
