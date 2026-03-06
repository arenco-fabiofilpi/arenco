// ignore_for_file: constant_identifier_names

import 'package:arenco_app/domain/domain.dart';
import 'package:equatable/equatable.dart';

enum LoginMethodParams {
  TWO_FACTOR_AUTHENTICATION,
  USERNAME_AND_PASSWORD,
}

class LoginResponseParams extends Equatable {
  final String? accessToken;
  final int? expiresIn;
  final String? refreshToken;
  final int? refreshTokenExpiryDate;
  final String? message;
  final LoginMethodParams? loginMethod;
  final ContactsPasswordListParams? contactMethod;

  const LoginResponseParams({
    this.accessToken,
    this.contactMethod,
    this.expiresIn,
    this.loginMethod,
    this.message,
    this.refreshToken,
    this.refreshTokenExpiryDate,
  });

  @override
  List<Object?> get props => [
        accessToken,
        contactMethod,
        expiresIn,
        loginMethod,
        message,
        refreshToken,
        refreshTokenExpiryDate,
      ];
}
