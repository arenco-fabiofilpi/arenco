package br.com.arenco.arenco_clientes.dtos.user;

import br.com.arenco.arenco_clientes.enums.ContactType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactPreferenceDto {
    private String createdAt;

    private String updatedAt;

    private String userFullName;

    private ContactType contactType;

    private String value;

    private boolean active;

    private int ownerIdErp;

}
