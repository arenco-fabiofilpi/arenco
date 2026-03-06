import 'package:arenco_app/data/data.dart';
import 'package:arenco_app/domain/domain.dart';
import 'package:arenco_app/infra/infra.dart';
import 'package:chopper/chopper.dart';

class FinanceRepository extends ChopperClientHttp
    implements SecuritiesReceivableFinanceInterface {
  @override
  Future<ResponseReturnDataEntity> getSecuritiesReceivableById() {
    // TODO: implement getSecuritiesReceivableById
    throw UnimplementedError();
  }

  @override
  Future<ResponseReturnDataEntity> getSecuritiesReceivableList() async {
    try {
      final response = await getFinanceAPI().securitiesReceivableList();
      SecuritiesReceivableListEntity? securitiesReceivableListEntity;

      final StatusData statusData =
          ResponseReturnDataEntity().getStatusDataWithReturnCode(
        statusCode: response.statusCode,
      );

      if (statusData == StatusData.success) {
        securitiesReceivableListEntity =
            getSuccessResponseSecuritiesReceivableList(response: response);
      }

      return ResponseReturnDataEntity(
        body: response.body.toString(),
        isSuccessful: response.isSuccessful,
        statusData: statusData,
        messageData: MessageDataAPIEntity(
          errorMessage: response.base.request?.url.toString(),
        ),
      );
    } catch (e) {
      return ResponseReturnDataEntity(
        statusData: StatusData.systemError,
        isSuccessful: false,
        messageData: MessageDataAPIEntity(
          errorMessage: ResponseReturnDataEntity().createMessageGeneric(
            statusData: StatusData.systemError,
            message: e.toString(),
          ),
        ),
      );
    }
  }

  @override
  SecuritiesReceivableListEntity getSuccessResponseSecuritiesReceivableList({
    required Response<dynamic> response,
  }) {
    return SecuritiesReceivableFinanceListData.fromJson(
      DecodeResponse().decode(response),
    ).toEntity();
  }

  @override
  SecuritiesReceivableEntity getSuccessResponseSecuritiesReceivableById({
    required Response response,
  }) {
    // TODO: implement getSuccessResponseSecuritiesReceivableById
    throw UnimplementedError();
  }
}
