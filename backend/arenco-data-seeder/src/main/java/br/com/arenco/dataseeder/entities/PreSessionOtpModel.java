package br.com.arenco.dataseeder.entities;

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
public class PreSessionOtpModel {
  @Id private String id;
  @CreatedDate private Instant dateCreated;
  @LastModifiedDate private Instant lastUpdated;

  private String userModelId;

  private String deliveredTo;

  private String token;

  private String ipAddress;

  private String userAgent;

  @Indexed(unique = true)
  private String preSessionId;

  private int attempts = 0;

  @Indexed(expireAfter = "0s") // TTL ativo baseado na data do campo
  private Instant expiresAt;
}
