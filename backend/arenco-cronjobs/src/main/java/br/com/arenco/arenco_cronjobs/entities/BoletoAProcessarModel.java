package br.com.arenco.arenco_cronjobs.entities;

import br.com.arenco.arenco_cronjobs.enums.TipoProcessamentoBoleto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Setter
@Getter
@Document
public class BoletoAProcessarModel {
    @Id
    private String id;
    @CreatedDate
    private Instant dateCreated;
    @LastModifiedDate
    private Instant lastUpdated;

    private String receivableTitleId;

    private TipoProcessamentoBoleto tipoProcessamentoBoleto;
}
