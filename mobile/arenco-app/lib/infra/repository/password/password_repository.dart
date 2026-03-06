import 'package:arenco_app/data/data.dart';
import 'package:arenco_app/domain/domain.dart';
import 'package:arenco_app/infra/infra.dart';

class PasswordRepository extends ChopperClientHttp
    implements ConsultContactInterface {
  @override
  Future<ResponseReturnDataEntity> getConsultContact({
    required String username,
  }) async {
    try {
      final response = await getServiceAPI().getContactConsultRecoveryPassword(
        username: username,
      );

      final StatusData statusData =
          ResponseReturnDataEntity().getStatusDataWithReturnCode(
        statusCode: response.statusCode,
      );
      String message = ResponseReturnDataEntity().createMessageGeneric(
        statusData: statusData,
        message: response.error.toString(),
      );
      ContactsPasswordListParams? contactsPasswordListParams;
      if (statusData == StatusData.success) {
        contactsPasswordListParams = ContactsPasswordListParams.fromData(
          passwordRecoveryListData: PasswordRecoveryListData.fromJson(
            DecodeResponse().decode(response),
          ),
        );
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
        body: contactsPasswordListParams,
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
