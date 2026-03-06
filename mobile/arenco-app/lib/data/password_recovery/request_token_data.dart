import 'package:arenco_app/data/data.dart';
import 'package:arenco_app/domain/domain.dart';
import 'package:json_annotation/json_annotation.dart';
part 'request_token_data.g.dart';

@JsonSerializable()
class RequestTokenDataModel {
  String? username;
  String? contactUid;

  RequestTokenDataModel({
    this.username,
    this.contactUid,
  });

  factory RequestTokenDataModel.fromJson(Map<String, dynamic> json) =>
      _$RequestTokenDataModelFromJson(json);
  Map<String, dynamic> toJson() => _$RequestTokenDataModelToJson(this);
}

@JsonSerializable()
class ResponseTokenDataModel {
  @JsonKey(fromJson: _contactMethodConvert)
  StatusData? result;
  PasswordRecoveryData? userContactDto;
  String? message;

  ResponseTokenDataModel({
    this.result,
    this.userContactDto,
    this.message,
  });

  static StatusData? _contactMethodConvert(String result) {
    if (result == "SUCCESS") {
      return StatusData.success;
    }
    if (result == "ERROR") {
      return StatusData.badRequest;
    }
    return null;
  }

  ResponseTokenParams toEntity() => ResponseTokenParams(
        message: message,
        result: result,
        userContactDto: ContactsPasswordParams(
          contactMethod: userContactDto?.contactMethod,
          contactValue: userContactDto?.contactValue,
          uuid: userContactDto?.uuid,
        ),
      );

  factory ResponseTokenDataModel.fromJson(Map<String, dynamic> json) =>
      _$ResponseTokenDataModelFromJson(json);
  Map<String, dynamic> toJson() => _$ResponseTokenDataModelToJson(this);
}
