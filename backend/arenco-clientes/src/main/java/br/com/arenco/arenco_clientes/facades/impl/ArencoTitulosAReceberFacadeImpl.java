package br.com.arenco.arenco_clientes.facades.impl;

import br.com.arenco.arenco_clientes.exceptions.TituloNaoEncontrado;
import br.com.arenco.arenco_clientes.facades.ArencoTitulosAReceberFacade;
import br.com.arenco.arenco_clientes.services.ArencoTitulosAReceberService;
import br.com.arenco.arenco_clientes.dtos.agreements.TituloAReceberCompletoDto;
import br.com.arenco.arenco_clientes.dtos.agreements.TituloAReceberDto;
import br.com.arenco.arenco_clientes.factories.TituloAReceberCompletoDtoFactory;
import br.com.arenco.arenco_clientes.factories.TituloAReceberDtoFactory;
import br.com.arenco.arenco_clientes.entities.ReceivableTitleModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArencoTitulosAReceberFacadeImpl implements ArencoTitulosAReceberFacade {
  private final ArencoTitulosAReceberService arencoTitulosAReceberService;

  @Override
  public Page<TituloAReceberDto> pesquisarTitulosAReceber(final Pageable pageable) {

    final Page<ReceivableTitleModel> pageModel =
        arencoTitulosAReceberService.pesquisar(pageable);

    return pageModel.map(i -> new TituloAReceberDtoFactory(i).create());
  }

  @Override
  public TituloAReceberCompletoDto pegarTituloAReceber(final String id) {
    final var optional = arencoTitulosAReceberService.findById(id);
    if (optional.isEmpty()) {
      throw new TituloNaoEncontrado(
          String.format("Nao encontrado Titulo a Receber com o id %s", id));
    }
    final var model = optional.get();
    return new TituloAReceberCompletoDtoFactory(model).create();
  }
}
