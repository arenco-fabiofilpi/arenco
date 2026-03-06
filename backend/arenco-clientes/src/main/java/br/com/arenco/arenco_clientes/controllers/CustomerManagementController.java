package br.com.arenco.arenco_clientes.controllers;

import br.com.arenco.arenco_clientes.facades.AddressFacade;
import br.com.arenco.arenco_clientes.facades.ContactFacade;
import br.com.arenco.arenco_clientes.facades.ContactPreferenceFacade;
import br.com.arenco.arenco_clientes.facades.CustomerFacade;
import br.com.arenco.arenco_clientes.dtos.misc.PageResponse;
import br.com.arenco.arenco_clientes.dtos.user.AddressDto;
import br.com.arenco.arenco_clientes.dtos.user.ContactFullDto;
import br.com.arenco.arenco_clientes.dtos.user.ContactPreferenceDto;
import br.com.arenco.arenco_clientes.dtos.user.UserDto;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
@RequestMapping("/customers-management")
@Tag(
    name = "Gerenciamento de Clientes",
    description = "Operações relacionadas a gerenciamento de clientes")
public class CustomerManagementController {
  private final ContactPreferenceFacade contactPreferenceFacade;
  private final CustomerFacade customerFacade;
  private final ContactFacade contactFacade;
  private final AddressFacade addressFacade;

  @Operation(summary = "Search for Customers")
  @PostMapping(
      value = "/search",
      params = {"page", "size"})
  public ResponseEntity<PageResponse<UserDto>> searchCustomers(
      @RequestParam(value = "page", defaultValue = "1") final int page,
      @RequestParam(value = "size", defaultValue = "20") final int size,
      final Pageable pageable) {
    log.info("searchCustomers - Init");
    final var response =
        customerFacade.findCustomers(PageableUtils.adjustPageable(pageable, page, size));
    final PageResponse<UserDto> pageResponse = new PageResponse<>(response);
    return ResponseEntity.status(HttpStatus.OK).body(pageResponse);
  }

  @Operation(
      summary = "Encontrar Cliente por UUID",
      description = "Retorna Informações de Cliente referente a um UUID informado")
  @ApiResponse(
      responseCode = "200",
      description = "Informações do Cliente",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = UserDto.class)))
  @ApiResponse(responseCode = "401", description = "Não autorizado")
  @ApiResponse(responseCode = "404", description = "Não encontrado")
  @GetMapping("/{id}")
  public ResponseEntity<UserDto> findCustomerByUuid(@PathVariable final String id) {
    log.info("CustomerManagementController: Find model by id {}", id);
    //    final var UserDtoOptional = customerFacade.findCustomerById(id);
    log.debug("CustomerManagementController: Finished model by id {}", id);
    //    return UserDtoOptional
    //        .map(UserDto -> ResponseEntity.status(HttpStatus.OK).body(UserDto))
    //        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    return ResponseEntity.status(HttpStatus.OK).body(null);
  }

  @Operation(summary = "Search for Addresses")
  @GetMapping(
      value = "/addresses/search",
      params = {"page", "size"})
  public ResponseEntity<PageResponse<AddressDto>> searchAddresses(
      @RequestParam(value = "page", defaultValue = "1") final int page,
      @RequestParam(value = "size", defaultValue = "20") final int size,
      final Pageable pageable) {
    log.info("searchAddresses - Init");
    final var response = addressFacade.search(PageableUtils.adjustPageable(pageable, page, size));
    final PageResponse<AddressDto> pageResponse = new PageResponse<>(response);
    return ResponseEntity.status(HttpStatus.OK).body(pageResponse);
  }

  @Operation(summary = "Search for Contacts")
  @GetMapping(
      value = "/contacts/search",
      params = {"page", "size"})
  public ResponseEntity<PageResponse<ContactFullDto>> searchContacts(
      @RequestParam(value = "page", defaultValue = "1") final int page,
      @RequestParam(value = "size", defaultValue = "20") final int size,
      final Pageable pageable) {
    log.info("searchContacts - Init");
    final var response = contactFacade.search(PageableUtils.adjustPageable(pageable, page, size));
    final PageResponse<ContactFullDto> pageResponse = new PageResponse<>(response);
    return ResponseEntity.status(HttpStatus.OK).body(pageResponse);
  }

  @Operation(summary = "Search for Contact Preferences")
  @GetMapping(
      value = "/contact-preferences/search",
      params = {"page", "size"})
  public ResponseEntity<PageResponse<ContactPreferenceDto>> searchContactPreferences(
      @RequestParam(value = "page", defaultValue = "1") final int page,
      @RequestParam(value = "size", defaultValue = "20") final int size,
      final Pageable pageable) {
    log.info("searchContactPreferences - Init");
    final var response =
        contactPreferenceFacade.search(PageableUtils.adjustPageable(pageable, page, size));
    final PageResponse<ContactPreferenceDto> pageResponse = new PageResponse<>(response);
    return ResponseEntity.status(HttpStatus.OK).body(pageResponse);
  }
}
