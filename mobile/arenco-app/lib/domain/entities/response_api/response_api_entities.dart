// ignore_for_file: must_be_immutable

import 'package:arenco_app/domain/entities/response_api/response_api.dart';
import 'package:equatable/equatable.dart';

enum StatusData {
  success,
  badRequest,
  notFound,
  serverError,
  serviceUnavailable,
  unauthorized,
  forbidden,
  invalidData,
  systemError,
}

/// This model is used for all API responses
///
/// ``` dart
/// ResponseReturnData({
///    this.body,
///    this.messageData,
///    this.statusData,
///  });
/// ```
///
///Used for every successful request and add the correct request model
///
/// ``` dart
///  ResponseReturnData(
///  body : response.body as LoginDataModel,
///  statusData : statusDataConverter(response.statusCode),
/// );
/// ```
///
/// this.messageData is used only for error message
class ResponseReturnDataEntity extends Equatable {
  dynamic body;
  StatusData? statusData;
  MessageDataAPIEntity? messageData;
  bool? isSuccessful;

  ResponseReturnDataEntity({
    this.body,
    this.messageData,
    this.statusData,
    this.isSuccessful,
  });

  StatusData getStatusDataWithReturnCode({required int statusCode}) {
    switch (statusCode) {
      case 200 || 201 || 202 || 204:
        return StatusData.success;
      case 400 || 405 || 409:
        return StatusData.badRequest;
      case 401:
        return StatusData.unauthorized;
      case 403:
        return StatusData.forbidden;
      case 404:
        return StatusData.notFound;
      case 500 || 501 || 502 || 504:
        return StatusData.serverError;
      case 503:
        return StatusData.serviceUnavailable;
      default:
        return StatusData.systemError;
    }
  }

  String createMessageGeneric({
    required StatusData statusData,
    required String message,
  }) {
    switch (statusData) {
      case StatusData.notFound:
        return "Não conseguimos encontrar o que você está procurando. Verifique o endereço ou tente outra opção.";
      case StatusData.serverError:
        return "Algo deu errado em nossos servidores. Estamos trabalhando para resolver isso. Por favor, tente novamente mais tarde.";
      case StatusData.serviceUnavailable:
        return "Estamos realizando manutenção no momento. Por favor, tente novamente mais tarde. Agradecemos pela sua paciência!";
      case StatusData.systemError:
        return '''
Estamos trabalhando para resolver isso o mais rápido possível.
Por favor, tente novamente em alguns instantes.
Se o problema persistir, entre em contato com nosso suporte.\n
Agradecemos pela sua paciência!''';
      case StatusData.unauthorized:
        return '''
Parece que houve um problema ao acessar sua conta. Aqui estão algumas coisas que você pode tentar:\n
1. Verifique se seu e-mail e senha estão corretos.\n
2. Confira se sua conexão com a internet está estável.\n
3. Se você esqueceu sua senha, clique em "Esqueci minha senha" para redefini-la.\n
Se o problema persistir, entre em contato com nosso suporte para que possamos ajudar. Estamos aqui para garantir que tudo funcione bem para você!''';
      default:
        return message;
    }
  }

  @override
  List<Object?> get props => [
        body,
        statusData,
        messageData,
        isSuccessful,
      ];
}
