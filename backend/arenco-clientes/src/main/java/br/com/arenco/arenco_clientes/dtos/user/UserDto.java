package br.com.arenco.arenco_clientes.dtos.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
  private String id;
  private String createdAt;
  private String updatedAt;
  private String name;
  private String enabled;
  private String loginMethod;

  private int idErp;
  private String cpf;
  private String cnpj;

  private String grupoContabil;

  private String username;
}
