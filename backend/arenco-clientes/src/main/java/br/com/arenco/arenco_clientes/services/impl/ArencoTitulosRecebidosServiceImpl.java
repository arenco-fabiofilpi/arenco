package br.com.arenco.arenco_clientes.services.impl;

import br.com.arenco.arenco_clientes.services.ArencoTitulosRecebidosService;
import br.com.arenco.arenco_clientes.entities.ReceivedTitleModel;
import br.com.arenco.arenco_clientes.repositories.ReceivedTitleModelRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArencoTitulosRecebidosServiceImpl extends AbstractArencoTitulosService
    implements ArencoTitulosRecebidosService {

  private final ReceivedTitleModelRepository repository;
  private final MongoTemplate mongoTemplate;

  @Override
  public Optional<ReceivedTitleModel> findById(final String id) {
    if (id == null) {
      return Optional.empty();
    }
    return repository.findById(id);
  }

  @Override
  public Page<ReceivedTitleModel> pesquisar(final Pageable pageable) {
    return repository.findAll(pageable);
    //    final List<Criteria> criterios = new ArrayList<>();
    //    popularCriteriosDePesquisa(filtro, criterios);
    //
    //    final Query query = new Query();
    //    if (!criterios.isEmpty()) {
    //      query.addCriteria(new Criteria().andOperator(criterios.toArray(new Criteria[0])));
    //    }
    //
    //    final long total = mongoTemplate.count(query, ReceivedTitleModel.class);
    //    query.with(pageable);
    //    final List<ReceivedTitleModel> resultados = mongoTemplate.find(query,
    // ReceivedTitleModel.class);
    //
    //    return new PageImpl<>(resultados, pageable, total);
  }
}
