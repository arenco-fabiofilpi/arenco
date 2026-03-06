import 'package:arenco_app/domain/domain.dart';

abstract class TokenUserLocalStorageInterface {
  void setTokenLocalStorage({required TokenUserLocalStorage token});

  void setLoginLocalStorage({required LoginStorage token});
}
