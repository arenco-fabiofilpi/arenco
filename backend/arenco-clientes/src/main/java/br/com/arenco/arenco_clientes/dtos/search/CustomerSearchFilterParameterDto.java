package br.com.arenco.arenco_clientes.dtos.search;

import br.com.arenco.arenco_clientes.enums.CustomerSearchKey;
import br.com.arenco.arenco_clientes.enums.SearchOperator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerSearchFilterParameterDto {

  @JsonProperty(required = true)
  private CustomerSearchKey key;

  private String value;

  @JsonProperty(required = true)
  private SearchOperator operator;
}
