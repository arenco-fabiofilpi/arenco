package br.com.arenco.arenco_clientes.facades.impl;

import br.com.arenco.arenco_clientes.facades.ArencoTitulosRecebidosFacade;
import br.com.arenco.arenco_clientes.services.ArencoTitulosRecebidosService;
import br.com.arenco.arenco_clientes.dtos.agreements.TituloRecebidoCompletoDto;
import br.com.arenco.arenco_clientes.dtos.agreements.TituloRecebidoDto;
import br.com.arenco.arenco_clientes.factories.TituloRecebidoCompletoDtoFactory;
import br.com.arenco.arenco_clientes.factories.TituloRecebidoDtoFactory;
import br.com.arenco.arenco_clientes.entities.ReceivedTitleModel;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArencoTitulosRecebidosFacadeImpl implements ArencoTitulosRecebidosFacade {
  private final ArencoTitulosRecebidosService arencoTitulosRecebidosService;

  @Override
  public Page<TituloRecebidoDto> pesquisarTitulosRecebidos(final Pageable pageable) {

    final Page<ReceivedTitleModel> pageModel = arencoTitulosRecebidosService.pesquisar(pageable);

    return pageModel.map(i -> new TituloRecebidoDtoFactory(i).create());
  }

  @Override
  public Optional<TituloRecebidoCompletoDto> pegarTituloRecebido(final String id) {
    final var optional = arencoTitulosRecebidosService.findById(id);
    if (optional.isPresent()) {
      final var model = optional.get();
      return Optional.of(new TituloRecebidoCompletoDtoFactory(model).create());
    }
    log.info("Titulo Recebido não encontrado com o id {}", id);
    return Optional.empty();
  }
}
