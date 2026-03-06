package br.com.arenco.arenco_clientes.entities;

import java.time.Instant;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Setter
@Getter
@Document
@CompoundIndex(
    name = "unique_user_active",
    def = "{'userModelId': 1, 'active': 1}",
    unique = true,
    partialFilter = "{ active: true }")
public class ContactPreferenceModel {
  @Id private String id;
  @CreatedDate private Instant dateCreated;
  @LastModifiedDate private Instant lastUpdated;

  private String userModelId;

  private String contactModelId;

  private boolean active = true;
}
