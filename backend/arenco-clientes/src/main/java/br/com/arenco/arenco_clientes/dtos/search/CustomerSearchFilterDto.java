package br.com.arenco.arenco_clientes.dtos.search;

import br.com.arenco.arenco_clientes.enums.JoinFiltersOperator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerSearchFilterDto {

  @JsonPropertyDescription("Filtros")
  @Valid
  private List<CustomerSearchFilterParameterDto> filters;

  @JsonPropertyDescription("Modo de combinação para mais de um filtro")
  @JsonProperty(defaultValue = "AND")
  private JoinFiltersOperator joinFiltersOperator = JoinFiltersOperator.AND;
}
