package br.com.arenco.arenco_clientes.controllers;

import br.com.arenco.arenco_clientes.facades.ArencoBoletosFacade;
import br.com.arenco.arenco_clientes.facades.ArencoTitulosAReceberFacade;
import br.com.arenco.arenco_clientes.dtos.agreements.SolicitacaoDeProcessamentoDeBoletosDto;
import br.com.arenco.arenco_clientes.dtos.agreements.TituloAReceberCompletoDto;
import br.com.arenco.arenco_clientes.dtos.agreements.TituloAReceberDto;
import br.com.arenco.arenco_clientes.dtos.misc.ErrorDto;
import br.com.arenco.arenco_clientes.dtos.misc.PageResponse;
import br.com.arenco.arenco_clientes.utils.PageableUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@RequestMapping("/admin/a-receber")
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
@Tag(
    name = "A Receber - API's Administrativa",
    description = "Operações administrativas relacionadas a títulos a receber de clientes")
public class AReceberAdminController {
  private final ArencoTitulosAReceberFacade arencoTitulosAReceberFacade;
  private final ArencoBoletosFacade arencoBoletosFacade;

  @Operation(
      summary = "Pesquisar de Titulos a Receber - Operação administrativa",
      description = "Retorna a lista de Titulos a Receber a partir do formulário de pesquisa",
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
  public ResponseEntity<PageResponse<TituloAReceberDto>> pesquisarTitulosAReceber(
      @RequestParam(value = "page", defaultValue = "0") final int page,
      @RequestParam(value = "size", defaultValue = "20") final int size,
      final Pageable pageable) {
    final var result =
        arencoTitulosAReceberFacade.pesquisarTitulosAReceber(
            PageableUtils.adjustPageable(pageable, page, size));
    log.info("ContratosAdminController: Pesquisa Titulos a Receber");
    final PageResponse<TituloAReceberDto> pageResponse = new PageResponse<>(result);
    return ResponseEntity.status(HttpStatus.OK).body(pageResponse);
  }

  @Operation(
      summary = "Pegar Titulo a Receber por ID - Operação administrativa",
      description = "Retorna os dados do Titulo a Receber a partir do ID",
      responses = {
        @ApiResponse(responseCode = "200", description = "Titulo a Receber"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "404", description = "Titulo a Receber não encontrado"),
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno",
            content = @Content(schema = @Schema(implementation = ErrorDto.class)))
      })
  @GetMapping
  public ResponseEntity<TituloAReceberCompletoDto> pegarTituloAReceberPorId(
      @RequestParam(value = "id") final String id) {
    log.info("ContratosAdminController: Pegar Titulo a Receber por ID");
    final TituloAReceberCompletoDto response = arencoTitulosAReceberFacade.pegarTituloAReceber(id);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @PostMapping(value = "/processamento-de-boletos")
  public ResponseEntity<HttpStatus> solicitarProcessamentoDeBoletos(
      @RequestBody
          final SolicitacaoDeProcessamentoDeBoletosDto solicitacaoDeProcessamentoDeBoletosDto) {
    log.info(
        "ContratosAdminController: Solicitando processamento de boletos. Tipo do Processamento: {}, Lista de Titulos: {}",
        solicitacaoDeProcessamentoDeBoletosDto.tipoProcessamentoBoleto(),
        solicitacaoDeProcessamentoDeBoletosDto.titulosAReceberIdList());
    arencoBoletosFacade.solicitarProcessamentoDeBoletos(solicitacaoDeProcessamentoDeBoletosDto);
    return ResponseEntity.status(HttpStatus.OK).build();
  }
}
