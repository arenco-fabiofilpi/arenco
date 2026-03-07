package br.com.arenco.arenco_cronjobs.oracle.repositories;

import br.com.arenco.arenco_cronjobs.oracle.entities.CadClientesSocio;
import br.com.arenco.arenco_cronjobs.oracle.entities.CadClientesSocioId;
import java.util.List;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CadClientesSocioRepository
    extends JpaRepository<@NonNull CadClientesSocio, @NonNull CadClientesSocioId> {
  List<CadClientesSocio> findAllByCliente(@NonNull final Integer cliente);
}
