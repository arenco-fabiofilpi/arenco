package br.com.arenco.arenco_clientes.controllers;

import br.com.arenco.arenco_clientes.dtos.UsernameAndTokenRequestBody;
import br.com.arenco.arenco_clientes.dtos.UsernameRequestBody;
import br.com.arenco.arenco_clientes.facades.CustomerAuthenticationFacade;
import br.com.arenco.arenco_clientes.context.ArencoAuthenticatedSessionContext;
import br.com.arenco.arenco_clientes.enums.UserType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/customer/auth")
@Tag(name = "Autenticação", description = "Operações relacionadas a autenticação")
public class CustomerAuthenticationController {
  private final CustomerAuthenticationFacade customerAuthenticationFacade;

  @Operation(
      summary = "Login",
      description = "Realiza o login de cliente (sem senha), e envia um OTP.")
  @PostMapping(value = "/login")
  public ResponseEntity<HttpStatus> customerLogin(
      @RequestBody @Valid final UsernameRequestBody usernameRequestBody) {
    log.info("Customer Logging in");
    customerAuthenticationFacade.customerLogin(usernameRequestBody.username());
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Operation(
      summary = "Validar OTP",
      description = "Validar o segundo fator de autenticação",
      responses = {
        @ApiResponse(responseCode = "200", description = "Código de verificação válido"),
        @ApiResponse(responseCode = "400", description = "Código de verificação inválido")
      })
  @PostMapping(value = "/validation")
  public ResponseEntity<HttpStatus> validate(
      @RequestBody @Valid final UsernameAndTokenRequestBody usernameAndTokenRequestBody) {
    log.info("CustomerAuthenticationController: Validate");
    customerAuthenticationFacade.validateOtp(
        usernameAndTokenRequestBody.username(), usernameAndTokenRequestBody.token());
    log.info("CustomerAuthenticationController: Valid");
    return ResponseEntity.ok(HttpStatus.OK);
  }

  @Operation(
      summary = "Autenticar OTP",
      description = "Autenticar o segundo fator de autenticação",
      responses = {
        @ApiResponse(responseCode = "200", description = "Autenticado com sucesso"),
        @ApiResponse(responseCode = "401", description = "Não autenticado")
      })
  @PostMapping(value = "/authentication")
  public ResponseEntity<HttpStatus> authenticate(
      @RequestBody @Valid final UsernameAndTokenRequestBody usernameAndTokenRequestBody,
      @NonNull final HttpServletResponse response) {
    log.info("CustomerAuthenticationController: Authenticate");
    customerAuthenticationFacade.authenticateOtp(
        usernameAndTokenRequestBody.username(), usernameAndTokenRequestBody.token(), response);
    log.info("CustomerAuthenticationController: Authenticated");
    return ResponseEntity.ok(HttpStatus.OK);
  }

  @Operation(summary = "Confere Autenticação do Cliente", description = "Conferência de Autenticação do Cliente")
  @GetMapping
  public ResponseEntity<HttpStatus> checkCustomerAuthentication() {
    final var contextAuthentication = SecurityContextHolder.getContext().getAuthentication();
    if (!(contextAuthentication instanceof ArencoAuthenticatedSessionContext arencoAuthenticatedSessionContext)) {
      log.debug("checkCustomerAuthentication: Returning Forbidden due to contextAuthentication not instance of ArencoAuthenticatedSessionContext");
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    final var userModel = arencoAuthenticatedSessionContext.getUser();
    if (userModel == null) {
      log.debug("checkCustomerAuthentication: Returning Forbidden due to contextAuthentication with user model null");
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    if (userModel.getUserType() == UserType.ADMINISTRADOR) {
      log.debug("checkCustomerAuthentication: Returning Forbidden because Admins should not use Customer Panel");
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    return ResponseEntity.ok(HttpStatus.OK);
  }
}
