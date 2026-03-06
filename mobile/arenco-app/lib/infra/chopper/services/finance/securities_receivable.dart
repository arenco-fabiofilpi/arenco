import "dart:async";
import 'package:chopper/chopper.dart';

part "securities_receivable.chopper.dart";

@ChopperApi(baseUrl: "/titulos-api/v1")
abstract class SecuritiesReceivableService extends ChopperService {
  static SecuritiesReceivableService create([ChopperClient? client]) => _$SecuritiesReceivableService(client);

  @Get(path: '/customers/titulos-a-receber/current')
  Future<Response> securitiesReceivableList();

  @Get(path: '/titulos-a-receber/{id}')
  Future<Response> securitiesReceivableById({@Path('id') required String id});

}
