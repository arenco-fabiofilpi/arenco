package br.com.arenco.arenco_clientes.controllers;

import br.com.arenco.arenco_clientes.dtos.ChangeCurrentUserPasswordRequest;
import br.com.arenco.arenco_clientes.facades.UsersFacade;
import br.com.arenco.arenco_clientes.dtos.user.ContactDto;
import br.com.arenco.arenco_clientes.dtos.user.UserDto;
import br.com.arenco.arenco_clientes.dtos.user.UserGroupDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/current")
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CUSTOMER')")
@Tag(name = "Usuário Logado", description = "Consultas e alterações para o usuário logado")
public class CurrentUserController {

  private final UsersFacade usersFacade;

  @Operation(
      summary = "Informações Usuário Logado",
      description = "Retorna informações referentes ao Usuário Atual")
  @GetMapping
  public ResponseEntity<UserDto> getCurrentUser() {
    log.info("getCurrentUser - Init");
    final var currentUser = usersFacade.getCurrentUser();
    log.info("getCurrentUser - Finish");
    return currentUser
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
  }

  @Operation(
      summary = "Contatos Usuário Logado",
      description = "Retorna lista de contatos relacionados ao Usuário Atual")
  @GetMapping("/contacts")
  public ResponseEntity<List<ContactDto>> getCurrentUserContacts() {
    log.info("getCurrentUserContacts - Init");
    final var dto = usersFacade.getCurrentUserContacts();
    log.info("getCurrentUserContacts - Finish");
    return ResponseEntity.ok(dto);
  }

  @Operation(
      summary = "Grupos do Usuário Logado",
      description = "Retorna lista de grupos relacionados ao Usuário Atual")
  @GetMapping("/groups")
  public ResponseEntity<List<UserGroupDto>> getCurrentUserGroups() {
    log.info("getCurrentUserGroups - Init");
    final var dto = usersFacade.getCurrentUserGroups();
    log.info("getCurrentUserGroups - Finish");
    return ResponseEntity.ok(dto);
  }

  @Operation(
      summary = "Alterar Senha",
      description = "Altera Senha do Usuario Atual. Precisa da senha atual e da nova senha")
  @PostMapping("/password")
  public ResponseEntity<HttpStatus> changeCurrentUserPassword(
      @RequestBody @Valid final ChangeCurrentUserPasswordRequest changeCurrentUserPasswordRequest) {
    log.info("changeCurrentUserPassword - Init");
    usersFacade.changeCurrentUserPassword(
        changeCurrentUserPasswordRequest.oldPassword(),
        changeCurrentUserPasswordRequest.newPassword());
    log.info("changeCurrentUserPassword - Finish");
    return ResponseEntity.ok().build();
  }
}
