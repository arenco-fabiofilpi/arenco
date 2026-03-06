import "dart:async";
import 'package:chopper/chopper.dart';

part "auth_service.chopper.dart";

@ChopperApi(baseUrl: "/auth") 
abstract class AuthService extends ChopperService {
  static AuthService create([ChopperClient? client]) => _$AuthService(client);

  @Post(path: '/token')
  Future<Response> login({@Body() required dynamic body});

  @Post(path: '/refresh')
  Future<Response> refreshToken({@Body() required dynamic body});

  @Post(path: '/reset-password/password-recovery')
  Future<Response> passwordRecovery({@Body() required dynamic body});

  @Post(path: '/reset-password/password-recovery/token')
  Future<Response> changePasswordWithToken({@Body() required dynamic body});
  
  @Get(path: '/password/password-recovery?username={username}')
  Future<Response> getContactConsultRecoveryPassword({@Path() required String username});

  @Post(path: '/password/password-recovery')
  Future<Response> resquestToken({@Body() required dynamic body});

  @Put(path: '/password/password-recovery/validate-token')
  Future<Response> checkTokenIsValidate({@Body() required dynamic body});

  @Put(path: '/password/password-recovery/reset-password')
  Future<Response> newPasswordWithToken({@Body() required dynamic body});
}
