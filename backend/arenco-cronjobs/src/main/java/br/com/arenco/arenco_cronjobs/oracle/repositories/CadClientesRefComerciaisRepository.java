package br.com.arenco.arenco_cronjobs.oracle.repositories;

import br.com.arenco.arenco_cronjobs.oracle.entities.CadClientesRefComerciais;
import br.com.arenco.arenco_cronjobs.oracle.entities.CadClientesRefComerciaisId;
import java.util.List;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CadClientesRefComerciaisRepository
    extends JpaRepository<@NonNull CadClientesRefComerciais, @NonNull CadClientesRefComerciaisId> {
  List<@NonNull CadClientesRefComerciais> findAllByCliente(@NonNull final Integer cliente);
}
