package br.com.arenco.arenco_cronjobs.repositories;

import br.com.arenco.arenco_cronjobs.entities.BoletoFileModel;
import br.com.arenco.arenco_cronjobs.enums.FileType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface BoletoFileModelRepository
    extends MongoRepository<BoletoFileModel, String> {
  Optional<BoletoFileModel> findFirstByReceivableTitleIdAndFileTypeOrderByDateCreatedDesc(
      final String receivableTitleId, final FileType fileType);

  List<BoletoFileModel> findAllByReceivableTitleId(final String receivableTitleId);
}
