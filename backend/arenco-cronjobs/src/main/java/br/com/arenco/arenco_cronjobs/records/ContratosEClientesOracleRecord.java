package br.com.arenco.arenco_cronjobs.records;

import br.com.arenco.arenco_cronjobs.oracle.entities.ClienteOracle;

import java.util.List;

public record ContratosEClientesOracleRecord(
    ClienteOracle clienteOracle, List<ContratoOracleComTitulosRecord> contratoOracleComTitulosRecordList) {}
