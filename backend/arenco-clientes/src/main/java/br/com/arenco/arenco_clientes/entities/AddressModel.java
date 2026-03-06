package br.com.arenco.arenco_clientes.entities;

import java.time.Instant;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Setter
@Getter
@Document
@ToString
public class AddressModel {
  @Id private String id;
  @CreatedDate private Instant dateCreated;
  @LastModifiedDate private Instant lastUpdated;

  private String userId;

  private String streetName;

  private String streetNumber;

  private String cep;

  private String district;

  private String city;

  private String state;

  private String country;

  private boolean disabled = false;
}
