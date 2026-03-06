import 'package:arenco_app/domain/domain.dart';

abstract class ConsultContactInterface {
  Future<ResponseReturnDataEntity> getConsultContact({
    required String username,
  });
}
