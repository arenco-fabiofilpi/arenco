import 'dart:convert';

import 'package:arenco_app/domain/domain.dart';
import 'package:chopper/chopper.dart';

class DecodeResponse implements DecodeInterface {
  @override
  Map<String, dynamic> decode(Response response) {
    final decoded = response.body != null &&
                response.body != "" &&
                response.isSuccessful == true ||
            (response.bodyString != "")
        ? json.decode(const Utf8Decoder().convert(response.bodyBytes))
            as Map<String, dynamic>
        : <String, dynamic>{};
    return decoded;
  }
}
