import 'dart:async';

import 'package:arenco_app/infra/infra.dart';
import 'package:arenco_app/resources/resources.dart';
import 'package:chopper/chopper.dart';

class ArencoAuthenticate extends Authenticator {
  @override
  FutureOr<Request?> authenticate(
    Request request,
    Response response, [
    Request? originalRequest,
  ]) async {
    if (GetUseTokenValidate()
            .getUseTokenUser(path: request.uri.path.toLowerCase()) &&
        response.statusCode == 401) {
      await LoginRepository().refreshToken();

      final token = await GetRefreshToken().getRefreshTokenLocalStorage();

      if (token.refreshToken != null && token.refreshToken != '') {
        final updateHeader = Map<String, String>.of(request.headers);
        token.refreshToken = 'Bearer $token';
        updateHeader.update(
          'Authorization',
          (value) => token.refreshToken!,
          ifAbsent: () => token.refreshToken!,
        );

        return request.copyWith(headers: updateHeader);
      }
    }

    return null;
  }
}
