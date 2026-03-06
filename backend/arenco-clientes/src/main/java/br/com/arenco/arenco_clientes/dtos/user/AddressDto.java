package br.com.arenco.arenco_clientes.dtos.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDto {
  private String createdAt;

  private String updatedAt;

  private String userFullName;

  private int ownerIdErp;

  private String streetName;

  private String streetNumber;

  private String cep;

  private String district;

  private String city;

  private String state;

  private String country;
}
