package br.com.arenco.arenco_cronjobs.repositories;

import br.com.arenco.arenco_cronjobs.entities.AgreementModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AgreementModelRepository extends MongoRepository<AgreementModel, String> {
  Optional<AgreementModel> findByNumeContratoAndEmpresaAndUserId(
      final String numeContrato, final String empresa, final String userId);

  boolean existsByNumeContratoAndEmpresaAndUserId(
      final String numeContrato, final String empresa, final String userId);

  Page<AgreementModel> findAllByUserId(final String userId, final Pageable pageable);

  Optional<AgreementModel> findByNumeContrato(final String numeContrato);
}
