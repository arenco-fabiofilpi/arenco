import 'package:json_annotation/json_annotation.dart';

part 'password_recovery_data.g.dart';

enum ContactMethod{
  cellphone,
  email,
}

@JsonSerializable(explicitToJson: true)
/// {
///
///       "contacts": List<PasswordRecoveryData>?,
///
///}
class PasswordRecoveryListData {
  List<PasswordRecoveryData>? contacts;

  PasswordRecoveryListData({
    this.contacts,
  });

  factory PasswordRecoveryListData.fromJson(Map<String, dynamic> json) =>
      _$PasswordRecoveryListDataFromJson(json);
  Map<String, dynamic> toJson() => _$PasswordRecoveryListDataToJson(this);
}

@JsonSerializable(explicitToJson: true)
/// {
///
///       "uuid": String,
///       "contactMethod": ContactMethod
///       "contactValue": String
///
///}
class PasswordRecoveryData {
  String? uuid;
  @JsonKey(fromJson: _contactMethodConvert)
  ContactMethod? contactMethod;
  String? contactValue;

  PasswordRecoveryData({
    this.uuid,
    this.contactMethod,
    this.contactValue,
  });

  static ContactMethod? _contactMethodConvert(String contactMethod){
    if(contactMethod == "CELLPHONE"){
      return ContactMethod.cellphone;
    }
    if(contactMethod == "EMAIL"){
      return ContactMethod.email;
    }
    return null;
  } 

  factory PasswordRecoveryData.fromJson(Map<String, dynamic> json) =>
      _$PasswordRecoveryDataFromJson(json);
  Map<String, dynamic> toJson() => _$PasswordRecoveryDataToJson(this);
}
