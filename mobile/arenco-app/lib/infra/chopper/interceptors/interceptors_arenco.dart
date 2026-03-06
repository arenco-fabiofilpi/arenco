import 'dart:async';
import 'package:arenco_app/resources/resources.dart';
import 'package:chopper/chopper.dart';

class InterceptorsArenco extends GetUseTokenValidate implements Interceptor {
  @override
  FutureOr<Response<BodyType>> intercept<BodyType>(
    Chain<BodyType> chain,
  ) async {
    Request request = applyHeader(
      chain.request,
      'Content-Type',
      'application/json',
    );
    if (getUseTokenUser(path: chain.request.uri.path.toLowerCase())) {
      final token = await GetRefreshToken().getTokenLocalStorage();
      request = applyHeaders(
        chain.request,
        {
          'Authorization': 'Bearer ${token.token}',
          'Content-Type': 'application/json',
        },
      );
    }

    return chain.proceed(request);
  }
}
