import 'package:arenco_app/domain/domain.dart';

abstract class RequestTokenInterface{

  Future<ResponseReturnDataEntity> requestToken({
    required RequestTokenParams requestTokenParams,
  });

}
