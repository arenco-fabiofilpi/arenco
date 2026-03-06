package br.com.arenco.arenco_clientes.entities;

import java.time.Instant;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Setter
@Getter
@Document
public class EmailModel {
  @Id private String id;
  @CreatedDate private Instant dateCreated;
  @LastModifiedDate private Instant lastUpdated;

  private String data;
  private String from;
  private String subject;
  private String messageId;
  private String templateId;
  private String deliveredTo;
  private Integer statusCode;
  private String responseBody;
  private String responseHeaders;
}
