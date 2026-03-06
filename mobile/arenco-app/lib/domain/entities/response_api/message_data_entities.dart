// ignore_for_file: must_be_immutable

import 'dart:math';

import 'package:arenco_app/data/data.dart';
import 'package:equatable/equatable.dart';

class MessageDataAPIEntity extends Equatable {
  String? errorMessage;
  String? code;

  MessageDataAPIEntity({
    this.errorMessage,
    this.code,
  });

  @override
  List<Object?> get props => [
        errorMessage,
        code,
      ];

  factory MessageDataAPIEntity.fromData({
    required ErrorResponseData errorResponseData,
  }) {
    return MessageDataAPIEntity(
      errorMessage: errorResponseData.message,
      code: errorResponseData.code,
    );
  }
}
