import 'package:json_annotation/json_annotation.dart';
part 'new_password_token_data.g.dart';

@JsonSerializable()
class NewPasswordTokenData {
  String? username;
  String? token;
  String? newPassord;

  NewPasswordTokenData({
    this.newPassord,
    this.token,
    this.username,
  });

  factory NewPasswordTokenData.fromJson(Map<String, dynamic> json) =>
      _$NewPasswordTokenDataFromJson(json);
  Map<String, dynamic> toJson() => _$NewPasswordTokenDataToJson(this);
}
