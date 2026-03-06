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
public class PasswordRecoveryModel {
  @Id private String id;
  @CreatedDate private Instant dateCreated;
  @LastModifiedDate private Instant lastUpdated;

  private String ip;

  private String userAgent;

  private String userId;

  private String otpId;

  @Indexed(expireAfter = "0s") // TTL ativo baseado na data do campo
  private Instant expiresAt;
}
