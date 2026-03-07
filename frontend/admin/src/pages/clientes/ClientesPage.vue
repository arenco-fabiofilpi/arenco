<template>
  <q-page class="q-pa-md">
    <h1 class="q-mt-lg q-mb-md">Clientes</h1>

    <!-- Barra de ações -->
    <div class="row items-center q-gutter-sm q-mb-md">
      <q-btn
        color="primary"
        icon="filter_list"
        label="Filtros"
        @click="filtrosVisiveis = !filtrosVisiveis"
      />

      <q-space />

      <q-btn
        outline
        color="negative"
        icon="clear"
        label="Limpar seleção"
        :disable="selectedClientes.length === 0"
        @click="limparSelecao"
      />

      <q-btn
        outline
        color="primary"
        icon="done_all"
        :label="allPageSelected ? 'Desmarcar todos (página)' : 'Selecionar todos (página)'"
        :disable="clientes.length === 0"
        @click="toggleSelectAllPage"
      />

      <q-btn
        color="positive"
        icon="download"
        label="Exportar"
        :disable="selectedClientes.length === 0"
        @click="exportarSelecionados(selectedIds)"
      />
    </div>

    <q-table
      ref="tableRef"
      :rows="clientes"
      :columns="columns"
      row-key="id"
      :loading="loading"
      v-model:pagination="pagination"
      v-model:selected="selectedClientes"
      selection="multiple"
      @request="fetchClientes"
      no-data-label="Nenhum cliente encontrado."
      :rows-per-page-options="[5, 10, 20, 50, 100, 150]"
    >
      <template #header="h">
        <q-tr :props="h">
          <q-th auto-width class="selection-header-th">
            <span class="selection-header-spacer" aria-hidden="true"></span>
          </q-th>

          <q-th
            v-for="col in h.cols"
            :key="col.name"
            :props="h"
            :class="['header-th', col.name !== 'selection' && isSortable(col) && 'cursor-pointer']"
            :aria-sort="
              col.name !== 'selection' && isSortable(col)
                ? dirOf(col.name) === 'asc'
                  ? 'ascending'
                  : dirOf(col.name) === 'desc'
                    ? 'descending'
                    : 'none'
                : 'none'
            "
            @click.stop="col.name !== 'selection' && isSortable(col) && handleCycleSort(col.name)"
          >
            <span class="header-wrap" :class="[`align-${col.align || 'left'}`]">
              <span class="header-label">{{ col.label }}</span>

              <q-icon
                v-if="isSortable(col) && dirOf(col.name) === 'asc'"
                name="arrow_upward"
                class="sort-icon"
                size="16px"
              />
              <q-icon
                v-else-if="isSortable(col) && dirOf(col.name) === 'desc'"
                name="arrow_downward"
                class="sort-icon"
                size="16px"
              />
              <span v-else class="sort-spacer"></span>
            </span>
          </q-th>
        </q-tr>
      </template>

      <!-- ações -->
      <template #body-cell-actions="props">
        <q-td :props="props">
          <q-btn flat round dense icon="more_vert">
            <q-menu>
              <q-list style="min-width: 200px">
                <q-item clickable v-close-popup @click="adicionarBetaUser(props.row)">
                  <q-item-section avatar>
                    <q-icon name="person_add" color="primary" />
                  </q-item-section>
                  <q-item-section>Adicionar à lista de Beta Users</q-item-section>
                </q-item>
              </q-list>
            </q-menu>
          </q-btn>
        </q-td>
      </template>
    </q-table>
  </q-page>
</template>

<script setup lang="ts">
  import { computed, ref, onMounted } from 'vue';
  import { useRouter } from 'vue-router';
  import { useQuasar, type QTableColumn } from 'quasar';
  import { apiClientes } from 'src/boot/axios';
  import { useTriStateMultiSort } from 'src/composables/useTriStateMultiSort';

  const $q = useQuasar();
  const router = useRouter();

  interface Filter {
    key: string;
    value: string;
    operator: string;
  }

  type ClienteRow = {
    id: string | number;
    idErp?: string;
    name?: string;
    username?: string;
    grupoContabil?: string;
    loginMethod?: string;
    enabled?: boolean;
  };

  const tableRef = ref<any>(null);
  const clientes = ref<ClienteRow[]>([]);
  const filtrosVisiveis = ref(false);
  const loading = ref(false);

  const selectedClientes = ref<ClienteRow[]>([]);
  const pagination = ref({ page: 1, rowsPerPage: 10, rowsNumber: 0 });

  /**
   * ✅ Tipagem correta: QTableColumn<ClienteRow>[]
   * field é obrigatório e agora o TS fica feliz.
   */
  const columns: QTableColumn<ClienteRow>[] = [
    {
      name: 'idErp',
      align: 'left',
      label: 'ID Eficaz',
      field: (row) => row.idErp ?? '',
      sortable: true,
    },
    { name: 'name', align: 'left', label: 'Nome', field: (row) => row.name ?? '', sortable: true },
    {
      name: 'username',
      align: 'left',
      label: 'CPF/CNPJ',
      field: (row) => row.username ?? '',
      sortable: true,
    },
    {
      name: 'grupoContabil',
      align: 'left',
      label: 'Grupo Contábil',
      field: (row) => row.grupoContabil ?? '',
      sortable: true,
    },
    {
      name: 'loginMethod',
      align: 'left',
      label: 'Método de Login',
      field: (row) => row.loginMethod ?? '',
      sortable: true,
    },
    {
      name: 'enabled',
      align: 'left',
      label: 'Habilitado',
      field: (row) => (row.enabled ? 'Sim' : 'Não'),
      sortable: true,
    },
    { name: 'actions', align: 'center', label: 'Ações', field: () => '', sortable: false },
  ];

  // helper para saber se coluna é ordenável
  const isSortable = (col: any) => col?.sortable !== false;

  // Ordenação (tri-state multi sort)
  const {
    sortState,
    dirOf,
    cycleSort,
    buildSortQuery,
    init: initSort,
  } = useTriStateMultiSort({
    storageKey: 'clientes:list:sort',
    persist: true,
  });

  function handleCycleSort(field: string) {
    cycleSort(field);
    requestServer();
  }

  function purgeNonSortableFromSort() {
    sortState.value = sortState.value.filter((s: { field: string }) => s.field !== 'actions');
  }

  /** -------- Filtros -------- */
  const filters = ref({ name: '', cpf: '', cnpj: '', idErp: '', email: '' });

  /** -------- Data @request -------- */
  const fetchClientes = async (props?: { pagination: any }) => {
    loading.value = true;
    try {
      const pag = props?.pagination ?? pagination.value;
      const pageParam = pag.page ?? 1; // troque para (pag.page - 1) se backend for 0-based
      const sizeParam = pag.rowsPerPage ?? 10;

      const filterDto: { filters: Filter[]; joinFiltersOperator: string } = {
        filters: [],
        joinFiltersOperator: 'AND',
      };

      if (filters.value.name)
        filterDto.filters.push({ key: 'NAME', value: filters.value.name, operator: 'CONTAINS' });
      if (filters.value.cpf)
        filterDto.filters.push({ key: 'CPF', value: filters.value.cpf, operator: 'EQUALS' });
      if (filters.value.cnpj)
        filterDto.filters.push({ key: 'CNPJ', value: filters.value.cnpj, operator: 'EQUALS' });
      if (filters.value.idErp)
        filterDto.filters.push({ key: 'ID_ERP', value: filters.value.idErp, operator: 'EQUALS' });
      if (filters.value.email)
        filterDto.filters.push({ key: 'EMAIL', value: filters.value.email, operator: 'CONTAINS' });

      const url =
        `/clientes-api/v1/customers-management/search` +
        `?page=${pageParam}&size=${sizeParam}` +
        `${buildSortQuery()}`;

      const resp = await apiClientes.post(
        url,
        { filterDto: filterDto.filters.length ? filterDto : undefined },
        { headers: { 'Content-Type': 'application/json' } }
      );

      const pageData = resp.data;
      clientes.value = pageData.content ?? [];
      pagination.value = { ...pag, rowsNumber: pageData.totalElements ?? 0 };
    } catch (error: any) {
      if (error.response?.status === 403) router.push('/login');
      else {
        console.error('Erro ao buscar clientes:', error);
        $q.notify({ type: 'negative', message: 'Erro ao buscar clientes.' });
      }
    } finally {
      loading.value = false;
    }
  };

  function requestServer() {
    if (tableRef.value?.requestServerInteraction) tableRef.value.requestServerInteraction();
    else fetchClientes({ pagination: pagination.value });
  }

  /** -------- Seleção (apenas página atual) -------- */
  const selectedIds = computed(() => new Set(selectedClientes.value.map((c) => c.id)));

  const allPageSelected = computed(() => {
    if (!clientes.value.length) return false;
    return clientes.value.every((r) => selectedIds.value.has(r.id));
  });

  function toggleSelectAllPage() {
    if (!clientes.value.length) return;

    if (allPageSelected.value) {
      const pageIds = new Set(clientes.value.map((r) => r.id));
      selectedClientes.value = selectedClientes.value.filter((c) => !pageIds.has(c.id));
    } else {
      const map = new Map<ClienteRow['id'], ClienteRow>(
        selectedClientes.value.map((c) => [c.id, c])
      );
      for (const r of clientes.value) map.set(r.id, r);
      selectedClientes.value = Array.from(map.values());
    }
  }

  function limparSelecao() {
    selectedClientes.value = [];
  }

  function downloadBlob(response: any, fallbackName: string) {
    let nomeArquivo = fallbackName;
    const disposition = response.headers['content-disposition'];
    if (disposition) {
      const match = disposition.match(/filename="?(.+?)"?$/);
      if (match) nomeArquivo = match[1];
    }

    const blob = new Blob([response.data], {
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
    });
    const link = document.createElement('a');
    link.href = URL.createObjectURL(blob);
    link.download = nomeArquivo;
    link.click();
    URL.revokeObjectURL(link.href);
  }

  async function exportarSelecionados(selectedIds: Set<string | number>) {
    if (!selectedClientes.value.length) return;
    const resp = await apiClientes.get(`/clientes-api/v1/exportar`, {
      params: { ids: [...selectedIds] },
      paramsSerializer: (params) =>
        Object.entries(params)
          .flatMap(([k, v]) => (Array.isArray(v) ? v.map((i) => `${k}=${i}`) : [`${k}=${v}`]))
          .join('&'),
      responseType: 'arraybuffer',
    });

    downloadBlob(resp, 'Clientes_Exportados.xlsx');

    $q.notify({
      type: 'positive',
      message: `${selectedClientes.value.length} clientes exportados.`,
    });
  }

  /** -------- Ações -------- */
  const adicionarBetaUser = async (cliente: ClienteRow) => {
    try {
      const response = await apiClientes.post(
        `/clientes-api/v1/settings/beta-users?userModelId=${cliente.id}`,
        {}
      );

      if (response.status === 201) {
        $q.notify({
          type: 'positive',
          message: `Cliente ${cliente.name ?? ''} adicionado aos Beta Users.`,
        });
      } else if (response.status === 208) {
        $q.notify({
          type: 'warning',
          message: `Cliente ${cliente.name ?? ''} já existente na lista de Beta Users.`,
        });
      }
    } catch (e) {
      $q.notify({ type: 'negative', message: 'Erro ao adicionar cliente aos Beta Users.' });
      console.error(e);
    }
  };

  onMounted(() => {
    initSort();
    purgeNonSortableFromSort();
    requestServer();
  });
</script>

<style scoped>
  .q-table thead tr > th {
    white-space: nowrap;
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

  .sort-icon,
  .sort-spacer {
    width: 16px;
    height: 16px;
    flex: 0 0 16px;
  }
  .sort-spacer {
    visibility: hidden;
  }

  .header-th:not(.cursor-pointer) {
    cursor: default;
  }

  :deep(.q-table thead tr > th .q-table__sort-icon) {
    display: none !important;
  }
  /* reserva exatamente o espaço visual do checkbox do Quasar (ajustável se necessário) */
  .selection-header-spacer {
    display: inline-block;
    width: 20px;
    height: 20px;
  }
</style>
