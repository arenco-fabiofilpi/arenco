import 'package:arenco_app/data/data.dart';
import 'package:equatable/equatable.dart';

class NewPasswordTokenParams extends Equatable {
  final String? token;
  final String? username;
  final String? newPassord;

  const NewPasswordTokenParams({
    this.token,
    this.username,
    this.newPassord,
  });

  @override
  List<Object?> get props => [
        token,
        username,
        newPassord,
      ];

  NewPasswordTokenData toData() {
    return NewPasswordTokenData(
      token: token,
      username: username,
      newPassord: newPassord,
    );
  }
}
