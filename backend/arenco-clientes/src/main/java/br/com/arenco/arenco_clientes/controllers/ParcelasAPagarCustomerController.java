package br.com.arenco.arenco_clientes.controllers;

import br.com.arenco.arenco_clientes.facades.CustomerParcelasAPagarFacade;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/customer/parcelas_aberto")
@PreAuthorize("hasAnyRole('ROLE_CUSTOMER')")
@Tag(name = "Parcelas a Pagar", description = "Operações relacionadas a parcelas a pagar")
public class ParcelasAPagarCustomerController {
  private final CustomerParcelasAPagarFacade customerParcelasAPagarFacade;
}
