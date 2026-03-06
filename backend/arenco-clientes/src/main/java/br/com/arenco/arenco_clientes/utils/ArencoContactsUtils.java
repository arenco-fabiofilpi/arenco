package br.com.arenco.arenco_clientes.utils;

import br.com.arenco.arenco_clientes.dtos.user.ContactDto;
import br.com.arenco.arenco_clientes.enums.ContactType;

public enum ArencoContactsUtils {
  ;

  public static ContactType getContactMethodEnum(final ContactDto contactDto) {
    if (contactDto == null || contactDto.contactValue() == null) {
      throw new IllegalArgumentException("ContactDto is null or empty");
    }

    final String contact = contactDto.contactValue();

    if (contact.matches("\\+\\d{13,15}")) { // Exemplo: +5516999492929
      return ContactType.CELLPHONE;
    } else if (contact.contains("@")) { // Exemplo: email@email.com
      return ContactType.EMAIL;
    }
    return ContactType.TELEPHONE;
  }
}
