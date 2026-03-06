package br.com.arenco.arenco_cronjobs.records;

import br.com.arenco.arenco_cronjobs.entities.UserModel;

import java.util.List;

public record ContratoOracleEClienteModelRecord(
        UserModel userModel, List<ContratoOracleComTitulosRecord> contratoOracleComTitulosRecordList) {}
