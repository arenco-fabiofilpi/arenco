package br.com.arenco.arenco_cronjobs.records;

import br.com.arenco.arenco_cronjobs.oracle.entities.ContratoOracle;
import br.com.arenco.arenco_cronjobs.oracle.entities.TituloAReceberOracle;
import br.com.arenco.arenco_cronjobs.oracle.entities.TituloRecebidoOracle;
import java.util.List;

public record ContratoOracleComTitulosRecord(
    ContratoOracle contratoOracle,
    List<TituloAReceberOracle> tituloAReceberOracleList,
    List<TituloRecebidoOracle> tituloRecebidoOracleList) {}
