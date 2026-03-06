import 'package:json_annotation/json_annotation.dart';

part 'check_token_data.g.dart';

@JsonSerializable()
class CheckTokenData {
  String? username;
  String? token;

  CheckTokenData({
    this.token,
    this.username,
  });

  factory CheckTokenData.fromJson(Map<String, dynamic> json) =>
      _$CheckTokenDataFromJson(json);
  Map<String, dynamic> toJson() => _$CheckTokenDataToJson(this);
}
