import 'package:arenco_app/domain/domain.dart';

abstract class GetUserDataInterface {
  Future<LoginParams> getUserData();
}
