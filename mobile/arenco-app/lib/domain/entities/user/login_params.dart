import 'package:arenco_app/data/data.dart';
import 'package:equatable/equatable.dart';

class LoginParams extends Equatable {
  final String? username;
  final String? password;
  final bool removePreviousSessions;

  const LoginParams({
    this.password,
    this.username,
    this.removePreviousSessions = true,
  });

  @override
  List<Object?> get props => [
        username,
        password,
        removePreviousSessions,
      ];

  LoginModel toData() => LoginModel(
        password: password,
        username: username,
      );
}
