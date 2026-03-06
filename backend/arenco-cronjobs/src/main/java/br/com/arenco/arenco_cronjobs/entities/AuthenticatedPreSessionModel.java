package br.com.arenco.arenco_cronjobs.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Document
@RequiredArgsConstructor
public class AuthenticatedPreSessionModel {
    @Id
    private final String hashedId;
    private final String ip;
    private final String userAgent;
    private final String userId;
    private final Instant createdAt;

    @Indexed(expireAfter = "0s") // TTL ativo baseado na data do campo
    private final Instant expiresAt;
}
