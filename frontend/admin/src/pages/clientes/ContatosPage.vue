<template>
  <div class="q-pa-md">
    <h1 class="q-mt-lg q-mb-md">Contatos</h1>

    <!-- Filtros -->
    <q-btn
      color="primary"
      icon="filter_list"
      label="Filtros"
      class="q-mb-md"
      @click="filtrosVisiveis = !filtrosVisiveis"
    />

    <!-- Tabela -->
    <q-table
      ref="tableRef"
      :rows="contatos"
      :columns="columns"
      row-key="id"
      :loading="loading"
      v-model:pagination="pagination"
      @request="fetchApi"
      no-data-label="Nenhum contato encontrado."
      :rows-per-page-options="[10, 20, 50]"
    >
      <!-- Header custom: tri-state + multiordem -->
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

              <!-- Ícone ou placeholder -->
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
  const contatos = ref<any[]>([]);
  const filtrosVisiveis = ref(false);
  const loading = ref(false);
  const router = useRouter();

  const pagination = ref({ page: 1, rowsPerPage: 10, rowsNumber: 0 });

  // Colunas — "Nome do Cliente" não ordenável
  const columns = [
    {
      name: 'userFullName',
      align: 'left' as const,
      label: 'Nome do Cliente',
      field: 'userFullName',
      sortable: false,
    },
    {
      name: 'ownerIdErp',
      align: 'left' as const,
      label: 'ID ERP do Cliente',
      field: 'ownerIdErp',
      sortable: false,
    },
    { name: 'value', align: 'left' as const, label: 'Contato', field: 'value', sortable: true },
    {
      name: 'contactType',
      align: 'left' as const,
      label: 'Tipo de Contato',
      field: 'contactType',
      sortable: true,
    },
    { name: 'active', align: 'left' as const, label: 'Ativo?', field: 'active', sortable: true },
  ];

  // Helper para colunas ordenáveis
  const isSortable = (col: any) => col.sortable !== false;

  // Hook de ordenação
  const {
    dirOf,
    cycleSort,
    buildSortQuery,
    init: initSort,
    sortState,
  } = useTriStateMultiSort({
    storageKey: 'contatos:list:sort',
    persist: true,
  });

  function handleCycleSort(field: string) {
    cycleSort(field);
    requestServer();
  }

  function purgeNonSortable() {
    sortState.value = sortState.value.filter((s) =>
      isSortable(columns.find((c) => c.name === s.field))
    );
  }

  // Busca dos dados
  const fetchApi = async (props?: { pagination?: any }) => {
    loading.value = true;
    try {
      const pag = props?.pagination ?? pagination.value;
      const pageParam = pag.page ?? 1; // ou pag.page - 1 se backend for 0-based
      const sizeParam = pag.rowsPerPage ?? 10;

      const url =
        `/clientes-api/v1/customers-management/contacts/search` +
        `?page=${pageParam}&size=${sizeParam}` +
        `${buildSortQuery()}`;

      const resp = await apiClientes.get(url, { headers: { 'Content-Type': 'application/json' } });
      const pageData = resp.data;

      contatos.value = pageData.content ?? [];
      pagination.value = { ...pag, rowsNumber: pageData.totalElements ?? 0 };
    } catch (err: any) {
      if (err.response?.status === 403) router.push('/login');
      else console.error('Erro ao buscar contatos:', err);
    } finally {
      loading.value = false;
    }
  };

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

  /* esconde o ícone nativo do Quasar */
  :deep(.q-table thead tr > th .q-table__sort-icon) {
    display: none !important;
  }
</style>
