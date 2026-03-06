import 'package:arenco_app/data/password_recovery/password_recovery.dart';
import 'package:arenco_app/domain/domain.dart';
import 'package:equatable/equatable.dart';

class RequestTokenParams extends Equatable {
  final String? username;
  final String? contactUid;

  const RequestTokenParams({
    this.username,
    this.contactUid,
  });

  @override
  List<Object?> get props => [
        username,
        contactUid,
      ];

  RequestTokenDataModel toData() {
    return RequestTokenDataModel(
      username: username,
      contactUid: contactUid,
    );
  }
}

class ResponseTokenParams extends Equatable {
  final StatusData? result;
  final ContactsPasswordParams? userContactDto;
  final String? message;

  const ResponseTokenParams({
    this.message,
    this.userContactDto,
    this.result,
  });

  @override
  List<Object?> get props => [
        result,
        userContactDto,
        message,
      ];

  ResponseTokenParams fromData({
    required ResponseTokenDataModel responseTokenDataModel,
  }) {
    return ResponseTokenParams(
      message: responseTokenDataModel.message,
      result: responseTokenDataModel.result,
      userContactDto: ContactsPasswordParams.fromData(
        passwordRecoveryData:
            responseTokenDataModel.userContactDto ?? PasswordRecoveryData(),
      ),
    );
  }
}
