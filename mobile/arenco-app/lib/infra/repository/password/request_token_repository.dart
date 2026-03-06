import 'dart:convert';

import 'package:arenco_app/data/data.dart';
import 'package:arenco_app/domain/domain.dart';
import 'package:arenco_app/infra/infra.dart';

class RequestTokenRepository extends ChopperClientHttp
    implements RequestTokenInterface {
  @override
  Future<ResponseReturnDataEntity> requestToken({
    required RequestTokenParams requestTokenParams,
  }) async {
    try {
      final response = await getServiceAPI().resquestToken(
        body: json.encode(
          requestTokenParams.toData().toJson(),
        ),
      );

      final StatusData statusData =
          ResponseReturnDataEntity().getStatusDataWithReturnCode(
        statusCode: response.statusCode,
      );
      String message = ResponseReturnDataEntity().createMessageGeneric(
        statusData: statusData,
        message: response.error.toString(),
      );
      ResponseTokenParams? responseTokenParams;
      if (statusData == StatusData.success) {
        responseTokenParams =
            ResponseTokenDataModel.fromJson(DecodeResponse().decode(response))
                .toEntity();
      } else {
        message = MessageDataAPIEntity.fromData(
              errorResponseData: ErrorResponseData.fromJson(
                DecodeResponse().decode(response),
              ),
            ).errorMessage ??
            "";
      }
      return ResponseReturnDataEntity(
        isSuccessful: response.isSuccessful,
        statusData: statusData,
        body: responseTokenParams,
        messageData: MessageDataAPIEntity(
          errorMessage: message,
        ),
      );
    } catch (e) {
      return ResponseReturnDataEntity(
        statusData: StatusData.serverError,
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
}
