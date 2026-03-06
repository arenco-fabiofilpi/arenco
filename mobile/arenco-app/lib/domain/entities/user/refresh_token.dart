// ignore_for_file: must_be_immutable

import 'package:arenco_app/data/data.dart';
import 'package:equatable/equatable.dart';

class RefreshTokenParams extends Equatable {
  String? refreshToken;

  RefreshTokenParams({this.refreshToken});

  @override
  List<Object?> get props => [refreshToken];

  RefreshTokenModel toData() => RefreshTokenModel(
        refreshToken: refreshToken,
      );
}
