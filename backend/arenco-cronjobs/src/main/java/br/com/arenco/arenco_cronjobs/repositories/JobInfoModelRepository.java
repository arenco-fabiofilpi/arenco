package br.com.arenco.arenco_cronjobs.repositories;

import br.com.arenco.arenco_cronjobs.entities.JobInfoModel;
import br.com.arenco.arenco_cronjobs.enums.JobType;
import br.com.arenco.arenco_cronjobs.enums.SyncStatus;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JobInfoModelRepository extends MongoRepository<JobInfoModel, String> {
  Page<JobInfoModel> findByType(final JobType jobType, final Pageable pageable);

  List<JobInfoModel> findByStatus(final SyncStatus status, final Pageable page);
}
