package br.com.arenco.arenco_clientes.controllers;

import br.com.arenco.arenco_clientes.facades.AdminUsersFacade;
import br.com.arenco.arenco_clientes.dtos.misc.PageResponse;
import br.com.arenco.arenco_clientes.dtos.user.UserDto;
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
@RequestMapping(value = "/admin-management")
@Tag(
    name = "Gerenciamento de Usuarios Administradores",
    description = "Operações relacionadas a gestão de usuários administradores da plataforma")
public class AdminManagementController {
  private final AdminUsersFacade facade;

  @GetMapping("/users")
  @Operation(
      summary = "Consultar Administradores",
      description = "Consultar Usuários Administradores")
  public ResponseEntity<PageResponse<UserDto>> getAdminUsers(
      @RequestParam(value = "page", defaultValue = "1") final int page,
      @RequestParam(value = "size", defaultValue = "20") final int size,
      final Pageable pageable) {
    final var result = facade.findAll(PageableUtils.adjustPageable(pageable, page, size));
    return ResponseEntity.ok().body(new PageResponse<>(result));
  }

  @DeleteMapping("/users/{id}")
  @Operation(summary = "Remover Administrador", description = "Remover Usuário Administrador")
  public ResponseEntity<HttpStatus> removeAdminUser(@PathVariable final String id) {
    facade.removeAdminUser(id);
    return ResponseEntity.accepted().build();
  }
}
