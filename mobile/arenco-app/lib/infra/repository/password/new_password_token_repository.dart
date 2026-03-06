import 'dart:convert';

import 'package:arenco_app/data/data.dart';
import 'package:arenco_app/domain/domain.dart';
import 'package:arenco_app/infra/infra.dart';

class NewPasswordTokenRepository extends ChopperClientHttp
    implements NewPasswordTokenInterface {
  @override
  Future<ResponseReturnDataEntity> changePasswordWithToken({
    required NewPasswordTokenParams newPassowrdTokenParams,
  }) async {
    try {
      final response = await getServiceAPI().newPasswordWithToken(
        body: json.encode(
          newPassowrdTokenParams.toData().toJson(),
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
      if (statusData != StatusData.success) {
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
        messageData: MessageDataAPIEntity(
          errorMessage: message,
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
}
