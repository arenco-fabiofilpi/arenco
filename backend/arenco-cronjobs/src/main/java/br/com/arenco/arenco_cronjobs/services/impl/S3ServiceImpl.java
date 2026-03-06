package br.com.arenco.arenco_cronjobs.services.impl;

import br.com.arenco.arenco_cronjobs.services.S3Service;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {
  @Value("${mgc.s3.bucket}")
  private String bucket;

  private final S3Client s3;

  @Override
  public void upload(
      final String key, final InputStream in, final long contentLength, final String contentType) {
    PutObjectRequest req =
        PutObjectRequest.builder().bucket(bucket).key(key).contentType(contentType).build();
    s3.putObject(req, RequestBody.fromInputStream(in, contentLength));
  }

  @Override
  public void delete(final String key) {
    s3.deleteObject(DeleteObjectRequest.builder().bucket(bucket).key(key).build());
  }
}
