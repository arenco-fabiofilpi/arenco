package br.com.arenco.arenco_cronjobs.services.impl;

import br.com.arenco.arenco_cronjobs.entities.*;
import br.com.arenco.arenco_cronjobs.mappers.*;
import br.com.arenco.arenco_cronjobs.oracle.entities.*;
import br.com.arenco.arenco_cronjobs.oracle.repositories.*;
import br.com.arenco.arenco_cronjobs.repositories.*;
import br.com.arenco.arenco_cronjobs.services.ArencoCadastrosExtrasService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArencoCadastrosExtrasServiceImpl implements ArencoCadastrosExtrasService {
  private final CadTipoClienteRepository cadTipoClienteRepository;
  private final CadClientesSocioRepository cadClientesSocioRepository;
  private final CadClientesOutrosRepository cadClientesOutrosRepository;
  private final CadTipoClienteModelRepository cadTipoClienteModelRepository;
  private final CadClientesSocioModelRepository cadClientesSocioModelRepository;
  private final CadClientesOutrosModelRepository cadClientesOutrosModelRepository;
  private final CadClientesRefBancariasRepository cadClientesRefBancariasRepository;
  private final CadClientesRefComerciaisRepository cadClientesRefComerciaisRepository;
  private final CadClientesRefBancariasModelRepository cadClientesRefBancariasModelRepository;
  private final CadClientesRefComerciaisModelRepository cadClientesRefComerciaisModelRepository;

  @Override
  public void sincronizarDadosExtra(final UserModel user, final ClienteOracle clienteOracle) {
    final var idCliente = Integer.valueOf(clienteOracle.getCodigo());
    final var referenciasBancarias = cadClientesRefBancariasRepository.findAllByCliente(idCliente);
    final var referenciasComerciaisList =
        cadClientesRefComerciaisRepository.findAllByCliente(idCliente);
    final var sociosList = cadClientesSocioRepository.findAllByCliente(idCliente);
    final var tipoDeClienteOptional = cadTipoClienteRepository.findByCliente(idCliente);
    final var outrosDadosOptional = cadClientesOutrosRepository.findByCliente(idCliente);

    for (final CadClientesRefBancarias referencia : referenciasBancarias) {
      final CadClientesRefBancariasModel model = new CadClientesRefBancariasModel();
      CadClientesRefBancariasMapper.sincronizar(referencia, model);
      cadClientesRefBancariasModelRepository.save(model);
    }

    for (final CadClientesRefComerciais referencia : referenciasComerciaisList) {
      final CadClientesRefComerciaisModel model = new CadClientesRefComerciaisModel();
      CadClientesRefComerciaisMapper.sincronizar(referencia, model);
      cadClientesRefComerciaisModelRepository.save(model);
    }

    for (final CadClientesSocio socio : sociosList) {
      final CadClientesSocioModel model = new CadClientesSocioModel();
      CadClientesSocioMapper.sincronizar(socio, model);
      cadClientesSocioModelRepository.save(model);
    }

    if (tipoDeClienteOptional.isPresent()) {
      final CadTipoCliente tipoCliente = tipoDeClienteOptional.get();
      final CadTipoClienteModel model = new CadTipoClienteModel();
      CadTipoClienteMapper.sincronizar(tipoCliente, model);
      cadTipoClienteModelRepository.save(model);
    }

    if (outrosDadosOptional.isPresent()) {
      final CadClientesOutros cadClientesOutros = outrosDadosOptional.get();
      final CadClientesOutrosModel model = new CadClientesOutrosModel();
      CadClientesOutrosMapper.sincronizar(cadClientesOutros, model);
      cadClientesOutrosModelRepository.save(model);
    }
  }
}
