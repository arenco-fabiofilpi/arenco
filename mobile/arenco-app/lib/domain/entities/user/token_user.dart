// ignore_for_file: must_be_immutable
import 'package:equatable/equatable.dart';

class TokenUserParams extends Equatable {
  String? token;

  TokenUserParams({this.token});

  @override
  List<Object?> get props => [token];
}
