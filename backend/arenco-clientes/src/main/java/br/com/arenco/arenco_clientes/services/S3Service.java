package br.com.arenco.arenco_clientes.services;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

public interface S3Service {
  ResponseInputStream<GetObjectResponse> download(final String key);

  void delete(final String key);
}
