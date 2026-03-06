package br.com.arenco.arenco_clientes.strategies.impl;

import br.com.arenco.arenco_clientes.enums.FileType;
import br.com.arenco.arenco_clientes.services.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PdfBoletoResponseStrategy extends AbstractBoletoResponseStrategy {
  @Override
  public FileType supportedType() {
    return FileType.PDF;
  }

  @Override
  String getContentType() {
    return "application/pdf";
  }

  @Override
  String getFileName() {
    return "boleto.pdf";
  }
}
