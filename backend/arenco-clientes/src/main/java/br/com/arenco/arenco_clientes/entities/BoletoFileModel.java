package br.com.arenco.arenco_clientes.entities;

import br.com.arenco.arenco_clientes.enums.FileType;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document
@CompoundIndex(
    name = "idx_receivableTitle_formato",
    def = "{'receivableTitleId' : 1, 'fileType': 1}")
public class BoletoFileModel {
  @Id private String id;
  @CreatedDate private Instant dateCreated;
  @LastModifiedDate private Instant lastUpdated;

  private String receivableTitleId;

  private FileType fileType;
}
