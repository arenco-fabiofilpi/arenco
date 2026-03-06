package br.com.arenco.arenco_clientes.services.impl;

import br.com.arenco.arenco_clientes.services.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {
  @Value("${mgc.s3.bucket}")
  private String bucket;

  private final S3Client s3;

  @Override
  public ResponseInputStream<GetObjectResponse> download(final String key) {
    GetObjectRequest req = GetObjectRequest.builder().bucket(bucket).key(key).build();
    return s3.getObject(req);
  }

  @Override
  public void delete(final String key) {
    s3.deleteObject(DeleteObjectRequest.builder().bucket(bucket).key(key).build());
  }
}
