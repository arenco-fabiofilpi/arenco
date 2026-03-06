package br.com.arenco.arenco_clientes.dtos.search;

import br.com.arenco.arenco_clientes.enums.CustomerSortKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerSearchFormDto {

  @JsonPropertyDescription("DTO de Filtros")
  @Valid
  private CustomerSearchFilterDto filterDto;

  @JsonPropertyDescription("Sort direction. Possible values are: ASC or DESC")
  @JsonProperty(defaultValue = "DESC")
  private Sort.Direction sortDirection = Sort.Direction.DESC;

  @JsonPropertyDescription("Sort key")
  @JsonProperty(defaultValue = "updatedAt")
  private CustomerSortKey sortKey = CustomerSortKey.ID_ERP;
}
