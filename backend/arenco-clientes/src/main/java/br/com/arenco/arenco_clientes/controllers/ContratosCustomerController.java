package br.com.arenco.arenco_clientes.controllers;

import br.com.arenco.arenco_clientes.facades.CustomerContratosFacade;
import br.com.arenco.arenco_clientes.dtos.agreements.ContratoDto;
import br.com.arenco.arenco_clientes.dtos.misc.PageResponse;
import br.com.arenco.arenco_clientes.utils.PageableUtils;
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
@RequestMapping("/customer/contratos")
@PreAuthorize("hasAnyRole('ROLE_CUSTOMER')")
@Tag(name = "Contratos", description = "Operações relacionadas a contratos")
public class ContratosCustomerController {
  private final CustomerContratosFacade customerContratosFacade;

  @GetMapping
  public ResponseEntity<PageResponse<ContratoDto>> pesquisarContratos(
      @RequestParam(value = "page", defaultValue = "0") final int page,
      @RequestParam(value = "size", defaultValue = "20") final int size,
      final Pageable pageable) {
    log.info("ContratosCustomerController: Pesquisa Contratos");
    final var result =
        customerContratosFacade.pesquisarContratos(
            PageableUtils.adjustPageable(pageable, page, size));
    final PageResponse<ContratoDto> pageResponse = new PageResponse<>(result);
    return ResponseEntity.status(HttpStatus.OK).body(pageResponse);
  }
}
