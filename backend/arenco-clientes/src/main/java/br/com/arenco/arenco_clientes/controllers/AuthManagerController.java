package br.com.arenco.arenco_clientes.controllers;

import br.com.arenco.arenco_clientes.dtos.SessionDto;
import br.com.arenco.arenco_clientes.facades.AuthManagementFacade;
import br.com.arenco.arenco_clientes.dtos.misc.PageResponse;
import br.com.arenco.arenco_clientes.utils.PageableUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
@RequestMapping(value = "/auth-manager")
@Tag(
    name = "Autenticação - Gestão",
    description =
        "Operações relacionadas à de autenticações, como verificar sessões ativas e usuários bloqueados.")
public class AuthManagerController {
  private final AuthManagementFacade authManagementFacade;

  @Operation(summary = "Sessões Ativas", description = "Consultar sessões ativas")
  @GetMapping("/active-sessions")
  public ResponseEntity<PageResponse<SessionDto>> getActiveSessions(
      @RequestParam(value = "page", defaultValue = "1") final int page,
      @RequestParam(value = "size", defaultValue = "20") final int size,
      final Pageable pageable) {
    final var response =
        authManagementFacade.getActiveSessions(PageableUtils.adjustPageable(pageable, page, size));
    return ResponseEntity.ok().body(new PageResponse<>(response));
  }

  @Operation(description = "Remover sessão ativa")
  @DeleteMapping("/active-sessions/{id}")
  public ResponseEntity<HttpStatus> removeActiveSession(@PathVariable final String id) {
    authManagementFacade.removeActiveSession(id);
    return ResponseEntity.accepted().build();
  }

  @Operation(summary = "Atividades Suspeitas", description = "Consultar atividades suspeitas")
  @GetMapping("/suspicious-activities")
  public ResponseEntity<HttpStatus> getSuspiciousActivities() {
    return ResponseEntity.ok().body(HttpStatus.OK);
  }

  @Operation(summary = "IP's bloqueados", description = "Consultar IP's bloqueados")
  @GetMapping("/blocked-ips")
  public ResponseEntity<HttpStatus> getBlockedIps() {
    return ResponseEntity.ok().body(HttpStatus.OK);
  }

  @Operation(summary = "Usuários bloqueados", description = "Consultar usuários bloqueados")
  @GetMapping("/blocked-users")
  public ResponseEntity<HttpStatus> getBlockedUsers() {
    return ResponseEntity.ok().body(HttpStatus.OK);
  }
}
