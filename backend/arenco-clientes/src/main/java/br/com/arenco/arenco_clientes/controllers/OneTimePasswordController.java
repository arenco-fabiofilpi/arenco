package br.com.arenco.arenco_clientes.controllers;

import br.com.arenco.arenco_clientes.dtos.ContactIdRequestBody;
import br.com.arenco.arenco_clientes.dtos.OtpRequestBody;
import br.com.arenco.arenco_clientes.facades.AuthenticationFacade;
import br.com.arenco.arenco_clientes.facades.ArencoAuthLibUserFacade;
import br.com.arenco.arenco_clientes.dtos.misc.ErrorDto;
import br.com.arenco.arenco_clientes.dtos.user.ContactDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/otp")
@PreAuthorize("hasAnyRole('ROLE_AUTHENTICATED_PRE_SESSION')")
@Tag(
    name = "One Time Password",
    description = "Operações relacionadas a envio e validação de One Time Password (OTP)")
public class OneTimePasswordController {
  private final ArencoAuthLibUserFacade arencoAuthLibUserFacade;
  private final AuthenticationFacade authenticationFacade;

  @Operation(
      summary = "Contatos Disponiveis",
      description =
          "Consultar os contatos que o cliente possui disponíveis para que receba um One Time Password (OTP)",
      responses = {
        @ApiResponse(responseCode = "200", description = "Contatos retornados com sucesso"),
        @ApiResponse(
            responseCode = "400",
            description = "Erro na requisição",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorDto.class))),
        @ApiResponse(
            responseCode = "401",
            description = "Não autorizado",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorDto.class))),
        @ApiResponse(
            responseCode = "404",
            description = "Usuário não encontrado",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorDto.class))),
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorDto.class)))
      })
  @GetMapping("/contacts-available")
  public ResponseEntity<List<ContactDto>> getContactsAvailable() {
    final var response = arencoAuthLibUserFacade.getContactsForOtpSend();
    return ResponseEntity.ok(response);
  }

  @Operation(
      summary = "Enviar OTP",
      description = "Enviar o código de verificação",
      responses = {
        @ApiResponse(responseCode = "200", description = "Código de verificação enviado"),
        @ApiResponse(responseCode = "401", description = "Código de verificação não enviado")
      })
  @PostMapping(value = "/trigger")
  public ResponseEntity<HttpStatus> sendOtp(
      @RequestBody @Valid final ContactIdRequestBody contactIdRequestBody) {
    log.info("AuthenticationController: OTP");
    authenticationFacade.send2FactorAuthOtp(contactIdRequestBody.contactId());
    return ResponseEntity.ok().build();
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
      @RequestBody @Valid final OtpRequestBody otpRequestBody) {
    log.info("AuthenticationController: Validate");
    authenticationFacade.validateSecondFactorAuthentication(otpRequestBody.otp());
    log.info("AuthenticationController: Valid");
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
      @RequestBody @Valid final OtpRequestBody otpRequestBody,
      @NonNull final HttpServletResponse response,
      @NonNull final HttpServletRequest request) {
    log.info("AuthenticationController: Authenticate");
    authenticationFacade.authenticateWithSecondFactorAuthentication(
        request, response, otpRequestBody.otp());
    log.info("AuthenticationController: Authenticated");
    return ResponseEntity.ok(HttpStatus.OK);
  }
}
