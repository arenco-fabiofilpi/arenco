package br.com.arenco.arenco_cronjobs.services;

import java.io.InputStream;

public interface S3Service {
  void upload(
      final String key, final InputStream in, final long contentLength, final String contentType);

  void delete(final String key);
}
