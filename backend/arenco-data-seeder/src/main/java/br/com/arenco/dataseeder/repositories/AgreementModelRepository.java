package br.com.arenco.dataseeder.repositories;

import br.com.arenco.dataseeder.entities.AgreementModel;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AgreementModelRepository extends MongoRepository<AgreementModel, String> {
  Optional<AgreementModel> findByNumeContratoAndEmpresaAndUserId(
      final String numeContrato, final String empresa, final String userId);

  boolean existsByNumeContratoAndEmpresaAndUserId(
      final String numeContrato, final String empresa, final String userId);

  Page<AgreementModel> findAllByUserId(final String userId, final Pageable pageable);

  Optional<AgreementModel> findByNumeContrato(final String numeContrato);
}
