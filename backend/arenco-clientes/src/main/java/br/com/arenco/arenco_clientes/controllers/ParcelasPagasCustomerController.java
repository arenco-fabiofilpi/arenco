package br.com.arenco.arenco_clientes.controllers;

import br.com.arenco.arenco_clientes.facades.CustomerParcelasPagasFacade;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/customer/parcelas_pagas")
@PreAuthorize("hasAnyRole('ROLE_CUSTOMER')")
@Tag(name = "Parcelas Pagas", description = "Operações relacionadas a parcelas pagas")
public class ParcelasPagasCustomerController {
  private final CustomerParcelasPagasFacade customerParcelasPagasFacade;
}
