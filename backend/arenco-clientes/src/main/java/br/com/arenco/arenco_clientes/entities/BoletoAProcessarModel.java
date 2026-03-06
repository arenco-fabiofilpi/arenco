package br.com.arenco.arenco_clientes.entities;

import br.com.arenco.arenco_clientes.enums.TipoProcessamentoBoleto;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

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
