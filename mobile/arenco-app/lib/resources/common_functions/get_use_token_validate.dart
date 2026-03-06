import 'package:arenco_app/domain/domain.dart';

class GetUseTokenValidate implements InterceptorsValidate {
  @override
  bool getUseTokenUser({required String path}) {
    return !RegExp(r'\/auth\/token|\/auth\/reset-password\/password-recovery"')
        .hasMatch(path);
  }
}
