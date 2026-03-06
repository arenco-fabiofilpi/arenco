package br.com.arenco.arenco_clientes.services.impl;

import br.com.arenco.arenco_clientes.services.ArencoTitulosAReceberService;
import br.com.arenco.arenco_clientes.entities.ReceivableTitleModel;
import br.com.arenco.arenco_clientes.repositories.ReceivableTitleModelRepository;
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
public class ArencoTitulosAReceberServiceImpl extends AbstractArencoTitulosService
    implements ArencoTitulosAReceberService {

  private final ReceivableTitleModelRepository repository;
  private final MongoTemplate mongoTemplate;

  @Override
  public Optional<ReceivableTitleModel> findById(final String id) {
    if (id == null) {
      return Optional.empty();
    }
    return repository.findById(id);
  }

  @Override
  public Page<ReceivableTitleModel> pesquisar(final Pageable pageable) {
    //    if (filtro == null) {
    return repository.findAll(pageable);
    //    }
    //    final List<Criteria> criterios = new ArrayList<>();
    //    popularCriteriosDePesquisa(filtro, criterios);
    //
    //    final Query query = new Query();
    //    if (!criterios.isEmpty()) {
    //      query.addCriteria(new Criteria().andOperator(criterios.toArray(new Criteria[0])));
    //    }
    //
    //    long total = mongoTemplate.count(query, ReceivableTitleModel.class);
    //    query.with(pageable);
    //    List<ReceivableTitleModel> resultados = mongoTemplate.find(query,
    // ReceivableTitleModel.class);
    //
    //    return new PageImpl<>(resultados, pageable, total);
  }
}
