package br.com.arenco.arenco_clientes.factories;

import br.com.arenco.arenco_clientes.dtos.user.ContactDto;
import br.com.arenco.arenco_clientes.entities.ContactModel;
import br.com.arenco.arenco_clientes.enums.ContactType;

public record ContactDtoFactory(ContactModel model, boolean maskValue) {
  public ContactDto create() {
    final String contactValue;
    if (maskValue) {
      contactValue = maskContactValue(model.getValue(), model.getContactType());
    } else {
      contactValue = model.getValue();
    }
    return new ContactDto(
            model.getId(), model.getContactType().toString(), contactValue);
  }

  public static String maskContactValue(final String value, final ContactType method) {
    if ((method == ContactType.CELLPHONE || method == ContactType.TELEPHONE) && value != null) {
      return value.substring(0, 6) + "XXXXX" + value.substring(value.length() - 2);
    }

    if (method == ContactType.EMAIL && value != null && value.contains("@")) {
      final String[] parts = value.split("@");
      final String name = parts[0];
      final String domain = parts[1];
      return name.charAt(0) + "*****" + name.charAt(name.length() - 1) + "@" + domain;
    }

    return value;
  }
}
