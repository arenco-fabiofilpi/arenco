package br.com.arenco.arenco_cronjobs.services.impl;

import br.com.arenco.arenco_cronjobs.entities.UserModel;
import br.com.arenco.arenco_cronjobs.oracle.entities.ClienteOracle;
import br.com.arenco.arenco_cronjobs.services.ArencoCadastrosExtrasService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArencoCadastrosExtrasServiceImpl implements ArencoCadastrosExtrasService {
  @Override
  public void sincronizarDadosExtra(final UserModel user, final ClienteOracle clienteOracle) {

  }
}
