package br.com.arenco.arenco_cronjobs.oracle.repositories;

import br.com.arenco.arenco_cronjobs.oracle.entities.CadClientesOutros;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CadClientesOutrosRepository
    extends JpaRepository<@NonNull CadClientesOutros, @NonNull Integer> {
  Optional<@NonNull CadClientesOutros> findByCliente(@NonNull final Integer cliente);
}
