<template>
  <div class="q-pa-md">
    <div class="row items-center justify-between q-mb-md">
      <h1 class="q-mt-lg q-mb-none">Sincronização de Boletos</h1>

      <div class="row items-center no-wrap q-gutter-sm">
        <q-btn
          dense
          round
          flat
          icon="refresh"
          :loading="loading"
          @click="requestServer"
          aria-label="Atualizar lista"
        >
          <q-tooltip>Atualizar</q-tooltip>
        </q-btn>
        <q-btn
          dense
          flat
          icon="sort"
          label="Limpar ordenação"
          :disable="sortState.length === 0"
          @click="handleClearSort"
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
      <template #header="h">
        <q-tr :props="h">
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
            @click="handleCycleSort(col.name)"
          >
            <div class="row items-center no-wrap">
              <span>{{ col.label }}</span>
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
            </div>
          </q-th>
        </q-tr>
      </template>
    </q-table>
  </div>
</template>

<script setup lang="ts">
  import { ref, watch, onMounted } from 'vue';
  import { useRouter } from 'vue-router';
  import { apiClientes } from 'src/boot/axios';
  import { useTriStateMultiSort } from 'src/composables/useTriStateMultiSort';

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

  const { sortState, dirOf, cycleSort, clearSort, buildSortQuery, init } = useTriStateMultiSort({
    storageKey: 'sync-boletos:sort',
    persist: true,
  });

  onMounted(() => {
    init(); // carrega do storage e aplica fallback padrão
    requestServer(); // já traz a 1ª página com o sort padrão
  });

  function handleCycleSort(field: string) {
    cycleSort(field);
    requestServer();
  }

  function handleClearSort() {
    clearSort();
    requestServer();
  }

  // -------- Listagem (tabela) --------
  const fetchApi = async (props?: { pagination?: any }) => {
    loading.value = true;
    try {
      const p = props?.pagination ?? pagination.value;
      pagination.value.page = p?.page ?? 1;
      pagination.value.rowsPerPage = p?.rowsPerPage ?? 10;

      const url =
        `/clientes-api/v1/admin/sync/SINCRONIZAR_BOLETOS` +
        `?page=${pagination.value.page - 1}` + // Spring 0-based
        `&size=${pagination.value.rowsPerPage}` +
        `${buildSortQuery()}`;

      const response = await apiClientes.get(url, {
        headers: { 'Content-Type': 'application/json' },
      });
      syncJobs.value = response.data.content ?? [];
      pagination.value.rowsNumber = response.data.totalElements || 0;
    } catch (error: any) {
      if (error.response?.status === 403) router.push('/login');
      else console.error('Erro ao buscar sincronizações:', error);
    } finally {
      loading.value = false;
    }
  };

  // Paginacão
  watch(
    () => pagination.value,
    (p) => fetchApi({ pagination: p }),
    { deep: true, immediate: false }
  );

  function requestServer() {
    if (tableRef.value?.requestServerInteraction) tableRef.value.requestServerInteraction();
    else fetchApi({ pagination: pagination.value });
  }
</script>

<style scoped>
  .text-h6 {
    font-size: 1.25rem;
    font-weight: bold;
  }
</style>
