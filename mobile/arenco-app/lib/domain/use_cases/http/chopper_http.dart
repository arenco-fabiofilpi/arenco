import 'package:arenco_app/infra/infra.dart';

abstract class ChopperHttpInterface {
  AuthService getServiceAPI();

  SecuritiesReceivableService getFinanceAPI();
}
