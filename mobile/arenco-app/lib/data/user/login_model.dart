import 'package:arenco_app/domain/entities/entities.dart';
import 'package:json_annotation/json_annotation.dart';

part 'login_model.g.dart';

@JsonSerializable(explicitToJson: true)

/// {
///
///       "username": "string",
///       "password": "string"
///       "removePreviousSessions": true
///
///}
class LoginModel {
  String? username;
  String? password;
  bool removePreviousSessions;

  LoginModel({
    this.password,
    this.username,
    this.removePreviousSessions = true,
  });

  factory LoginModel.fromJson(Map<String, dynamic> json) =>
      _$LoginModelFromJson(json);

  factory LoginModel.fromDomain(LoginParams params) => LoginModel(
        password: params.password,
        username: params.username,
        removePreviousSessions: params.removePreviousSessions,
      );

  Map<String, dynamic> toJson() => _$LoginModelToJson(this);
}
