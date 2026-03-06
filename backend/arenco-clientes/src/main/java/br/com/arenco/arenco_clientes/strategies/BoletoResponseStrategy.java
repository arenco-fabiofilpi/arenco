package br.com.arenco.arenco_clientes.strategies;

import br.com.arenco.arenco_clientes.enums.FileType;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface BoletoResponseStrategy {
  FileType supportedType();

  void colocarBoletoNaResposta(
          final HttpServletResponse response, final byte[] bytes) throws IOException;
}
