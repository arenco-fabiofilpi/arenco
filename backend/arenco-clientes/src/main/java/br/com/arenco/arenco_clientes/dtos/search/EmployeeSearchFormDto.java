package br.com.arenco.arenco_clientes.dtos.search;

import br.com.arenco.arenco_clientes.enums.EmployeeSortKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.data.domain.Sort;

@Data
public class EmployeeSearchFormDto {

  @JsonPropertyDescription("DTO de Filtros")
  @Valid
  private EmployeeSearchFilterDto filterDto;

  @JsonPropertyDescription("Sort direction. Possible values are: ASC or DESC")
  @JsonProperty(defaultValue = "DESC")
  private Sort.Direction sortDirection = Sort.Direction.DESC;

  @JsonPropertyDescription("Sort key")
  @JsonProperty(defaultValue = "updatedAt")
  private EmployeeSortKey sortKey = EmployeeSortKey.UPDATED_AT;
}
