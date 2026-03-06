package br.com.arenco.dataseeder.entities;

import java.time.Instant;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document("sessions")
@RequiredArgsConstructor
public class SessionModel {
  @Id private final String hashedId;
  private final String ip;
  private final String userId;
  private final String userAgent;
  private final Instant createdAt;

  @Indexed(expireAfter = "0s") // TTL ativo baseado na data do campo
  private final Instant expiresAt;
}
