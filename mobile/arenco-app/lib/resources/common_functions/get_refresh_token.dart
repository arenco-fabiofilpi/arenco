import 'package:arenco_app/domain/domain.dart';
import 'package:arenco_app/infra/infra.dart';

class GetRefreshToken implements ReadTokenInterface {
  @override
  Future<RefreshTokenParams> getRefreshTokenLocalStorage() async {
    final LocalStorageService localStorageService = LocalStorageService();
    return RefreshTokenParams(
      refreshToken: await localStorageService.read(
        key: TokenUserLocalStorage().keyRefreshToken ?? "",
      ),
    );
  }
  
  @override
  Future<TokenUserParams> getTokenLocalStorage()  async {
    final LocalStorageService localStorageService = LocalStorageService();
    return TokenUserParams(
      token: await localStorageService.read(
        key: TokenUserLocalStorage().keyAccessToken ?? "",
      ),
    );
  }
}
