import 'package:arenco_app/domain/domain.dart';

abstract class ReadTokenInterface {
  Future<RefreshTokenParams> getRefreshTokenLocalStorage();

  Future<TokenUserParams> getTokenLocalStorage();
}
