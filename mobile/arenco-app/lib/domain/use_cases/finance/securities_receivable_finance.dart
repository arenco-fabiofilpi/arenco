import 'package:arenco_app/domain/domain.dart';
import 'package:chopper/chopper.dart';

abstract class SecuritiesReceivableFinanceInterface {
  Future<ResponseReturnDataEntity> getSecuritiesReceivableList();

  Future<ResponseReturnDataEntity> getSecuritiesReceivableById();

  SecuritiesReceivableListEntity getSuccessResponseSecuritiesReceivableList({
    required Response<dynamic> response,
  });

  SecuritiesReceivableEntity getSuccessResponseSecuritiesReceivableById({
    required Response<dynamic> response,
  });
}
