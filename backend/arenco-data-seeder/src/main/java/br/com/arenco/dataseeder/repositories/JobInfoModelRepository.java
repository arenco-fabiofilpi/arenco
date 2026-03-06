package br.com.arenco.dataseeder.repositories;

import br.com.arenco.dataseeder.entities.JobInfoModel;
import br.com.arenco.dataseeder.enums.JobType;
import br.com.arenco.dataseeder.enums.SyncStatus;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JobInfoModelRepository extends MongoRepository<JobInfoModel, String> {
  Page<JobInfoModel> findByType(final JobType jobType, final Pageable pageable);

  List<JobInfoModel> findByStatus(final SyncStatus status, final Pageable page);
}
