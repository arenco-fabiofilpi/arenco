package br.com.arenco.arenco_clientes.controllers;

import br.com.arenco.arenco_clientes.facades.ArencoTitulosRecebidosFacade;
import br.com.arenco.arenco_clientes.dtos.agreements.TituloRecebidoCompletoDto;
import br.com.arenco.arenco_clientes.dtos.agreements.TituloRecebidoDto;
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
@RequestMapping("/admin/recebidos")
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
@Tag(
    name = "Recebidos - API's Administrativa",
    description = "Operações administrativas relacionadas a títulos recebidos de clientes")
public class RecebidosAdminController {
  private final ArencoTitulosRecebidosFacade arencoTitulosRecebidosFacade;

  @Operation(
      summary = "Pesquisar Titulos Recebidos - Operação administrativa",
      description = "Retorna a lista de Titulos Recebidos a partir do formulário de pesquisa",
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
  public ResponseEntity<PageResponse<TituloRecebidoDto>> pesquisarTitulosRecebidos(
      @RequestParam(value = "page", defaultValue = "0") final int page,
      @RequestParam(value = "size", defaultValue = "20") final int size,
      final Pageable pageable) {
    final var result =
        arencoTitulosRecebidosFacade.pesquisarTitulosRecebidos(
            PageableUtils.adjustPageable(pageable, page, size));
    log.info("ContratosAdminController: Pesquisa Titulos Recebidos");
    final PageResponse<TituloRecebidoDto> pageResponse = new PageResponse<>(result);
    return ResponseEntity.status(HttpStatus.OK).body(pageResponse);
  }

  @Operation(
      summary = "Pegar Titulo Recebido por ID - Operação administrativa",
      description = "Retorna os dados do Titulo Recebido a partir do ID",
      responses = {
        @ApiResponse(responseCode = "200", description = "Titulo Recebido"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "404", description = "Titulo Recebido não encontrado"),
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno",
            content = @Content(schema = @Schema(implementation = ErrorDto.class)))
      })
  @GetMapping
  public ResponseEntity<TituloRecebidoCompletoDto> pegarTituloRecebidoPorId(
      @RequestParam(value = "id") final String id) {
    log.info("ContratosAdminController: Pegar Titulo Recebido por ID");
    final Optional<TituloRecebidoCompletoDto> optional =
        arencoTitulosRecebidosFacade.pegarTituloRecebido(id);
    if (optional.isEmpty()) {
      log.error("ContratosAdminController: Titulo Recebido não encontrado para o ID {}", id);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.status(HttpStatus.OK).body(optional.get());
  }
}
