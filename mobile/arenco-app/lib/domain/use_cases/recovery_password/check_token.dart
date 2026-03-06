import 'package:arenco_app/domain/domain.dart';

abstract class CheckTokenInterface {
  Future<ResponseReturnDataEntity> checkToken({
    required CheckTokenParams checkTokenParams,
  });
}
