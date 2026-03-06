package br.com.arenco.arenco_clientes.dtos.search;

import br.com.arenco.arenco_clientes.enums.EmployeeSearchKey;
import br.com.arenco.arenco_clientes.enums.SearchOperator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeSearchFilterParameterDto {

  @JsonProperty(required = true)
  private EmployeeSearchKey key;

  private String value;

  @JsonProperty(required = true)
  private SearchOperator operator;
}
