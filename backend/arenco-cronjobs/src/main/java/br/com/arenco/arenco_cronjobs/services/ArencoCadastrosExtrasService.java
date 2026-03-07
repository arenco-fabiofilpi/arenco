package br.com.arenco.arenco_cronjobs.services;

import br.com.arenco.arenco_cronjobs.entities.UserModel;
import br.com.arenco.arenco_cronjobs.oracle.entities.ClienteOracle;

public interface ArencoCadastrosExtrasService {
    void sincronizarDadosExtra(UserModel user, ClienteOracle clienteOracle);
}
