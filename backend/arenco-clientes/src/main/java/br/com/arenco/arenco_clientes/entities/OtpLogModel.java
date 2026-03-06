package br.com.arenco.arenco_clientes.entities;

import br.com.arenco.arenco_clientes.enums.OtpType;
import java.time.Instant;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Setter
@Getter
@Document
public class OtpLogModel {
  @Id private String id;
  @CreatedDate private Instant dateCreated;
  @LastModifiedDate private Instant lastUpdated;

  private OtpType type;

  private String userModelId;

  private String deliveredTo;

  private String ipAddress;

  private String userAgent;

  @Indexed(expireAfter = "0s") // TTL ativo baseado na data do campo
  private Instant expiresAt;
}
