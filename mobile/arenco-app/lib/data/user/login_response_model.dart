// ignore_for_file: constant_identifier_names

import 'package:arenco_app/data/data.dart';
import 'package:arenco_app/domain/entities/entities.dart';
import 'package:json_annotation/json_annotation.dart';

part 'login_response_model.g.dart';

enum LoginMethod {
  TWO_FACTOR_AUTHENTICATION,
  USERNAME_AND_PASSWORD,
}

@JsonSerializable(explicitToJson: true)

///   {
///   "access_token": "string",
///   "expires_in": 0,
///   "refresh_token": "string",
///   "refresh_token_expires_in": 0,
///   "pre_access_token": "string",
///   "contact_methods": [
///     {
///       "uuid": "string",
///       "contactMethod": "CELLPHONE",
///       "contactValue": "string"
///     }
///   ],
///   "message": "string",
///   "login_method": "TWO_FACTOR_AUTHENTICATION"
/// }
class LoginResponseModel {
  @JsonKey(name: "access_token")
  String? accessToken;
  @JsonKey(name: "expires_in")
  int? expiresIn;
  @JsonKey(name: "refresh_token")
  String? refreshToken;
  @JsonKey(name: "refresh_token_expires_in")
  int? refreshTokenExpiryDate;
  String? message;
  @JsonKey(name: "login_method", fromJson: _fromJsonLoginMethod)
  LoginMethod? loginMethod;
  @JsonKey(name: "contact_methods")
  List<PasswordRecoveryData>? contactMethod;

  static LoginMethod? _fromJsonLoginMethod(String loginMethod) {
    if (loginMethod == "TWO_FACTOR_AUTHENTICATION") {
      return LoginMethod.TWO_FACTOR_AUTHENTICATION;
    }
    if (loginMethod == "USERNAME_AND_PASSWORD") {
      return LoginMethod.USERNAME_AND_PASSWORD;
    }
    return null;
  }

  LoginResponseModel({
    this.accessToken,
    this.refreshToken,
    this.refreshTokenExpiryDate,
    this.expiresIn,
    this.loginMethod,
    this.message,
  });

  factory LoginResponseModel.fromJson(Map<String, dynamic> json) =>
      _$LoginResponseModelFromJson(json);

  ContactsPasswordListParams _tryParseContactsPassword({
    required List<PasswordRecoveryData>? contactMethod,
  }) {
    final List<ContactsPasswordParams> contacts = [];

    contactMethod?.forEach(
      (value) {
        contacts.add(
          ContactsPasswordParams(
            contactMethod: value.contactMethod,
            contactValue: value.contactValue,
            uuid: value.uuid,
          ),
        );
      },
    );

    return ContactsPasswordListParams(
      contacts: contacts,
    );
  }

  LoginMethodParams? _tryParseLoginMethods({
    required LoginMethod? loginMethod,
  }) {
    if (loginMethod == LoginMethod.TWO_FACTOR_AUTHENTICATION) {
      return LoginMethodParams.TWO_FACTOR_AUTHENTICATION;
    }
    if (loginMethod == LoginMethod.USERNAME_AND_PASSWORD) {
      return LoginMethodParams.USERNAME_AND_PASSWORD;
    }
    return LoginMethodParams.TWO_FACTOR_AUTHENTICATION;
  }

  LoginResponseParams toEntity() => LoginResponseParams(
        accessToken: accessToken,
        contactMethod: _tryParseContactsPassword(contactMethod: contactMethod),
        expiresIn: expiresIn,
        loginMethod: _tryParseLoginMethods(
          loginMethod: loginMethod,
        ),
        message: message,
        refreshToken: refreshToken,
        refreshTokenExpiryDate: refreshTokenExpiryDate,
      );

  Map<String, dynamic> toJson() => _$LoginResponseModelToJson(this);
}
