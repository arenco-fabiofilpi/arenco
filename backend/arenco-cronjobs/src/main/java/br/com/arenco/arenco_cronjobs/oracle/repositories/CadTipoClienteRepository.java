package br.com.arenco.arenco_cronjobs.oracle.repositories;

import br.com.arenco.arenco_cronjobs.oracle.entities.CadTipoCliente;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CadTipoClienteRepository
    extends JpaRepository<@NonNull CadTipoCliente, @NonNull Integer> {
  Optional<@NonNull CadTipoCliente> findByCliente(@NonNull final Integer cliente);
}
