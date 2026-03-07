package br.com.arenco.arenco_cronjobs.oracle.repositories;

import br.com.arenco.arenco_cronjobs.oracle.entities.CadClientesRefBancarias;
import br.com.arenco.arenco_cronjobs.oracle.entities.CadClientesRefBancariasId;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CadClientesRefBancariasRepository
    extends JpaRepository<@NonNull CadClientesRefBancarias, @NonNull CadClientesRefBancariasId> {
    List<CadClientesRefBancarias> findAllByCliente(@NonNull final Integer cliente);
}
