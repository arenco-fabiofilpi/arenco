import 'dart:convert';
import 'package:arenco_app/data/data.dart';
import 'package:arenco_app/domain/domain.dart';
import 'package:arenco_app/infra/infra.dart';
import 'package:arenco_app/resources/resources.dart';

class LoginRepository extends ChopperClientHttp
    implements LoginInterface, TokenUserLocalStorageInterface {
  @override
  Future<ResponseReturnDataEntity> loginRequest({
    required LoginParams loginParams,
  }) async {
    try {
      final response = await getServiceAPI().login(
        body: json.encode(loginParams.toData().toJson()),
      );

      final StatusData statusData =
          ResponseReturnDataEntity().getStatusDataWithReturnCode(
        statusCode: response.statusCode,
      );
      final String message = ResponseReturnDataEntity().createMessageGeneric(
        statusData: statusData,
        message: response.error.toString(),
      );

      if (statusData == StatusData.success) {
        setLoginLocalStorage(
          token: LoginStorage(
            password: loginParams.password,
            username: loginParams.username,
          ),
        );

        final LoginResponseParams loginResponseParams =
            LoginResponseModel.fromJson(DecodeResponse().decode(response)).toEntity();
        final TokenUserLocalStorage tokenUserLocalStorage =
            TokenUserLocalStorage(
          accessToken: loginResponseParams.accessToken,
          expiryDate: loginResponseParams.expiryDate,
          refreshToken: loginResponseParams.refreshToken,
          refreshTokenExpiryDate: loginResponseParams.refreshTokenExpiryDate,
        );
        setTokenLocalStorage(token: tokenUserLocalStorage);
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

  @override
  void setTokenLocalStorage({required TokenUserLocalStorage token}) {
    LocalStorageService().create(
      key: token.keyAccessToken ?? "",
      value: token.accessToken ?? "",
    );
    LocalStorageService()
        .create(key: token.keyExpiryDate ?? "", value: token.expiryDate ?? "");
    LocalStorageService().create(
      key: token.keyRefreshToken ?? "",
      value: token.refreshToken ?? "",
    );
    LocalStorageService().create(
      key: token.keyRefreshTokenExpiryDate ?? "",
      value: token.keyRefreshTokenExpiryDate ?? "",
    );
  }

  @override
  Future<ResponseReturnDataEntity> refreshToken() async {
    try {
      final refreshToken = await GetRefreshToken().getRefreshTokenLocalStorage();

      final response = await getServiceAPI().refreshToken(
        body: json.encode(refreshToken.toData().toJson()),
      );

      final StatusData statusData =
          ResponseReturnDataEntity().getStatusDataWithReturnCode(
        statusCode: response.statusCode,
      );

      if (statusData == StatusData.success) {
        final LoginResponseParams loginResponseParams =
            LoginResponseModel.fromJson(DecodeResponse().decode(response)).toEntity();
        final TokenUserLocalStorage tokenUserLocalStorage =
            TokenUserLocalStorage(
          accessToken: loginResponseParams.accessToken,
          expiryDate: loginResponseParams.expiryDate,
          refreshToken: loginResponseParams.refreshToken,
          refreshTokenExpiryDate: loginResponseParams.refreshTokenExpiryDate,
        );
        setTokenLocalStorage(token: tokenUserLocalStorage);
      }

      return ResponseReturnDataEntity(
        isSuccessful: response.isSuccessful,
        statusData: statusData,
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
  void setLoginLocalStorage({required LoginStorage token}) {
    LocalStorageService().create(
      key: token.keyPassword ?? "",
      value: token.password ?? "",
    );
    LocalStorageService().create(
      key: token.keyUsername ?? "",
      value: token.username ?? "",
    );
  }
}
