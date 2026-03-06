package br.com.arenco.arenco_cronjobs.oracle.repositories;

import br.com.arenco.arenco_cronjobs.oracle.entities.ClienteOracle;
import java.util.Optional;

import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteOracleRepository extends JpaRepository<@NonNull ClienteOracle, @NonNull String> {
  Optional<ClienteOracle> findFirstByCodigo(final String codigo);
}
