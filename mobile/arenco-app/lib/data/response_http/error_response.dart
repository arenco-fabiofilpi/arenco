import 'package:json_annotation/json_annotation.dart';

part 'error_response.g.dart';

@JsonSerializable(explicitToJson: true)

/// {
///
///       "code": "string",
///       "message": "string"
///
///}
class ErrorResponseData {
  String? code;
  String? message;

  ErrorResponseData({
    this.code,
    this.message,
  });

  factory ErrorResponseData.fromJson(Map<String, dynamic> json) =>
      _$ErrorResponseDataFromJson(json);


  Map<String, dynamic> toJson() => _$ErrorResponseDataToJson(this);
}
