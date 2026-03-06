import 'package:arenco_app/domain/domain.dart';

abstract class NewPasswordTokenInterface {
  Future<ResponseReturnDataEntity> changePasswordWithToken({
    required NewPasswordTokenParams newPassowrdTokenParams,
  });
}
