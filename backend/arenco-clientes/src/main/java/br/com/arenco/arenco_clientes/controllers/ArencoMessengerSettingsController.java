package br.com.arenco.arenco_clientes.controllers;

import br.com.arenco.arenco_clientes.dtos.settings.MessengerSettingsDto;
import br.com.arenco.arenco_clientes.dtos.UpdateMessengerSettingsDto;
import br.com.arenco.arenco_clientes.facades.ArencoMessengerSettingsFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/settings")
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
@Tag(
    name = "Configurações de Mensagens",
    description = "Operações relacionadas a configurações relacionadas a mensagerias")
public class ArencoMessengerSettingsController {
  private final ArencoMessengerSettingsFacade arencoMessengerSettingsFacade;

  @GetMapping
  @Operation(
      summary = "Pegar Configurações",
      description = "Consultar configurações referentes a disparos automáticos")
  public ResponseEntity<MessengerSettingsDto> getSettings() {
    final var result = arencoMessengerSettingsFacade.getSettings();
    return ResponseEntity.ok().body(result);
  }

  @PatchMapping
  @Operation(
      summary = "Atualizar Configurações",
      description = "Atualizar configurações referentes a disparos automáticos")
  public ResponseEntity<HttpStatus> updateSettings(
      @RequestBody final UpdateMessengerSettingsDto updateSettingsDto) {
    arencoMessengerSettingsFacade.updateSettings(updateSettingsDto);
    log.info("Atualizadas configurações de disparos automáticos");
    return ResponseEntity.accepted().build();
  }
}
