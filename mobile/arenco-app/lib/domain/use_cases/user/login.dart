import 'package:arenco_app/domain/domain.dart';

abstract class LoginInterface {
  Future<ResponseReturnDataEntity> loginRequest({
    required LoginParams loginParams,
  });

  Future<ResponseReturnDataEntity> refreshToken();
}
