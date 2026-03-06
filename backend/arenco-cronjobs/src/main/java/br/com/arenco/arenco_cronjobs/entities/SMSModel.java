package br.com.arenco.arenco_cronjobs.entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Currency;

@Data
@Setter
@Getter
@Document
public class SMSModel {
  @Id private String id;
  @CreatedDate private Instant dateCreated;
  @LastModifiedDate private Instant lastUpdated;

  private String from;
  private String data;
  private String deliveredTo;
  private String provider;
  private String providerSid;
  private String status;
  private Integer errorCode;
  private String errorMessage;
  private String segments;
  private String price;
  private Currency priceUnit;
  private ZonedDateTime dateSent;
}
