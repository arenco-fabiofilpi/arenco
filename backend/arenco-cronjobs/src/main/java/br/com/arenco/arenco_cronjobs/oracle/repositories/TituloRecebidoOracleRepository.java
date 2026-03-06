package br.com.arenco.arenco_cronjobs.oracle.repositories;

import br.com.arenco.arenco_cronjobs.oracle.entities.TituloRecebidoIdOracle;
import br.com.arenco.arenco_cronjobs.oracle.entities.TituloRecebidoOracle;
import java.util.List;

import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TituloRecebidoOracleRepository
    extends JpaRepository<@NonNull TituloRecebidoOracle, @NonNull TituloRecebidoIdOracle> {
  List<TituloRecebidoOracle> findAllByNumeDocAndEmpresa(final String numeDoc, final String empresa);
}
