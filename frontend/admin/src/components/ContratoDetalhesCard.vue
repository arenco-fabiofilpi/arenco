<template>
  <q-card v-if="contrato" flat bordered class="q-pa-md">
    <q-card-section class="q-pb-none">
      <div class="text-h6">Detalhes do Contrato</div>
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

  type Contrato = Record<string, any>;

  const props = defineProps<{ contrato: Contrato | null }>();

  const $q = useQuasar();

  /**
   * No FIELD_MAP você pode usar:
   * - key: 'campo' (string única)
   * - keys: ['alias1', 'alias2'] (primeiro valor não vazio será usado)
   * - type:  'currency' | 'number' | 'percent' | 'boolean'
   */
  type Field =
    | {
        label: string;
        key: string;
        type?: 'currency' | 'number' | 'percent' | 'boolean';
      }
    | {
        label: string;
        keys: string[];
        type?: 'currency' | 'number' | 'percent' | 'boolean';
      };

  const FIELD_MAP: Field[] = [
    // Identificação
    { label: 'Número Contrato', key: 'numeContrato' },
    { label: 'Empresa', key: 'empresa' },
    { label: 'Nome da Empresa', key: 'nomeEmpresa' },
    { label: 'CNPJ Empresa', key: 'cnpjEmpresa' },
    { label: 'Cidade Empresa', key: 'cidadeEmpresa' },
    { label: 'Endereço Empresa', key: 'enderecoCompletoEmpresa' },

    // Cliente
    { label: 'Cliente', key: 'cliente' },
    { label: 'Nome Cliente', key: 'nomeCliente' },
    { label: 'CPF/CIC Cliente', key: 'cic' },
    { label: 'RG Cliente', key: 'rgCliente' },
    { label: 'Data Nascimento Cliente', key: 'clienteNascimento' },
    { label: 'Endereço Cliente', key: 'enderecoCompletoCliente' },
    { label: 'Cidade Cliente', key: 'cidade' },
    { label: 'Estado Cliente', key: 'estado' },
    { label: 'CEP Cliente', key: 'cepCompleto' },
    { label: 'Telefones Cliente', key: 'telefones' },

    // Contrato
    { label: 'Contrato Original', key: 'numeContratoOriginal' },
    { label: 'Contrato Anterior', key: 'numeContratoAnterior' },
    { label: 'Número Contrato Matriz', key: 'numeroContratoMatriz' },
    { label: 'Número Contrato Interno', key: 'numeroContratoInterno' },
    { label: 'Observação Contrato', key: 'observacaoContrato' },

    // Financeiro
    { label: 'Valor Contrato', key: 'valorContrato' },
    { label: 'Valor Contrato Extenso', key: 'valorContratoExtenso' },
    { label: 'Receita', key: 'receita' },
    { label: 'Denominação', key: 'denominacao' },
    { label: 'Total Saldo em Aberto', key: 'totalSaldoEmAberto' },

    // Unidade de Custo
    { label: 'Centro de Custo', key: 'ccusto' },
    { label: 'Nome Centro de Custo', key: 'nomeCcusto' },
    { label: 'Unidade de Custo', key: 'unidadeDeCusto' },
    { label: 'Unidade de Custo Original', key: 'unidadeDeCustoOriginal' },
    { label: 'Quadra Original', key: 'quadraOriginal' },
    { label: 'Lote Original', key: 'loteOriginal' },

    // Datas principais
    { label: 'Data Base', key: 'dataBase' },
    { label: 'Data Emissão', key: 'dtEmissao' },
    { label: 'Data Inclusão', key: 'dataInclusao' },
    { label: 'Data Rescisão', key: 'dataRescisao' },
    { label: 'Data Entrega Unidade', key: 'dataEntregaUnidade' },

    // Corretor/Vendedor
    { label: 'Nome Corretor', key: 'nomeCorretor' },
    { label: 'Nome Vendedor', key: 'nomeVendedor' },

    // Conjuge
    { label: 'Nome Cônjuge', key: 'conjugueNome' },
    { label: 'CPF Cônjuge', key: 'cpfConjugue' },

    // Diversos
    { label: 'Descrição', key: 'descricao' },
    { label: 'Tipo Documento', key: 'tipoDoc' },
    { label: 'Tipo Unidade', key: 'tipoUnidade' },
    { label: 'Unidade/Quadra/Lote', key: 'unidadeQuadraLote' },
    { label: 'Usuário Inclusão', key: 'usuarioInclusao' },
    { label: 'Observação Cadastro Cliente', key: 'observacaoCadastroCliente' },
    { label: 'ID', key: 'id' },
  ];

  /** —— Helpers de valor/visibilidade —— */
  function firstNonEmpty(...vals: any[]) {
    for (const v of vals) {
      if (v !== undefined && v !== null && String(v) !== '') return v;
    }
    return undefined;
  }

  function getRawValue(f: Field) {
    if ('key' in f) return props.contrato?.[f.key];
    return firstNonEmpty(...f.keys.map((k) => props.contrato?.[k]));
  }

  const visibleFields = computed(() => FIELD_MAP.filter((f) => getRawValue(f) !== undefined));

  /** —— Formatação —— */
  const numberFmt = new Intl.NumberFormat('pt-BR');

  function formatValue(f: Field) {
    const v: any = getRawValue(f);
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
