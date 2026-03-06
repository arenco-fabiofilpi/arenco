<template>
  <q-card v-if="titulo" flat bordered class="q-pa-md">
    <q-card-section class="q-pb-none">
      <div class="text-h6">Detalhes do Título Recebido</div>
      <div class="text-caption text-grey-7">Informações principais e financeiras</div>
    </q-card-section>

    <q-separator spaced />

    <q-card-section class="q-pt-none">
      <div class="row q-col-gutter-md">
        <div v-for="f in visibleFields" :key="f.label" class="col-12 col-sm-6 col-lg-4">
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
  </q-card>
</template>

<script setup lang="ts">
  import { computed } from 'vue';
  import { useQuasar } from 'quasar';

  type Titulo = Record<string, any>;

  const props = defineProps<{ titulo: Titulo | null }>();

  const $q = useQuasar();

  /**
   * No FIELD_MAP você pode usar:
   * - key: 'campo' (string única)
   * - keys: ['alias1', 'alias2'] (primeiro valor não vazio será usado)
   * - type: 'currency' | 'number' | 'percent' | 'boolean'
   */
  type Field =
    | { label: string; key: string; type?: 'currency' | 'number' | 'percent' | 'boolean' }
    | {
        label: string;
        keys: string[];
        type?: 'currency' | 'number' | 'percent' | 'boolean';
      };

  const FIELD_MAP: Field[] = [
    { label: 'Empresa', key: 'empresa' },
    { label: 'Cliente', key: 'cliente' },
    { label: 'Nome Cliente', key: 'nomeClte' },

    // centro de custo com alias
    { label: 'Centro de Custo', keys: ['ccusto', 'centroCusto'] },
    { label: 'Nome Centro de Custo', key: 'nomeCcusto' },

    { label: 'Número Doc', key: 'numeDoc' },
    { label: 'Sequência', key: 'sequencia' },
    { label: 'Sequência Reparcela', key: 'sequenciaReparcela' },

    { label: 'Lote', key: 'lote' },
    { label: 'Lote Loteamento', key: 'loteLoteamento' },
    { label: 'Quadra', key: 'quadra' },

    { label: 'Data Base', key: 'dataBase' },
    { label: 'Data Emissão', key: 'dtEmissao' },
    { label: 'Data Depósito', key: 'dtDeposito' },
    { label: 'Data Gravação', key: 'dataGravacao' },
    { label: 'Data Reparcela', key: 'dataReparcela' },
    { label: 'Vencimento', key: 'vencimento' },

    { label: 'Valor', key: 'valor', type: 'currency' },
    { label: 'Valor Pago', key: 'valorPago', type: 'currency' },
    { label: 'Valor Depósito', key: 'valorDeposito', type: 'currency' },
    { label: 'Valor Juros', key: 'valorJuros', type: 'currency' },
    { label: 'Valor Desconto', key: 'valorDesc', type: 'currency' },
    { label: 'Valor Multa', key: 'valorMulta', type: 'currency' },
    { label: 'Valor VM', key: 'valorVm', type: 'currency' },
    { label: 'Valor Seguro', key: 'valorSeguro', type: 'currency' },

    { label: 'Tipo Doc', key: 'tipoDoc' },
    { label: 'Série', key: 'serie' },
    { label: 'Moeda', key: 'moeda' },

    { label: 'Fatura', key: 'fatura' },
    { label: 'Número Fatura', key: 'numeFatura' },
    { label: 'Número Depósito', key: 'numeDeposito' },

    { label: 'Denominação', key: 'denominacao' },
    { label: 'Receita', key: 'receita' },

    { label: 'Tipo Depósito', key: 'tipoDeposito' },
    { label: 'Descrição Tipo Depósito', key: 'descricaoTipoDeposito' },

    { label: 'Tipo Baixa', key: 'tipoBaixa' },
    { label: 'Descrição Tipo Baixa', key: 'descricaoTipoBaixa' },

    { label: 'Dias Atraso', key: 'diasAtraso', type: 'number' },

    { label: 'Banco', key: 'banco' },
    { label: 'Código Banco', key: 'codigoBanco' },
    { label: 'Nome Banco', key: 'nomeBanco' },
    { label: 'Agência', key: 'agencia' },
    { label: 'Conta', key: 'conta' },

    { label: 'Unidade de Custo', key: 'unidadeDeCusto' },
    { label: 'Quadra Original', key: 'quadraOriginal' },
    { label: 'Lote Original', key: 'loteOriginal' },
    { label: 'Unidade Custo Original', key: 'unidadeDeCustoOriginal' },

    { label: 'Total Reparcela', key: 'totalReparcela', type: 'currency' },

    { label: 'Observação', key: 'observacao' },
    { label: 'Observação Contrato', key: 'observacaoContrato' },
  ];

  /** —— Helpers de valor/visibilidade —— */
  function firstNonEmpty(...vals: any[]) {
    for (const v of vals) {
      if (v !== undefined && v !== null && String(v) !== '') return v;
    }
    return undefined;
  }

  function getRawValue(f: Field) {
    if ('key' in f) return props.titulo?.[f.key];
    return firstNonEmpty(...f.keys.map((k) => props.titulo?.[k]));
  }

  const visibleFields = computed(() => FIELD_MAP.filter((f) => getRawValue(f) !== undefined));

  /** —— Formatação —— */
  const brCurrency = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' });
  const numberFmt = new Intl.NumberFormat('pt-BR');

  function formatValue(f: Field) {
    const v: any = getRawValue(f);
    switch (f.type) {
      case 'currency':
        return isFinite(+v) ? brCurrency.format(+v) : v != null ? String(v) : '';
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

  /** —— UX —— */
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
