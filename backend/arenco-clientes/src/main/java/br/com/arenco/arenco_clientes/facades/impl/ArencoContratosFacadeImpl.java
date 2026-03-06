package br.com.arenco.arenco_clientes.facades.impl;

import br.com.arenco.arenco_clientes.facades.ArencoContratosFacade;
import br.com.arenco.arenco_clientes.services.ArencoContratosService;
import br.com.arenco.arenco_clientes.dtos.agreements.ContratoCompletoDto;
import br.com.arenco.arenco_clientes.dtos.agreements.ContratoDto;
import br.com.arenco.arenco_clientes.factories.ContratoCompletoDtoFactory;
import br.com.arenco.arenco_clientes.factories.ContratoDtoFactory;
import br.com.arenco.arenco_clientes.entities.AgreementModel;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArencoContratosFacadeImpl implements ArencoContratosFacade {
  private final ArencoContratosService arencoContratosService;

  @Override
  public Page<ContratoDto> pesquisarContratos(final Pageable pageable) {
    final Page<AgreementModel> pageModel = arencoContratosService.pesquisar(pageable);

    return pageModel.map(i -> new ContratoDtoFactory(i).create());
  }

  @Override
  public Optional<ContratoCompletoDto> pegarContratoPorId(final String id) {
    final var contratoModelOptional = arencoContratosService.findById(id);
    if (contratoModelOptional.isPresent()) {
      final var contratoModel = contratoModelOptional.get();
      return Optional.of(new ContratoCompletoDtoFactory(contratoModel).create());
    }
    log.info("Contrato não encontrado com o id {}", id);
    return Optional.empty();
  }
}
