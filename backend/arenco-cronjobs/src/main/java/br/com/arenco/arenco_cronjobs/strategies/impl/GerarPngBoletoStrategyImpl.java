package br.com.arenco.arenco_cronjobs.strategies.impl;

import br.com.arenco.arenco_cronjobs.entities.*;
import br.com.arenco.arenco_cronjobs.enums.FileType;
import br.com.arenco.arenco_cronjobs.exceptions.ArencoSincronizacaoBoletosInterrompida;
import br.com.arenco.arenco_cronjobs.repositories.BoletoFileModelRepository;
import br.com.arenco.arenco_cronjobs.repositories.ReceivableTitleModelRepository;
import br.com.arenco.arenco_cronjobs.services.S3Service;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GerarPngBoletoStrategyImpl extends AbstractGerarBoletoStrategyImpl {
  @Getter private final S3Service s3Service;
  private final BoletoFileModelRepository repository;
  private final ReceivableTitleModelRepository receivableTitleModelRepository;

  @Override
  public void gerar(
      final UserModel customer,
      final AddressModel addressModel,
      final ReceivableTitleModel receivableTitleModel,
      final PaymentSlipSettingsModel paymentSlipSettingsModel)
      throws ArencoSincronizacaoBoletosInterrompida {
    final var model = new BoletoFileModel();
    model.setFileType(FileType.PNG);
    model.setReceivableTitleId(receivableTitleModel.getId());
    repository.save(model);
    final var pngGerado =
        getGeradorDeBoleto(paymentSlipSettingsModel, customer, addressModel, receivableTitleModel);
    try {
      final var bytes = pngGerado.geraPNG();
      uploadFromBytes(model.getId(), bytes, "image/png");
    } catch (final RuntimeException e) {
      log.error("Erro ao gerar PDF", e);
      repository.delete(model);
      return;
    }
    receivableTitleModel.setPdfId(model.getId());
    receivableTitleModelRepository.save(receivableTitleModel);
  }
}
