<template>
  <div class="q-pa-md">
    <h1 class="q-mt-lg q-mb-md">Títulos Recebidos</h1>

    <!-- Filtros -->
    <q-btn
      color="primary"
      icon="filter_list"
      label="Filtros"
      class="q-mb-md"
      @click="filtrosVisiveis = !filtrosVisiveis"
    />

    <q-slide-transition>
      <div v-show="filtrosVisiveis">
        <q-card class="q-mb-md">
          <q-card-section>
            <div class="text-h6 q-mb-sm">Filtros</div>
            <div class="row q-col-gutter-md items-end">
              <q-select
                filled
                v-model="novoFiltro.key"
                :options="filtroOptions"
                label="Campo"
                class="col-12 col-md-3"
              />
              <q-select
                filled
                v-model="novoFiltro.operator"
                :options="operadores"
                label="Operador"
                class="col-12 col-md-3"
              />
              <q-input filled v-model="novoFiltro.value" label="Valor" class="col-12 col-md-4" />
              <div class="col-12 col-md-2">
                <q-btn label="Adicionar" color="primary" @click="adicionarFiltro" />
              </div>
            </div>

            <div class="q-mt-md">
              <div class="text-subtitle2 q-mb-sm">Filtros Aplicados</div>
              <div v-if="filtros.length === 0">Nenhum filtro aplicado</div>
              <q-chip
                v-for="(filtro, index) in filtros"
                :key="index"
                removable
                @remove="removerFiltro(index)"
                class="q-mr-sm q-mb-sm"
              >
                {{ filtro.key }} {{ filtro.operator }} "{{ filtro.value }}"
              </q-chip>
            </div>

            <q-option-group
              v-model="joinFiltersOperator"
              :options="[
                { label: 'E', value: 'AND' },
                { label: 'OU', value: 'OR' },
              ]"
              type="radio"
              inline
              label="Combinar filtros por"
            />
          </q-card-section>

          <q-card-actions align="right">
            <q-btn label="Buscar" color="primary" @click="buscarTitulos" />
            <q-btn label="Limpar" color="secondary" flat @click="limparFiltros" />
          </q-card-actions>
        </q-card>
      </div>
    </q-slide-transition>

    <!-- Tabela -->
    <q-table
      ref="tableRef"
      :rows="titulos"
      :columns="colunas"
      row-key="id"
      :loading="carregando"
      v-model:pagination="paginacao"
      @request="buscarTitulos"
      no-data-label="Nenhum título encontrado."
      @row-click="verDetalhes"
      :rows-per-page-options="[10, 20, 50]"
    >
      <!-- Header custom: tri-state + multiordem -->
      <template #header="h">
        <q-tr :props="h">
          <q-th
            v-for="col in h.cols"
            :key="col.name"
            :props="h"
            class="header-th cursor-pointer"
            :aria-sort="
              dirOf(col.name) === 'asc'
                ? 'ascending'
                : dirOf(col.name) === 'desc'
                  ? 'descending'
                  : 'none'
            "
            @click.stop="handleCycleSort(col.name)"
          >
            <span class="header-wrap" :class="[`align-${col.align || 'left'}`]">
              <span class="header-label">{{ col.label }}</span>
              <q-icon
                v-if="dirOf(col.name) === 'asc'"
                name="arrow_upward"
                class="sort-icon"
                size="16px"
              />
              <q-icon
                v-else-if="dirOf(col.name) === 'desc'"
                name="arrow_downward"
                class="sort-icon"
                size="16px"
              />
              <span v-else class="sort-spacer"></span>
            </span>
          </q-th>
        </q-tr>
      </template>
    </q-table>
  </div>
</template>

<script setup lang="ts">
  import { ref, onMounted } from 'vue';
  import { useRouter } from 'vue-router';
  import { apiClientes } from 'src/boot/axios';
  import { useColunasTitulosRecebidos } from 'src/composables/useColunasTitulos';
  import { useTriStateMultiSort } from 'src/composables/useTriStateMultiSort';

  const tableRef = ref();
  const titulos = ref<any[]>([]);
  const carregando = ref(false);
  const router = useRouter();
  const filtrosVisiveis = ref(false);

  const paginacao = ref({ page: 1, rowsPerPage: 10, rowsNumber: 0 });
  const colunas = useColunasTitulosRecebidos();

  /** -------- Ordenação (useTriStateMultiSort) -------- */
  const {
    dirOf,
    cycleSort,
    buildSortQuery,
    init: initSort,
  } = useTriStateMultiSort({
    storageKey: 'titulos-recebidos:list:sort',
    persist: true,
  });
  function handleCycleSort(field: string) {
    cycleSort(field);
    requestServer();
  }

  /** -------- Filtros -------- */
  const filtroOptions = [
    'uuidContrato',
    'empresa',
    'cliente',
    'numeDoc',
    'sequencia',
    'dtEmissao',
    'dataBase',
    'vencimento',
    'nomeClte',
    'fatura',
    'numeFatura',
    'observacao',
    'nomeCcusto',
    'ccusto',
    'descricao',
    'lote',
    'serie',
    'dtDeposito',
  ];
  const operadores = ['EQUALS', 'CONTAINS', 'STARTS_WITH', 'ENDS_WITH'] as const;

  const novoFiltro = ref({ key: '', value: '', operator: 'EQUALS' });
  const filtros = ref([] as { key: string; value: string; operator: string }[]);
  const joinFiltersOperator = ref<'AND' | 'OR'>('AND');

  function adicionarFiltro() {
    if (novoFiltro.value.key && novoFiltro.value.value) {
      filtros.value.push({ ...novoFiltro.value });
      novoFiltro.value.value = '';
    }
  }
  function removerFiltro(index: number) {
    filtros.value.splice(index, 1);
  }
  function limparFiltros() {
    filtros.value = [];
    novoFiltro.value = { key: '', value: '', operator: 'EQUALS' };
    buscarTitulos();
  }

  /** -------- Data @request -------- */
  async function buscarTitulos(props?: any) {
    carregando.value = true;
    try {
      if (props?.pagination) {
        paginacao.value.page = props.pagination.page ?? paginacao.value.page;
        paginacao.value.rowsPerPage = props.pagination.rowsPerPage ?? paginacao.value.rowsPerPage;
      }

      const url =
        `/clientes-api/v1/admin/recebidos/search` +
        `?page=${paginacao.value.page}&size=${paginacao.value.rowsPerPage}` +
        `${buildSortQuery()}`;

      let body: any = {};
      if (filtros.value.length > 0) {
        body = filtros.value.reduce(
          (acc, f) => {
            acc[f.key] = f.value;
            return acc;
          },
          {} as Record<string, string>
        );
        body.joinFiltersOperator = joinFiltersOperator.value;
      }

      const resp = await apiClientes.post(url, body, { headers: {} });
      const pageData = resp.data;

      titulos.value = pageData.content ?? [];
      paginacao.value.rowsNumber = pageData.totalElements ?? 0;
    } catch (err: any) {
      if (err.response?.status === 403) router.push('/login');
      console.error('Erro ao buscar títulos recebidos:', err);
    } finally {
      carregando.value = false;
    }
  }

  /** -------- Disparador padrão do QTable -------- */
  function requestServer() {
    if (tableRef.value?.requestServerInteraction) tableRef.value.requestServerInteraction();
    else buscarTitulos({ pagination: paginacao.value });
  }

  /** -------- Init -------- */
  onMounted(() => {
    initSort(); // carrega ordenação persistida (ou nenhuma)
    requestServer(); // primeira busca via @request
  });

  function verDetalhes(evt: Event, row: any) {
    router.push({ path: '/admin/app/recebidos/detalhes', query: { id: row.id } });
  }
</script>

<style scoped>
  /* Cabeçalho alinhado e sem quebra */
  .q-table thead tr > th {
    white-space: nowrap;
    vertical-align: middle;
  }

  .header-th {
    vertical-align: middle;
  }
  .header-wrap {
    display: inline-flex;
    align-items: center;
    gap: 4px;
    max-width: 100%;
  }
  .header-wrap.align-right {
    justify-content: flex-end;
  }
  .header-wrap.align-center {
    justify-content: center;
  }
  .header-wrap.align-left {
    justify-content: flex-start;
  }

  .header-label {
    display: inline-block;
    max-width: 100%;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  /* espaço do ícone de sort (evita “pulo”) */
  .sort-icon,
  .sort-spacer {
    width: 16px;
    height: 16px;
    flex: 0 0 16px;
  }
  .sort-spacer {
    visibility: hidden;
  }

  /* esconde o ícone nativo do Quasar (hover) */
  :deep(.q-table thead tr > th .q-table__sort-icon) {
    display: none !important;
  }
</style>
