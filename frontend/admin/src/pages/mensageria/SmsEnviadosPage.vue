<template>
  <div class="q-pa-md">
    <h1 class="q-mt-lg q-mb-md">SMS's Enviados</h1>

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
      :rows="lista"
      :columns="columns"
      row-key="id"
      :loading="loading"
      v-model:pagination="pagination"
      @request="fetchApi"
      no-data-label="Nenhum SMS encontrado."
    />
  </div>
</template>

<script setup lang="ts">
  import { ref, watch } from 'vue';
  import { useRouter } from 'vue-router';
  import { apiClientes } from 'src/boot/axios';

  // Dados da tabela
  const lista = ref([]);
  const filtrosVisiveis = ref(false);
  const loading = ref(false);
  // Router para redirecionamento
  const router = useRouter();

  const pagination = ref({
    page: 1,
    rowsPerPage: 10,
    rowsNumber: 0,
  });

  // Configuração das colunas
  const columns = [
    { name: 'deliveredTo', align: 'left' as const, label: 'Destinatário', field: 'deliveredTo' },
    { name: 'message', align: 'left' as const, label: 'Mensagem', field: 'message' },
    { name: 'dateCreated', align: 'left' as const, label: 'Data de Criação', field: 'dateCreated' },
    { name: 'status', align: 'left' as const, label: 'Status', field: 'status' },
  ];

  // Busca clientes na API
  const fetchApi = async (props: any) => {
    loading.value = true;
    try {
      if (!props.pagination) pagination.value.rowsPerPage = 10;
      else pagination.value.rowsPerPage = props.pagination.rowsPerPage;

      if (!props.pagination) pagination.value.page = 1;
      else pagination.value.page = props.pagination.page;

      const response = await apiClientes.get(
        `/clientes-api/v1/sms-sent?page=${pagination.value.page}&size=${pagination.value.rowsPerPage}`,
        {
          headers: {
            'Content-Type': 'application/json',
          },
        }
      );

      lista.value = response.data.content;
      pagination.value.rowsNumber = response.data.totalElements || 0;
    } catch (error: any) {
      if (error.response?.status === 403) {
        router.push('/login');
      } else {
        console.error('Erro ao buscar info:', error);
      }
    } finally {
      loading.value = false;
    }
  };

  // Atualiza a lista ao mudar paginação
  watch(pagination, fetchApi, { immediate: true });
</script>

<style scoped>
  .text-h6 {
    font-size: 1.25rem;
    font-weight: bold;
  }
</style>
