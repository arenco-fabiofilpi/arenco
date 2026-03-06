<template>
  <q-card v-if="titulo" flat bordered class="q-pa-md">
    <q-card-section class="q-pb-none">
      <div class="text-h6">Dados do Título</div>
      <div class="text-caption text-grey-7">Informações principais e financeiras</div>
    </q-card-section>

    <q-separator spaced />

    <q-card-section class="q-pt-none">
      <div class="row q-col-gutter-md">
        <div v-for="f in visibleFields" :key="f.key" class="col-12 col-sm-6 col-lg-4">
          <q-card flat class="q-pa-sm bg-grey-1 rounded-borders">
            <div class="text-caption text-grey-7 q-mb-xs">{{ f.label }}</div>
            <div class="row items-center no-wrap">
              <div class="col text-body2 text-weight-medium ellipsis">
                {{ formatValue(f) }}
              </div>
              <q-btn
                size="sm"
                dense
                round
                flat
                icon="content_copy"
                :title="`Copiar ${f.label}`"
                @click="copy(formatValue(f))"
              >
                <q-tooltip>Clicar para copiar</q-tooltip>
              </q-btn>
            </div>
          </q-card>
        </div>
      </div>
    </q-card-section>

    <q-card-actions align="right" class="q-pt-md">
      <q-btn
        v-if="titulo.pdfId"
        label="Baixar Boleto PDF"
        icon="picture_as_pdf"
        color="primary"
        :loading="carregandoPdf"
        @click="baixarBoleto(titulo.pdfId, FormatoBoleto.PDF)"
      />
      <q-btn
        v-if="titulo.pngId"
        label="Baixar Boleto PNG"
        icon="image"
        color="primary"
        :loading="carregandoPng"
        @click="baixarBoleto(titulo.pngId, FormatoBoleto.PNG)"
      />
    </q-card-actions>
    <q-card-section v-if="errorMessage" class="text-negative text-center">
      {{ errorMessage }}
    </q-card-section>
  </q-card>
</template>

<script setup lang="ts">
  import { computed } from 'vue';
  import { useQuasar } from 'quasar';
  import { FormatoBoleto, useBoletoDownloader } from 'src/composables/useBoletoDownloader';

  type Titulo = Record<string, any>;

  const props = defineProps<{ titulo: Titulo | null }>();

  const { baixarBoleto, carregandoPdf, carregandoPng, errorMessage } = useBoletoDownloader();
  const $q = useQuasar();

  const FIELD_MAP = [
    { label: 'Empresa', key: 'empresa', type: 'string' },
    { label: 'Cliente', key: 'cliente', type: 'string' },
    { label: 'Nome Cliente', key: 'nomeClte', type: 'string' },
    { label: 'CNPJ/CPF Cliente', key: 'cnpjCpfDoCliente', type: 'string' },
    { label: 'Número Doc', key: 'numeDoc', type: 'string' },
    { label: 'Sequência', key: 'sequencia', type: 'string' },
    { label: 'Sequência Reparcela', key: 'sequenciaReparcela', type: 'string' },
    { label: 'Lote', key: 'lote', type: 'string' },
    { label: 'Quadra', key: 'quadra', type: 'string' },
    { label: 'Data Emissão', key: 'dtEmissao', type: 'string' },
    { label: 'Data Base', key: 'dataBase', type: 'string' },
    { label: 'Data Hoje', key: 'dataDeHoje', type: 'string' },
    { label: 'Data Reparcela', key: 'dataReparcela', type: 'string' },
    { label: 'Centro de Custo', key: 'ccusto', type: 'string' },
    { label: 'Nome Centro de Custo', key: 'nomeCcusto', type: 'string' },
    { label: 'Unidade de Custo', key: 'unidadeDeCusto', type: 'string' },
    { label: 'Fatura', key: 'fatura', type: 'string' },
    { label: 'Número Fatura', key: 'numeFatura', type: 'string' },
    { label: 'Vencimento', key: 'vencimento', type: 'string' },
    { label: 'Valor', key: 'valor', type: 'string' },
    { label: 'Tipo Doc', key: 'tipoDoc', type: 'string' },
    { label: 'Série', key: 'serie', type: 'string' },
    { label: 'Observação', key: 'observacao', type: 'string' },
    { label: 'Saldo', key: 'saldo', type: 'string' },
    { label: 'Receita', key: 'receita', type: 'string' },
    { label: 'Denominação', key: 'denominacao', type: 'string' },
    { label: 'Moeda', key: 'moeda', type: 'string' },
    { label: 'Descrição Moeda', key: 'descricaoMoeda', type: 'string' },
    { label: 'Quantidade Moeda', key: 'qtdeMoeda', type: 'number' },
    { label: 'Saldo Qtde Moeda', key: 'saldoQtdeMoeda', type: 'number' },
    { label: 'Valor Corrigido Vencimento', key: 'valorCorrigidoVencimento', type: 'string' },
    { label: 'Valor Corrigido Hoje', key: 'valorCorrigidoHoje', type: 'string' },
    { label: 'Valor Corrigido Data Ref', key: 'valorCorrigidoDataRef', type: 'string' },
    { label: 'Código Portador', key: 'codigoPortador', type: 'string' },
    { label: 'Tem Cobrança Boleto', key: 'temCobrancaBoleto', type: 'boolean' },
    { label: 'Situação Jurídica', key: 'indicacaoSituacaoJuridica', type: 'string' },
    { label: 'Parcelas Liberadas Após', key: 'parcelasLiberadaApos', type: 'string' },
    { label: 'Tipo Correção Contrato', key: 'tipoCorrecaoContrato', type: 'string' },
    { label: 'Taxa Juros Valor Presente', key: 'taxaJurosValorPresente', type: 'percent' },
    { label: 'Saldo Valor Presente', key: 'saldoValorPresente', type: 'string' },
    { label: 'Seguro Embutido', key: 'seguroEmbutidoParcela', type: 'boolean' },
    { label: 'Percentual Seguro', key: 'percentualSeguro', type: 'percent' },
    { label: 'Código Cobrança', key: 'codigoCobranca', type: 'string' },
    { label: 'Total Reparcela', key: 'totalReparcela', type: 'string' },
  ] as const;

  const visibleFields = computed(() =>
    FIELD_MAP.filter((f) => {
      const v = props.titulo?.[f.key as keyof Titulo];
      return v !== undefined && v !== null && String(v) !== '';
    })
  );

  const numberFmt = new Intl.NumberFormat('pt-BR');

  function formatValue(f: (typeof FIELD_MAP)[number]) {
    const v: any = props.titulo?.[f.key as keyof Titulo];
    switch (f.type) {
      case 'number':
        return isFinite(+v) ? numberFmt.format(+v) : v != null ? String(v) : '';
      case 'percent':
        return isFinite(+v) ? `${numberFmt.format(+v)}%` : v != null ? String(v) : '';
      case 'boolean':
        return v ? 'Sim' : 'Não';
      default:
        return v != null ? String(v) : '';
    }
  }

  function copy(text: string) {
    navigator.clipboard.writeText(text);
    $q.notify({ message: 'Copiado!', color: 'positive', icon: 'check', timeout: 1200 });
  }
</script>

<style scoped>
  .rounded-borders {
    border-radius: 12px;
  }
  .ellipsis {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
</style>
