<template>
  <div class="q-pa-md">
    <h1 class="q-mt-lg q-mb-md">Títulos a Receber</h1>

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

            <div class="q-mt-md">
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
            </div>
          </q-card-section>

          <q-card-actions align="right">
            <q-btn label="Buscar" color="primary" @click="buscarTitulos()" />
            <q-btn label="Limpar" color="secondary" flat @click="limparFiltros" />
          </q-card-actions>
        </q-card>
      </div>
    </q-slide-transition>

    <!-- Ações em massa -->
    <div class="row items-center justify-between q-mb-sm">
      <div class="text-caption">
        <strong>{{ totalSelecionados }}</strong> selecionado(s) no total
        <span v-if="selecionadosPagina.length">
          • {{ selecionadosPagina.length }} nesta página</span
        >
      </div>
      <div class="row q-gutter-sm">
        <q-btn
          outline
          icon="clear_all"
          label="Limpar seleção"
          :disable="totalSelecionados === 0 || carregando"
          @click="limparSelecaoTotal"
        />
        <q-separator vertical inset />
        <q-btn
          color="positive"
          icon="receipt_long"
          label="Gerar Boletos"
          :disable="totalSelecionados === 0 || carregando"
          @click="confirmarGerar"
        />
        <q-btn
          color="negative"
          icon="delete"
          label="Apagar Boletos"
          :disable="totalSelecionados === 0 || carregando"
          @click="confirmarApagar"
        />
      </div>
    </div>

    <!-- Tabela -->
    <q-table
      :rows="titulos"
      :columns="colunas"
      row-key="id"
      :loading="carregando"
      v-model:pagination="paginacao"
      @request="buscarTitulos"
      no-data-label="Nenhum título encontrado."
      selection="multiple"
      v-model:selected="selecionadosPagina"
      :rows-per-page-options="[5, 10, 20, 50]"
      :selected-rows-label="selectedRowsLabel"
      @row-click="verDetalhes"
    >
      <!-- header custom: tri-state + multisort (sem shift) -->
      <template #header="h">
        <q-tr :props="h">
          <!-- 1) Coluna de seleção SEMPRE primeiro, com a mesma largura do body-selection -->
          <q-th auto-width>
            <q-checkbox
              :model-value="todaPaginaSelecionada"
              :indeterminate="algumaPaginaSelecionada && !todaPaginaSelecionada"
              @update:model-value="toggleSelecionarPagina"
            />
          </q-th>

          <!-- 2) Demais colunas, clicáveis para tri-state/multisort -->
          <q-th
            v-for="col in h.cols"
            :key="col.name"
            :props="h"
            class="cursor-pointer"
            role="button"
            :aria-sort="
              dirOf(col.name) === 'asc'
                ? 'ascending'
                : dirOf(col.name) === 'desc'
                  ? 'descending'
                  : 'none'
            "
            @click.stop="handleCycleSort(col.name)"
          >
            <div class="row items-center no-wrap">
              <span>{{ col.label }}</span>
              <!-- placeholder para evitar 'pulo' quando o ícone aparece/desaparece -->
              <q-icon
                v-if="dirOf(col.name) === 'asc'"
                name="arrow_upward"
                size="16px"
                class="q-ml-xs"
              />
              <q-icon
                v-else-if="dirOf(col.name) === 'desc'"
                name="arrow_downward"
                size="16px"
                class="q-ml-xs"
              />
              <span v-else class="sort-placeholder q-ml-xs" />
            </div>
          </q-th>
        </q-tr>
      </template>

      <!-- checkbox por linha (não navegar ao clicar) -->
      <template #body-selection="scope">
        <q-checkbox v-model="scope.selected" @click.stop />
      </template>
    </q-table>
  </div>
</template>

<script setup lang="ts">
  import { computed, ref, watch, onMounted } from 'vue';
  import { useRouter } from 'vue-router';
  import { useQuasar } from 'quasar';
  import { apiClientes } from 'src/boot/axios';
  import { useColunasTitulosAReceber } from 'src/composables/useColunasTitulos';
  import { useTriStateMultiSort } from 'src/composables/useTriStateMultiSort';

  type Titulo = {
    vencimento?: string;
    id: string;
    numeDoc?: string;
    sequencia?: string | number;
    cliente?: string;
    nomeClte?: string;
    empresa?: string;
    valor?: number | string;
  };

  const $q = useQuasar();
  const router = useRouter();

  const titulos = ref<Titulo[]>([]);
  const carregando = ref(false);
  const filtrosVisiveis = ref(false);

  const paginacao = ref({ page: 1, rowsPerPage: 10, rowsNumber: 0 });
  const colunas = useColunasTitulosAReceber();

  const {
    dirOf,
    cycleSort,
    buildSortQuery,
    init: initSort,
  } = useTriStateMultiSort({
    storageKey: 'titulos-a-receber:sort',
    persist: true,
  });

  function handleCycleSort(field: string) {
    cycleSort(field);
    // recarrega mantendo paginação atual
    buscarTitulos({ pagination: paginacao.value });
  }

  /** ---- Filtros ---- */
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
  ];
  const operadores = ['EQUALS', 'CONTAINS', 'STARTS_WITH', 'ENDS_WITH'] as const;
  const novoFiltro = ref<{ key: string; value: string; operator: (typeof operadores)[number] }>({
    key: '',
    value: '',
    operator: 'EQUALS',
  });
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

  /** ---- Seleção persistente entre páginas ---- */
  const selectedIds = ref<Set<string>>(new Set());
  const selecionadosPagina = ref<Titulo[]>([]);

  const idsDaPagina = computed(() => new Set(titulos.value.map((t) => t.id)));
  const totalSelecionados = computed(() => selectedIds.value.size);
  const selectedRowsLabel = (n: number) => (n ? `${n} nesta página` : '');

  function syncSelecionadosDaPagina() {
    selecionadosPagina.value = titulos.value.filter((t) => selectedIds.value.has(t.id));
  }
  function aplicarSelecaoDaPagina() {
    idsDaPagina.value.forEach((id) => selectedIds.value.delete(id));
    selecionadosPagina.value.forEach((t) => selectedIds.value.add(t.id));
  }
  watch(selecionadosPagina, () => aplicarSelecaoDaPagina());

  const todaPaginaSelecionada = computed(
    () => titulos.value.length > 0 && titulos.value.every((t) => selectedIds.value.has(t.id))
  );
  const algumaPaginaSelecionada = computed(() =>
    titulos.value.some((t) => selectedIds.value.has(t.id))
  );
  function toggleSelecionarPagina(val: boolean) {
    if (val) selecionarPaginaAtual();
    else limparSelecaoDaPagina();
  }
  function selecionarPaginaAtual() {
    titulos.value.forEach((t) => selectedIds.value.add(t.id));
    syncSelecionadosDaPagina();
  }
  function limparSelecaoDaPagina() {
    titulos.value.forEach((t) => selectedIds.value.delete(t.id));
    syncSelecionadosDaPagina();
  }
  function limparSelecaoTotal() {
    selectedIds.value.clear();
    syncSelecionadosDaPagina();
  }

  /** ---- Data ---- */
  async function buscarTitulos(props?: any) {
    carregando.value = true;
    try {
      if (props?.pagination) {
        paginacao.value.page = props.pagination.page;
        paginacao.value.rowsPerPage = props.pagination.rowsPerPage;
      }

      // inclui múltiplos sort na query (?sort=campo,asc&sort=outro,desc)
      const url =
        `/clientes-api/v1/admin/a-receber/search` +
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

      const { data } = await apiClientes.post(url, body);
      titulos.value = data.content;
      paginacao.value.rowsNumber = data.totalElements;

      syncSelecionadosDaPagina();
    } catch (err: any) {
      if (err.response?.status === 403) router.push('/login');
      console.error('Erro ao buscar títulos a receber:', err);
      $q.notify({ type: 'negative', message: 'Erro ao buscar títulos.' });
    } finally {
      carregando.value = false;
    }
  }

  function verDetalhes(_: Event, row: Titulo) {
    router.push({ path: '/admin/app/a-receber/detalhes', query: { id: row.id } });
  }

  /** ---- Ações em massa (assíncronas) ---- */
  const ENDPOINT_PROCESSAR = '/clientes-api/v1/admin/a-receber/processamento-de-boletos';
  type TipoProcessamento = 'INCLUSAO' | 'REMOCAO';

  async function processarBoletos(tipo: TipoProcessamento) {
    if (totalSelecionados.value === 0) return;
    carregando.value = true;
    try {
      const payload = {
        tipoProcessamentoBoleto: tipo,
        titulosAReceberIdList: Array.from(selectedIds.value),
      };

      await apiClientes.post(ENDPOINT_PROCESSAR, payload);

      $q.notify({
        type: 'info',
        message:
          tipo === 'INCLUSAO'
            ? 'Solicitação de geração de boletos enviada com sucesso.'
            : 'Solicitação de exclusão de boletos enviada com sucesso.',
        caption:
          'O processamento ocorrerá em segundo plano. Acompanhe o status na página "Sincronizações de Boletos".',
        timeout: 6000,
      });

      limparSelecaoTotal();
      await buscarTitulos({ pagination: paginacao.value });
    } catch (err) {
      console.error('Erro ao processar boletos:', err);
      $q.notify({
        type: 'negative',
        message:
          tipo === 'INCLUSAO'
            ? 'Falha ao solicitar geração de boletos.'
            : 'Falha ao solicitar exclusão de boletos.',
        caption: 'Verifique a conexão ou tente novamente mais tarde.',
      });
    } finally {
      carregando.value = false;
    }
  }

  function confirmarGerar() {
    if (totalSelecionados.value === 0) return;
    $q.dialog({
      title: 'Gerar Boletos',
      message: `Deseja solicitar a geração de boletos para ${totalSelecionados.value} título(s)?`,
      cancel: true,
      ok: { label: 'Solicitar', color: 'positive' },
    }).onOk(() => processarBoletos('INCLUSAO'));
  }

  function confirmarApagar() {
    if (totalSelecionados.value === 0) return;
    $q.dialog({
      title: 'Apagar Boletos',
      message:
        'Deseja solicitar a remoção dos boletos dos títulos selecionados? O processo será executado em segundo plano.',
      cancel: true,
      ok: { label: 'Solicitar', color: 'negative' },
    }).onOk(() => processarBoletos('REMOCAO'));
  }

  /** ---- Inicializações ---- */
  onMounted(() => {
    initSort(); // carrega ordenação (ou aplica o padrão) e garante "sempre 1+"
    buscarTitulos({ pagination: paginacao.value }); // já busca com sort ativo
    // seleção persistida na sessão
    const raw = sessionStorage.getItem('titulosSelecionados');
    if (raw) selectedIds.value = new Set(JSON.parse(raw));
    watch(
      selectedIds,
      () =>
        sessionStorage.setItem(
          'titulosSelecionados',
          JSON.stringify(Array.from(selectedIds.value))
        ),
      { deep: true }
    );
  });
</script>

<style scoped>
  .text-h6 {
    font-size: 1.25rem;
    font-weight: bold;
  }
  .sort-placeholder {
    display: inline-block;
    width: 16px;
    height: 16px;
  }
</style>
