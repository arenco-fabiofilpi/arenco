<template>
  <div class="q-pa-md">
    <h1 class="q-mt-lg q-mb-md">Endereços</h1>

    <q-btn
      color="primary"
      icon="filter_list"
      label="Filtros"
      class="q-mb-md"
      @click="filtrosVisiveis = !filtrosVisiveis"
    />

    <q-table
      ref="tableRef"
      :rows="enderecos"
      :columns="columns"
      row-key="id"
      :loading="loading"
      v-model:pagination="pagination"
      @request="fetchApi"
      no-data-label="Nenhum endereço encontrado."
      :rows-per-page-options="[10, 20, 50]"
    >
      <!-- Header custom com tri-state + multiordem -->
      <template #header="h">
        <q-tr :props="h">
          <q-th
            v-for="col in h.cols"
            :key="col.name"
            :props="h"
            :class="['header-th', isSortable(col) && 'cursor-pointer']"
            :aria-sort="
              isSortable(col)
                ? dirOf(col.name) === 'asc'
                  ? 'ascending'
                  : dirOf(col.name) === 'desc'
                    ? 'descending'
                    : 'none'
                : 'none'
            "
            @click.stop="isSortable(col) && handleCycleSort(col.name)"
          >
            <div
              class="header-cell"
              :style="{
                justifyContent:
                  col.align === 'right'
                    ? 'flex-end'
                    : col.align === 'center'
                      ? 'center'
                      : 'flex-start',
              }"
            >
              <span class="header-label">{{ col.label }}</span>

              <!-- Ícone de ordenação ou placeholder -->
              <q-icon
                v-if="isSortable(col) && dirOf(col.name) === 'asc'"
                name="arrow_upward"
                size="16px"
                class="sort-icon"
              />
              <q-icon
                v-else-if="isSortable(col) && dirOf(col.name) === 'desc'"
                name="arrow_downward"
                size="16px"
                class="sort-icon"
              />
              <span v-else class="sort-placeholder"></span>
            </div>
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
  import { useTriStateMultiSort } from 'src/composables/useTriStateMultiSort';

  const tableRef = ref();
  const enderecos = ref<any[]>([]);
  const filtrosVisiveis = ref(false);
  const loading = ref(false);
  const router = useRouter();

  const pagination = ref({ page: 1, rowsPerPage: 10, rowsNumber: 0 });

  // Colunas: Nome do Cliente NÃO ordenável
  const columns = [
    {
      name: 'userFullName',
      align: 'left' as const,
      label: 'Nome do Cliente',
      field: 'userFullName',
      sortable: false,
    },
    {
      name: 'streetName',
      align: 'left' as const,
      label: 'Nome da Rua',
      field: 'streetName',
      sortable: true,
    },
    {
      name: 'streetNumber',
      align: 'left' as const,
      label: 'Número',
      field: 'streetNumber',
      sortable: true,
    },
    { name: 'cep', align: 'left' as const, label: 'CEP', field: 'cep', sortable: true },
    {
      name: 'district',
      align: 'left' as const,
      label: 'Bairro',
      field: 'district',
      sortable: true,
    },
    { name: 'city', align: 'left' as const, label: 'Cidade', field: 'city', sortable: true },
    { name: 'state', align: 'left' as const, label: 'Estado', field: 'state', sortable: true },
    { name: 'country', align: 'left' as const, label: 'País', field: 'country', sortable: true },
  ];

  // Helper
  const isSortable = (col: any) => col.sortable !== false;

  // Ordenação
  const {
    dirOf,
    cycleSort,
    buildSortQuery,
    init: initSort,
    sortState,
  } = useTriStateMultiSort({
    storageKey: 'enderecos:list:sort',
    persist: true,
  });

  function handleCycleSort(field: string) {
    cycleSort(field);
    requestServer();
  }

  // Filtra qualquer sort de coluna não ordenável (persistida indevidamente)
  function purgeNonSortable() {
    sortState.value = sortState.value.filter((s) =>
      isSortable(columns.find((c) => c.name === s.field))
    );
  }

  // Fetch principal
  const fetchApi = async (props?: { pagination?: any }) => {
    loading.value = true;
    try {
      const pag = props?.pagination ?? pagination.value;
      const pageParam = pag.page ?? 1;
      const sizeParam = pag.rowsPerPage ?? 10;

      const url =
        `/clientes-api/v1/customers-management/addresses/search` +
        `?page=${pageParam}&size=${sizeParam}` +
        `${buildSortQuery()}`;

      const resp = await apiClientes.get(url, { headers: { 'Content-Type': 'application/json' } });
      const pageData = resp.data;

      enderecos.value = pageData.content ?? [];
      pagination.value = { ...pag, rowsNumber: pageData.totalElements ?? 0 };
    } catch (err: any) {
      if (err.response?.status === 403) router.push('/login');
      else console.error('Erro ao buscar endereços:', err);
    } finally {
      loading.value = false;
    }
  };

  // Requisição
  function requestServer() {
    if (tableRef.value?.requestServerInteraction) tableRef.value.requestServerInteraction();
    else fetchApi({ pagination: pagination.value });
  }

  onMounted(() => {
    initSort();
    purgeNonSortable();
    requestServer();
  });
</script>

<style scoped>
  .header-th {
    vertical-align: middle;
  }

  .header-cell {
    display: flex;
    align-items: center;
    gap: 4px;
    white-space: nowrap;
    width: 100%;
  }

  .header-label {
    display: inline-block;
    max-width: 100%;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  .sort-icon,
  .sort-placeholder {
    width: 16px;
    height: 16px;
    flex: 0 0 16px;
  }

  .sort-placeholder {
    visibility: hidden;
  }

  /* esconde a seta nativa do Quasar no hover */
  :deep(.q-table thead tr > th .q-table__sort-icon) {
    display: none !important;
  }
</style>
