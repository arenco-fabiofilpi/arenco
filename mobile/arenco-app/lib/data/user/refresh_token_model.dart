import 'package:json_annotation/json_annotation.dart';

part 'refresh_token_model.g.dart';

@JsonSerializable(explicitToJson: true)

/// {
///
///       "refreshToken": "string"
///
///}
class RefreshTokenModel {
  String? refreshToken;


  RefreshTokenModel({
    this.refreshToken,
  });

  factory RefreshTokenModel.fromJson(Map<String, dynamic> json) =>
      _$RefreshTokenModelFromJson(json);


  Map<String, dynamic> toJson() => _$RefreshTokenModelToJson(this);
}
