package br.com.arenco.arenco_clientes.controllers;

import br.com.arenco.arenco_clientes.dtos.misc.PageResponse;
import br.com.arenco.arenco_clientes.dtos.settings.BetaUserDto;
import br.com.arenco.arenco_clientes.utils.PageableUtils;
import br.com.arenco.arenco_clientes.facades.ArencoMessengerBetaUsersFacade;
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
@RequestMapping("/settings/beta-users")
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
@Tag(
    name = "Configurações de Beta Users para Mensagens Automaticas",
    description =
        "Operações relacionadas a cadastro e remoção de Beta Users para a funcionalidade de Mensagens Automáticas")
public class ArencoMessengerBetaUsersController {
  private final ArencoMessengerBetaUsersFacade facade;

  @GetMapping
  @Operation(summary = "Consultar Beta Users", description = "Consultar Beta Users")
  public ResponseEntity<PageResponse<BetaUserDto>> getMessengerBetaUsers(
      @RequestParam(value = "page", defaultValue = "1") final int page,
      @RequestParam(value = "size", defaultValue = "20") final int size,
      final Pageable pageable) {
    final var result = facade.findAll(PageableUtils.adjustPageable(pageable, page, size));
    return ResponseEntity.ok().body(new PageResponse<>(result));
  }

  @PostMapping
  @Operation(summary = "Adicionar Beta User", description = "Adicionar Beta User")
  public ResponseEntity<HttpStatus> addBetaUser(@RequestParam final String userModelId) {
    final var result = facade.addBetaUser(userModelId);
    log.info("Adicionado Beta User {}", userModelId);
    return new ResponseEntity<>(result);
  }

  @DeleteMapping
  @Operation(summary = "Remover Beta User", description = "Remover Beta User")
  public ResponseEntity<HttpStatus> removeBetaUser(@RequestParam final String userModelId) {
    facade.removeBetaUser(userModelId);
    log.info("Removido Beta User {}", userModelId);
    return ResponseEntity.accepted().build();
  }
}
