package br.com.arenco.arenco_clientes.entities;

import br.com.arenco.arenco_clientes.enums.JobType;
import br.com.arenco.arenco_clientes.enums.SyncStatus;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document
public class JobInfoModel {
  @Id private String id;
  @CreatedDate private Instant dateCreated;
  @LastModifiedDate private Instant lastUpdated;
  @Indexed(expireAfter = "0s") // TTL ativo baseado na data do campo
  private Instant expiresAt;
  private JobType type;
  private SyncStatus status;
  private Instant startedAt;
  private Instant finishedAt; // pode ser null enquanto roda
  private String message; // opcional: msg de erro/ok
}
