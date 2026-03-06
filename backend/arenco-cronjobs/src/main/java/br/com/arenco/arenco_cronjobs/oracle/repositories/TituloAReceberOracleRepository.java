package br.com.arenco.arenco_cronjobs.oracle.repositories;

import br.com.arenco.arenco_cronjobs.oracle.entities.TituloAReceberIdOracle;
import br.com.arenco.arenco_cronjobs.oracle.entities.TituloAReceberOracle;
import java.util.List;

import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TituloAReceberOracleRepository
    extends JpaRepository<@NonNull TituloAReceberOracle, @NonNull TituloAReceberIdOracle> {
  List<TituloAReceberOracle> findAllByNumeDocAndEmpresa(final String numeDoc, final String empresa);
}
