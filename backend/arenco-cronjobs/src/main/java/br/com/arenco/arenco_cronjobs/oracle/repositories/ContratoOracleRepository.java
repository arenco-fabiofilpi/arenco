package br.com.arenco.arenco_cronjobs.oracle.repositories;

import br.com.arenco.arenco_cronjobs.oracle.entities.ContratoIdOracle;
import br.com.arenco.arenco_cronjobs.oracle.entities.ContratoOracle;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContratoOracleRepository
    extends JpaRepository<@NonNull ContratoOracle, @NonNull ContratoIdOracle> {

  @Query(
      value = "SELECT c FROM ContratoOracle c",
      countQuery = "SELECT COUNT(c) FROM ContratoOracle c")
  Page<@NonNull ContratoOracle> findContratoOracles(final Pageable pageable);
}
