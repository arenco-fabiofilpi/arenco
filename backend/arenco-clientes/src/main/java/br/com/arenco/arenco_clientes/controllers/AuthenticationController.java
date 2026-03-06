package br.com.arenco.arenco_clientes.controllers;

import br.com.arenco.arenco_clientes.facades.AuthenticationFacade;
import br.com.arenco.arenco_clientes.context.ArencoAuthenticatedPreSessionContext;
import br.com.arenco.arenco_clientes.context.ArencoAuthenticatedSessionContext;
import br.com.arenco.arenco_clientes.dtos.authentication.PasswordLoginRequest;
import br.com.arenco.arenco_clientes.dtos.misc.ErrorDto;
import br.com.arenco.arenco_clientes.enums.UserType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/auth")
@Tag(name = "Autenticação", description = "Operações relacionadas a autenticação")
public class AuthenticationController {
  private final AuthenticationFacade authenticationFacade;

  @Operation(
      summary = "Login",
      description = "Realiza o login do usuário e retorna um ID de sessão.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Login realizado com sucesso. Retorna um Cookie com o ID da sessão.",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = HttpStatus.class))),
        @ApiResponse(
            responseCode = "401",
            description = "Login não autorizado. Verifique suas credenciais.",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorDto.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Login proibido. Acesso negado para o usuário.",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorDto.class))),
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno do servidor. Tente novamente mais tarde.",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorDto.class)))
      })
  @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<HttpStatus> login(
      @RequestBody @Valid final PasswordLoginRequest passwordLoginRequest,
      @NonNull final HttpServletResponse response) {
    log.info("Regular User Logging in");
    final var responseStatus =
        authenticationFacade.login(
            response, passwordLoginRequest.username(), passwordLoginRequest.password());
    return new ResponseEntity<>(responseStatus, responseStatus);
  }

  @Operation(summary = "Confere Autenticação", description = "Conferência de Autenticação")
  @GetMapping
  public ResponseEntity<HttpStatus> checkAuthentication() {
    final var contextAuthentication = SecurityContextHolder.getContext().getAuthentication();
    if (contextAuthentication instanceof ArencoAuthenticatedPreSessionContext) {
      log.debug("checkAuthentication: Returning Partial Content due to PreSessionContext as current Authentication");
      return new ResponseEntity<>(HttpStatus.PARTIAL_CONTENT);
    }
    if (!(contextAuthentication instanceof ArencoAuthenticatedSessionContext arencoAuthenticatedSessionContext)) {
      log.debug("checkAuthentication: Returning Forbidden due to contextAuthentication not instance of ArencoAuthenticatedSessionContext");
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    final var userModel = arencoAuthenticatedSessionContext.getUser();
    if (userModel == null) {
      log.debug("checkAuthentication: Returning Forbidden due to contextAuthentication with user model null");
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    if (userModel.getUserType() == UserType.CLIENTE) {
      log.debug("checkAuthentication: Returning Forbidden because Customers should not use CRM Administration Panel");
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    return ResponseEntity.ok(HttpStatus.OK);
  }

  @PostMapping("/logout")
  public ResponseEntity<String> logout(
      @NonNull final HttpServletRequest request, @NonNull final HttpServletResponse response) {
    authenticationFacade.logout(request, response);
    return ResponseEntity.ok().build();
  }
}
