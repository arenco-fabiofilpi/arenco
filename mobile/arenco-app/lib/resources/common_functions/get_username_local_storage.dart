import 'package:arenco_app/domain/domain.dart';
import 'package:arenco_app/infra/infra.dart';

class GetUsernameLocalStorage implements GetUserDataInterface {

  @override
  Future<LoginParams> getUserData() async {
    final LocalStorageService localStorageService = LocalStorageService();
    return LoginParams(
      username: await localStorageService.read(
        key: LoginStorage().keyUsername ?? "",
      ),
      password: await localStorageService.read(
        key: LoginStorage().keyPassword ?? "",
      ),
    );
  }
}
