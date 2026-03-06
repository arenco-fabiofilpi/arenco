import 'package:arenco_app/data/data.dart';
import 'package:equatable/equatable.dart';

class ContactsPasswordListParams extends Equatable {
  final List<ContactsPasswordParams>? contacts;

  const ContactsPasswordListParams({
    this.contacts,
  });

  @override
  List<Object?> get props => [
        contacts,
      ];

  factory ContactsPasswordListParams.fromData({
    required PasswordRecoveryListData passwordRecoveryListData,
  }) {
    final List<ContactsPasswordParams> contactsPasswordParams = [];

    passwordRecoveryListData.contacts
        ?.forEach((PasswordRecoveryData passwordRecoveryData) {
      contactsPasswordParams.add(
        ContactsPasswordParams(
          contactMethod: passwordRecoveryData.contactMethod,
          contactValue: passwordRecoveryData.contactValue,
          uuid: passwordRecoveryData.uuid,
        ),
      );
    });

    return ContactsPasswordListParams(
      contacts: contactsPasswordParams,
    );
  }
}

class ContactsPasswordParams extends Equatable {
  final String? uuid;
  final ContactMethod? contactMethod;
  final String? contactValue;

  const ContactsPasswordParams({
    this.uuid,
    this.contactMethod,
    this.contactValue,
  });

  @override
  List<Object?> get props => [
        uuid,
        contactMethod,
        contactValue,
      ];

  factory ContactsPasswordParams.fromData({
    required PasswordRecoveryData passwordRecoveryData,
  }) {
    return ContactsPasswordParams(
      uuid: passwordRecoveryData.uuid,
      contactMethod: passwordRecoveryData.contactMethod,
      contactValue: passwordRecoveryData.contactValue,
    );
  }
}
