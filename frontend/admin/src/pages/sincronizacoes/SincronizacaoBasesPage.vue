<template>
  <div class="q-pa-md">
    <div class="row items-center justify-between q-mb-md">
      <h1 class="q-mt-lg q-mb-none">Sincronização de Bases de Dados</h1>

      <div class="row items-center no-wrap q-gutter-sm">
        <q-btn
          dense
          round
          flat
          icon="refresh"
          :loading="loading"
          @click="refreshTable"
          aria-label="Atualizar lista"
        >
          <q-tooltip>Atualizar</q-tooltip>
        </q-btn>

        <q-btn
          label="Sincronizar"
          color="primary"
          icon="sync"
          unelevated
          @click="triggerManualDbsSync"
        />
      </div>
    </div>

    <q-table
      ref="tableRef"
      :rows="syncJobs"
      :columns="columns"
      row-key="id"
      :loading="loading"
      v-model:pagination="pagination"
      @request="fetchApi"
      no-data-label="Nenhuma Sincronização encontrada."
      :rows-per-page-options="[10, 20, 50]"
    >
      <!-- Header custom: tri-state + multiordem (todas ordenáveis) -->
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
  import { useQuasar } from 'quasar';
  import { apiClientes } from 'src/boot/axios';
  import { useTriStateMultiSort } from 'src/composables/useTriStateMultiSort';

  const $q = useQuasar();

  // Refs
  const tableRef = ref();
  const syncJobs = ref<any[]>([]);
  const loading = ref(false);
  const router = useRouter();

  const pagination = ref({ page: 1, rowsPerPage: 10, rowsNumber: 0 });

  const columns = [
    { name: 'startedAt', align: 'left' as const, label: 'Iniciado em', field: 'startedAt' },
    { name: 'finishedAt', align: 'left' as const, label: 'Finalizado em', field: 'finishedAt' },
    { name: 'type', align: 'left' as const, label: 'Tipo', field: 'type' },
    { name: 'status', align: 'left' as const, label: 'Status', field: 'status' },
    { name: 'message', align: 'left' as const, label: 'Mensagem', field: 'message' },
  ];

  /** -------- Ordenação (useTriStateMultiSort) -------- */
  const {
    dirOf,
    cycleSort,
    buildSortQuery,
    init: initSort,
  } = useTriStateMultiSort({
    storageKey: 'sync-bases:list:sort',
    persist: true,
  });

  function handleCycleSort(field: string) {
    cycleSort(field);
    refreshTable();
  }

  // -------- Listagem (tabela) --------
  const fetchApi = async (props?: { pagination?: any }) => {
    loading.value = true;
    try {
      // Usa a paginação recebida pelo @request OU a atual
      const p = props?.pagination ?? pagination.value;

      pagination.value.page = p?.page ?? pagination.value.page ?? 1;
      pagination.value.rowsPerPage = p?.rowsPerPage ?? pagination.value.rowsPerPage ?? 10;

      const url =
        `/clientes-api/v1/admin/sync/SINCRONIZAR_BASES` +
        `?page=${pagination.value.page}&size=${pagination.value.rowsPerPage}` +
        `${buildSortQuery()}`;

      const response = await apiClientes.get(url, {
        headers: { 'Content-Type': 'application/json' },
      });

      syncJobs.value = response.data.content ?? [];
      pagination.value.rowsNumber = response.data.totalElements || 0;
    } catch (error: any) {
      if (error.response?.status === 403) {
        router.push('/login');
      } else {
        console.error('Erro ao buscar configurações:', error);
      }
    } finally {
      loading.value = false;
    }
  };

  // Botão de refresh / disparador padrão
  const refreshTable = () => {
    if (tableRef.value?.requestServerInteraction) {
      tableRef.value.requestServerInteraction();
    } else {
      fetchApi({ pagination: pagination.value });
    }
  };

  const triggerManualDbsSync = async () => {
    try {
      const response = await apiClientes.post(
        `/clientes-api/v1/admin/sync/SINCRONIZAR_BASES`,
        {}
      );
      if (response.status == 201) {
        $q.notify({ type: 'positive', message: `Sincronização solicitada com sucesso.` });
      }
    } catch (e) {
      console.error(e);
      $q.notify({ type: 'negative', message: 'Erro ao solicitar sincronização.' });
    } finally {
      refreshTable();
    }
  };

  // Init
  onMounted(() => {
    initSort(); // carrega ordenação persistida (ou nenhuma)
    refreshTable();
  });
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

  /* esconde o ícone nativo do Quasar no hover */
  :deep(.q-table thead tr > th .q-table__sort-icon) {
    display: none !important;
  }
</style>
