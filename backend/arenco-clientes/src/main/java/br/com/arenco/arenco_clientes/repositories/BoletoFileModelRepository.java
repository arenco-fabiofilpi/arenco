package br.com.arenco.arenco_clientes.repositories;

import br.com.arenco.arenco_clientes.entities.BoletoFileModel;
import br.com.arenco.arenco_clientes.enums.FileType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BoletoFileModelRepository
    extends MongoRepository<BoletoFileModel, String> {
  Optional<BoletoFileModel> findFirstByReceivableTitleIdAndFileTypeOrderByDateCreatedDesc(
      final String receivableTitleId, final FileType fileType);

  List<BoletoFileModel> findAllByReceivableTitleId(final String receivableTitleId);
}
