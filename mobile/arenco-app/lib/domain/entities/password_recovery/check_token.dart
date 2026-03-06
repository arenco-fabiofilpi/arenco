import 'package:arenco_app/data/data.dart';
import 'package:equatable/equatable.dart';

class CheckTokenParams extends Equatable {
  final String? token;
  final String? username;

  const CheckTokenParams({
    this.token,
    this.username,
  });

  @override
  List<Object?> get props => [
        token,
        username,
      ];

  CheckTokenData toData() {
    return CheckTokenData(
      token: token,
      username: username,
    );
  }
}
