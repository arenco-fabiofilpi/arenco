<template>
  <div class="q-pa-md">
    <h1 class="q-mt-lg q-mb-md">Sessões Ativas</h1>

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
      :rows="sessoes"
      :columns="columns"
      row-key="id"
      :loading="loading"
      v-model:pagination="pagination"
      @request="fetchSessions"
      no-data-label="Nenhuma Sessão encontrada."
      :rows-per-page-options="[10, 20, 50]"
    >
      <!-- Header custom: tri-state + multiordem (Ações não ordena) -->
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

      <!-- Coluna de Ações -->
      <template #body-cell-actions="props">
        <q-td :props="props">
          <q-btn flat round dense icon="more_vert">
            <q-menu>
              <q-list style="min-width: 200px">
                <q-item clickable v-close-popup @click="removerSessao(props, props.row)">
                  <q-item-section avatar><q-icon name="delete" color="primary" /></q-item-section>
                  <q-item-section>Remover Sessão</q-item-section>
                </q-item>
              </q-list>
            </q-menu>
          </q-btn>
        </q-td>
      </template>
    </q-table>
  </div>
</template>

<script setup lang="ts">
  import { ref, onMounted } from 'vue';
  import { useRouter } from 'vue-router';
  import { apiClientes } from 'src/boot/axios';
  import { useQuasar } from 'quasar';
  import { useTriStateMultiSort } from 'src/composables/useTriStateMultiSort';

  const $q = useQuasar();

  // Dados da tabela
  const tableRef = ref();
  const sessoes = ref<any[]>([]);
  const filtrosVisiveis = ref(false);
  const loading = ref(false);
  const router = useRouter();

  const pagination = ref({ page: 1, rowsPerPage: 10, rowsNumber: 0 });

  // Colunas (Ações não ordenável)
  const columns = [
    {
      name: 'username',
      align: 'left' as const,
      label: 'Username',
      field: 'username',
      sortable: true,
    },
    { name: 'ip', align: 'left' as const, label: 'IP', field: 'ip', sortable: true },
    {
      name: 'userAgent',
      align: 'left' as const,
      label: 'User Agent',
      field: 'userAgent',
      sortable: true,
    },
    {
      name: 'createdAt',
      align: 'left' as const,
      label: 'Data de Criação',
      field: 'createdAt',
      sortable: true,
    },
    {
      name: 'expiresAt',
      align: 'left' as const,
      label: 'Data de expiração',
      field: 'expiresAt',
      sortable: true,
    },
    {
      name: 'actions',
      align: 'center' as const,
      label: 'Ações',
      field: 'actions',
      sortable: false,
    },
  ];

  // helper de ordenáveis
  const isSortable = (col: any) => col?.sortable !== false;

  /** -------- Ordenação (useTriStateMultiSort) -------- */
  const {
    dirOf,
    cycleSort,
    buildSortQuery,
    init: initSort,
    sortState,
  } = useTriStateMultiSort({
    storageKey: 'active-sessions:list:sort',
    persist: true,
  });

  function handleCycleSort(field: string) {
    cycleSort(field);
    requestServer();
  }

  // limpa ordenações persistidas de colunas não-ordenáveis
  function purgeNonSortable() {
    sortState.value = sortState.value.filter((s) =>
      isSortable(columns.find((c) => c.name === s.field))
    );
  }

  // Busca na API
  const fetchSessions = async (props?: { pagination?: any }) => {
    loading.value = true;
    try {
      const p = props?.pagination ?? pagination.value;
      pagination.value.page = p?.page ?? pagination.value.page ?? 1;
      pagination.value.rowsPerPage = p?.rowsPerPage ?? pagination.value.rowsPerPage ?? 10;

      const url =
        `/clientes-api/v1/auth-manager/active-sessions` +
        `?page=${pagination.value.page}&size=${pagination.value.rowsPerPage}` +
        `${buildSortQuery()}`;

      const response = await apiClientes.get(url, {
        headers: { 'Content-Type': 'application/json' },
      });

      sessoes.value = response.data.content ?? [];
      pagination.value.rowsNumber = response.data.totalElements || 0;
    } catch (error: any) {
      if (error.response?.status === 403) router.push('/login');
      else console.error('Erro ao buscar sessões:', error);
    } finally {
      loading.value = false;
    }
  };

  // disparador padrão
  function requestServer() {
    if (tableRef.value?.requestServerInteraction) tableRef.value.requestServerInteraction();
    else fetchSessions({ pagination: pagination.value });
  }

  onMounted(() => {
    initSort();
    purgeNonSortable();
    requestServer();
  });

  const removerSessao = async (props: any, sessao: any) => {
    try {
      const response = await apiClientes.delete(
        `/clientes-api/v1/auth-manager/active-sessions/${sessao.id}`,
        {}
      );
      if (response.status == 201) {
        $q.notify({ type: 'positive', message: `Sessão ${sessao.id} removida com sucesso.` });
      }
    } catch (e) {
      console.error(e);
      $q.notify({ type: 'negative', message: 'Erro ao remover sessão.' });
    } finally {
      fetchSessions(props);
    }
  };
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

  /* esconde a seta nativa do Quasar */
  :deep(.q-table thead tr > th .q-table__sort-icon) {
    display: none !important;
  }
</style>
