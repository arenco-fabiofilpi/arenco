import 'dart:io';

import 'package:arenco_app/domain/domain.dart';
import 'package:arenco_app/infra/infra.dart';

import 'package:chopper/chopper.dart';
import 'package:http/io_client.dart';

class ChopperClientHttp extends ChopperHttpInterface {
  final chopperCmsAppsClient = ChopperClient(
    baseUrl: Uri.tryParse(URLLaunch.baseURLArenco()),
    services: [
      AuthService.create(),
    ],
    interceptors: [
      HttpLoggingInterceptor(),
      InterceptorsArenco(),
    ],
    authenticator: ArencoAuthenticate(),
    client: IOClient(
      HttpClient()..connectionTimeout = const Duration(seconds: 60),
    ),
  );

  final chopperFinanceAppClient = ChopperClient(
    baseUrl: Uri.tryParse(URLLaunch.baseURLArencoFinance()),
    services: [
      SecuritiesReceivableService.create(),
    ],
    interceptors: [
      HttpLoggingInterceptor(),
      InterceptorsArenco(),
    ],
    authenticator: ArencoAuthenticate(),
    client: IOClient(
      HttpClient()..connectionTimeout = const Duration(seconds: 60),
    ),
  );

  @override
  AuthService getServiceAPI() => chopperCmsAppsClient.getService<AuthService>();
  
  @override
  SecuritiesReceivableService getFinanceAPI() => chopperFinanceAppClient.getService<SecuritiesReceivableService>();
}
