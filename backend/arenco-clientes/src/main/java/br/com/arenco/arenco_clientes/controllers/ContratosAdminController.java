package br.com.arenco.arenco_clientes.controllers;

import br.com.arenco.arenco_clientes.dtos.agreements.ContratoCompletoDto;
import br.com.arenco.arenco_clientes.dtos.agreements.ContratoDto;
import br.com.arenco.arenco_clientes.facades.ArencoContratosFacade;
import br.com.arenco.arenco_clientes.dtos.misc.ErrorDto;
import br.com.arenco.arenco_clientes.dtos.misc.PageResponse;
import br.com.arenco.arenco_clientes.utils.PageableUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
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
@RequestMapping("/admin/contratos")
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
@Tag(
    name = "Contratos - API's Administrativa",
    description = "Operações administrativas relacionadas a contratos de clientes")
public class ContratosAdminController {
  private final ArencoContratosFacade arencoContratosFacade;

  @Operation(
      summary = "Pesquisar Contratos - Operação administrativa",
      description = "Retorna a lista de contratos a partir do formulário de pesquisa",
      responses = {
        @ApiResponse(responseCode = "200", description = "Lista de Contratos"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno",
            content = @Content(schema = @Schema(implementation = ErrorDto.class)))
      })
  @PostMapping(
      value = "/search",
      params = {"page", "size"})
  public ResponseEntity<PageResponse<ContratoDto>> pesquisarContratos(
      @RequestParam(value = "page", defaultValue = "0") final int page,
      @RequestParam(value = "size", defaultValue = "20") final int size,
      final Pageable pageable) {
    log.info("ContratosAdminController: Pesquisa Contratos");
    final var result =
        arencoContratosFacade.pesquisarContratos(
            PageableUtils.adjustPageable(pageable, page, size));
    final PageResponse<ContratoDto> pageResponse = new PageResponse<>(result);
    return ResponseEntity.status(HttpStatus.OK).body(pageResponse);
  }

  @Operation(
      summary = "Pegar Contrato por ID - Operação administrativa",
      description = "Retorna os dados do contrato a partir do ID",
      responses = {
        @ApiResponse(responseCode = "200", description = "Contrato"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "404", description = "Contrato não encontrado"),
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno",
            content = @Content(schema = @Schema(implementation = ErrorDto.class)))
      })
  @GetMapping
  public ResponseEntity<ContratoCompletoDto> pegarContratoPorID(
      @RequestParam(value = "id") final String id) {
    log.info("ContratosAdminController: Pegar Contrato por ID");
    final Optional<ContratoCompletoDto> contratoOptional =
        arencoContratosFacade.pegarContratoPorId(id);
    if (contratoOptional.isEmpty()) {
      log.error("ContratosAdminController: Contrato não encontrado para o ID {}", id);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.status(HttpStatus.OK).body(contratoOptional.get());
  }
}
