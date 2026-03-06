import 'package:arenco_app/infra/infra.dart';
import 'package:equatable/equatable.dart';

// ignore: must_be_immutable
class TokenUserLocalStorage extends Equatable {
  String? keyLoginData;
  final String? loginData;

  TokenUserLocalStorage({
    this.keyLoginData,
    this.loginData,
  }) {
    keyLoginData = KeyLocalStorage.loginData();
  }

  @override
  List<Object?> get props => [
        keyLoginData,
        loginData,
      ];
}
