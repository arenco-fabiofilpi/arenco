package br.com.arenco.arenco_clientes.dtos.agreements;

import br.com.arenco.arenco_clientes.enums.TipoProcessamentoBoleto;

import java.util.List;

public record SolicitacaoDeProcessamentoDeBoletosDto(TipoProcessamentoBoleto tipoProcessamentoBoleto, List<String> titulosAReceberIdList) {
}
