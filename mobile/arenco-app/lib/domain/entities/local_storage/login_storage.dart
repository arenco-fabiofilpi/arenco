import 'package:arenco_app/infra/infra.dart';
import 'package:equatable/equatable.dart';

// ignore: must_be_immutable
class LoginStorage extends Equatable {
  String? keyUsername;
  final String? username;

  LoginStorage({
    this.keyUsername,
    this.username,
  }) {
    keyUsername = KeyLocalStorage.keyUsername();
  }

  @override
  List<Object?> get props => [
        keyUsername,
        username,
      ];
}
