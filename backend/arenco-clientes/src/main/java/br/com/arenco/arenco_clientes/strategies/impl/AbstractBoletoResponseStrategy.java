package br.com.arenco.arenco_clientes.strategies.impl;

import br.com.arenco.arenco_clientes.strategies.BoletoResponseStrategy;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public abstract class AbstractBoletoResponseStrategy implements BoletoResponseStrategy {

  abstract String getContentType();

  abstract String getFileName();

  @Override
  public void colocarBoletoNaResposta(
      final HttpServletResponse response, final byte[] bytes) throws IOException {
    // Coloca na Resposta
    // Cabeçalhos do download
    response.setContentType(getContentType());
    response.setCharacterEncoding("UTF-8");
    // Nome do arquivo no download (compatível com RFC 5987)
    final String filename = getFileName();
    response.setHeader(
            "Content-Disposition",
            "attachment; filename=\"" + filename + "\"; filename*=UTF-8''" + urlEncode(filename));
    response.setHeader("Content-Length", String.valueOf(bytes.length));
    // Opcional: cache control
    response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");

    // Escreve no body
    try (final ServletOutputStream os = response.getOutputStream()) {
      os.write(bytes);
      os.flush();
    }
  }

  private String urlEncode(final String value) {
    try {
      return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20");
    } catch (final Exception e) {
      log.error("error while encoding response", e);
      return value;
    }
  }
}
