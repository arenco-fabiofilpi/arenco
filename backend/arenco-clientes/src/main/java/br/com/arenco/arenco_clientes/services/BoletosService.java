package br.com.arenco.arenco_clientes.services;

import br.com.arenco.arenco_clientes.entities.BoletoFileModel;
import br.com.arenco.arenco_clientes.enums.TipoProcessamentoBoleto;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoletosService {
  void gerarBoletoAProcessar(
      final TipoProcessamentoBoleto tipoProcessamentoBoleto, final String receivableTitleId);

  Page<@NonNull BoletoFileModel> findAll(final Pageable pageable);

  void colocarBoletoNaResposta(final String id, final HttpServletResponse response);
}
