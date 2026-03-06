package br.com.arenco.arenco_clientes.facades;

import br.com.arenco.arenco_clientes.dtos.agreements.BoletoFileDto;
import br.com.arenco.arenco_clientes.dtos.agreements.SolicitacaoDeProcessamentoDeBoletosDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArencoBoletosFacade {
  void solicitarProcessamentoDeBoletos(final SolicitacaoDeProcessamentoDeBoletosDto dto);

  Page<BoletoFileDto> search(final Pageable pageable);

  void get(final String id, final HttpServletResponse response);
}
