List<RegExp> getListRegex() {
  return [
    RegExp(r'^.{8,}$'),
    RegExp(r'^(?=.*[A-Z]).*$'),
    RegExp(r'^(?=.*[a-z]).*$'),
    RegExp(r'^(?=.*\d).*$'),
    RegExp(r'^(?=.*[!@#$%^&*(),.?":{}|<>]).*$'),
  ];
}

bool checkAllValidate({required String valueToValidate}) {
  final listRegex = getListRegex();

  for (final RegExp regex in listRegex) {
    if (!regex.hasMatch(valueToValidate)) {
      return false;
    }
  }
  return true;
}

String formatPhoneNumber(String phoneNumber) {
  // Adiciona espaços e hífens ao número de telefone, incluindo o código do país
  final String formattedPhoneNumber = phoneNumber
      .replaceAllMapped(RegExp(r'(\+\d{2})(\d{2})(\w{2,})(\d{2,})'), (match) {
    return '${match.group(1)} ${match.group(2)} ${match.group(3)}-${match.group(4)}';
  });

  // Retorna o número formatado com espaços e hífens
  return formattedPhoneNumber;
}
