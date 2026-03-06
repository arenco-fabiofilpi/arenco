package br.com.arenco.arenco_clientes.controllers;

import br.com.arenco.arenco_clientes.dtos.ResetPasswordRequestBody;
import br.com.arenco.arenco_clientes.dtos.UsernameAndTokenRequestBody;
import br.com.arenco.arenco_clientes.dtos.UsernameRequestBody;
import br.com.arenco.arenco_clientes.facades.ResetPasswordFacade;
import br.com.arenco.arenco_clientes.context.RequestContext;
import br.com.arenco.arenco_clientes.dtos.misc.ErrorDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@Tag(
    name = "Esqueci Minha Senha",
    description = "Operações relacionadas ao Processo de Esqueci Minha Senha")
@RestController
@Slf4j
@RequestMapping("/password-reset")
@RequiredArgsConstructor
public class ResetPasswordController {
  private final ResetPasswordFacade resetPasswordFacade;

  @Operation(
      summary = "Esqueci minha senha",
      description = "Realizar o processo de Esqueci Minha Senha por SMS ou E-mail",
      responses = {
        @ApiResponse(responseCode = "200", description = "Processo solicitado com sucesso"),
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
            responseCode = "500",
            description = "Erro interno",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorDto.class)))
      })
  @PostMapping
  public ResponseEntity<HttpStatus> askPasswordRecovery(
      @RequestBody @Valid final UsernameRequestBody usernameRequestBody) {
    final var username = usernameRequestBody.username();
    final var ip = RequestContext.get().ip();
    final var userAgent = RequestContext.get().userAgent();
    log.info("Init - Asking password recovery for {} by IP {}", username, ip);
    try {
      resetPasswordFacade.askPasswordReset(username);
    } catch (final UsernameNotFoundException ignored) {
      log.warn(
          "askPasswordRecovery - Username não encontrado. Username {}. IP {}. User-Agent {}",
          username,
          ip,
          userAgent);
    }
    log.info("Finish - Password recovery for {} by IP {}", username, ip);
    return ResponseEntity.ok().build();
  }

  @Operation(
      summary = "Validar Token Esqueci Minha Senha",
      description = "Validar Token de Esqueci Minha Senha",
      responses = {
        @ApiResponse(responseCode = "200", description = "Token validado com sucesso"),
        @ApiResponse(
            responseCode = "400",
            description = "Erro na requisição",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorDto.class))),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(
            responseCode = "429",
            description = "Muitas requisições",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorDto.class)))
      })
  @PostMapping("/validate")
  public ResponseEntity<HttpStatus> validateResetPasswordToken(
      @RequestBody @Valid final UsernameAndTokenRequestBody usernameAndTokenRequestBody) {
    final var username = usernameAndTokenRequestBody.username();
    final var token = usernameAndTokenRequestBody.token();
    log.info("Validating reset password token");
    final var ip = RequestContext.get().ip();
    final var userAgent = RequestContext.get().userAgent();
    try {
      resetPasswordFacade.validateResetPasswordToken(username, token);
    } catch (final UsernameNotFoundException ignored) {
      log.warn(
          "validateResetPasswordToken - Username não encontrado. Username {}. IP {}. User-Agent {}",
          username,
          ip,
          userAgent);
    }
    log.info("Validated reset password token");
    return ResponseEntity.ok().build();
  }

  @Operation(
      summary = "Resetar Senha",
      description = "Resetar Senha usando Token de Esqueci Minha Senha",
      responses = {
        @ApiResponse(responseCode = "202", description = "Senha resetada com sucesso"),
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
            responseCode = "429",
            description = "Muitas requisições",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorDto.class)))
      })
  @PostMapping("/reset")
  public ResponseEntity<HttpStatus> resetPassword(
      @RequestBody @Valid final ResetPasswordRequestBody resetPasswordRequestBody) {
    final var username = resetPasswordRequestBody.username();
    final var token = resetPasswordRequestBody.token();
    log.info("Resetting password");
    final var ip = RequestContext.get().ip();
    final var userAgent = RequestContext.get().userAgent();
    try {
      resetPasswordFacade.resetPassword(username, token, resetPasswordRequestBody.password());
    } catch (final UsernameNotFoundException ignored) {
      log.warn(
          "resetPassword - Username não encontrado. Username {}. IP {}. User-Agent {}",
          username,
          ip,
          userAgent);
    }
    log.info("Password reset");
    return ResponseEntity.accepted().build();
  }
}
