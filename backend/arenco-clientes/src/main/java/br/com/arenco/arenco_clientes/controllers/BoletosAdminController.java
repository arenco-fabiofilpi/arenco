package br.com.arenco.arenco_clientes.controllers;

import br.com.arenco.arenco_clientes.facades.ArencoBoletosFacade;
import br.com.arenco.arenco_clientes.dtos.agreements.BoletoFileDto;
import br.com.arenco.arenco_clientes.dtos.misc.ErrorDto;
import br.com.arenco.arenco_clientes.dtos.misc.PageResponse;
import br.com.arenco.arenco_clientes.utils.PageableUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
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
@RequestMapping("/admin/boletos")
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
@Tag(
    name = "Boletos - API's Administrativa",
    description = "Operações administrativas relacionadas a boletos de clientes")
public class BoletosAdminController {
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
  public ResponseEntity<PageResponse<BoletoFileDto>> pesquisar(
      @RequestParam(value = "page", defaultValue = "0") final int page,
      @RequestParam(value = "size", defaultValue = "20") final int size,
      final Pageable pageable) {
    final var result =
        arencoBoletosFacade.search(PageableUtils.adjustPageable(pageable, page, size));
    final PageResponse<BoletoFileDto> pageResponse = new PageResponse<>(result);
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
  @GetMapping("/{id}")
  public ResponseEntity<HttpStatus> pegar(
      @PathVariable(value = "id") final String id, final HttpServletResponse response) {
    arencoBoletosFacade.get(id, response);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
