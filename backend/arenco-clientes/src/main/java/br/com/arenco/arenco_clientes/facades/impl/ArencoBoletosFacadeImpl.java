package br.com.arenco.arenco_clientes.facades.impl;

import br.com.arenco.arenco_clientes.facades.ArencoBoletosFacade;
import br.com.arenco.arenco_clientes.facades.JobInfoFacade;
import br.com.arenco.arenco_clientes.services.ArencoTitulosAReceberService;
import br.com.arenco.arenco_clientes.dtos.agreements.BoletoFileDto;
import br.com.arenco.arenco_clientes.dtos.agreements.SolicitacaoDeProcessamentoDeBoletosDto;
import br.com.arenco.arenco_clientes.factories.BoletoFileDtoFactory;
import br.com.arenco.arenco_clientes.enums.JobType;
import br.com.arenco.arenco_clientes.services.BoletosService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArencoBoletosFacadeImpl implements ArencoBoletosFacade {
  private final BoletosService boletosService;
  private final JobInfoFacade jobInfoFacade;
  private final ArencoTitulosAReceberService arencoTitulosAReceberService;

  @Override
  public void solicitarProcessamentoDeBoletos(final SolicitacaoDeProcessamentoDeBoletosDto dto) {
    final var listaDeIdDeTitulosAReceber = dto.titulosAReceberIdList();
    final var tipoDeProcessamento = dto.tipoProcessamentoBoleto();
    for (final var idDoTituloAReceber : listaDeIdDeTitulosAReceber) {
      boletosService.gerarBoletoAProcessar(tipoDeProcessamento, idDoTituloAReceber);
    }
    jobInfoFacade.createManualTrigger(JobType.SINCRONIZAR_BOLETOS);
    log.info("solicitarProcessamentoDeBoletos concluido com sucesso");
  }

  @Override
  public Page<BoletoFileDto> search(final Pageable pageable) {
    final var pageModel = boletosService.findAll(pageable);

    return pageModel.map(
        i -> {
          final var receivableTitleId = i.getReceivableTitleId();
          final var receivableTitleModelOpt =
              arencoTitulosAReceberService.findById(receivableTitleId);
          if (receivableTitleModelOpt.isEmpty()) {
            throw new IllegalArgumentException(
                "Nao encontrado Titulo para o Boleto "
                    + i.getId()
                    + ". Buscando Titulo ID "
                    + receivableTitleId);
          }
          return new BoletoFileDtoFactory(i, receivableTitleModelOpt.get()).create();
        });
  }

  @Override
  public void get(final String id, final HttpServletResponse response) {
    boletosService.colocarBoletoNaResposta(id, response);
  }
}
