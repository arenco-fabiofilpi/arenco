<template>
  <div class="q-pa-md">
    <h1 class="q-mt-lg q-mb-md">Preferencias de Contato</h1>
    <h2 class="text-caption text-grey-7">Importante para processo de Esqueci Minha Senha</h2>

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
      :rows="preferencias"
      :columns="columns"
      row-key="id"
      :loading="loading"
      v-model:pagination="pagination"
      @request="fetchApi"
      no-data-label="Nenhuma preferência de contato encontrada."
    />
  </div>
</template>

<script setup lang="ts">
  import { ref, watch } from 'vue';
  import { useRouter } from 'vue-router';
  import { apiClientes } from 'src/boot/axios';

  // Dados da tabela
  const preferencias = ref([]);
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
    {
      name: 'userFullName',
      align: 'left' as const,
      label: 'Nome do Cliente',
      field: 'userFullName',
    },
    {
      name: 'ownerIdErp',
      align: 'left' as const,
      label: 'ID ERP do Cliente',
      field: 'ownerIdErp',
    },
    { name: 'value', align: 'left' as const, label: 'Contato', field: 'value' },
    { name: 'contactType', align: 'left' as const, label: 'Tipo de Contato', field: 'contactType' },
    { name: 'active', align: 'left' as const, label: 'Ativo?', field: 'active' },
  ];

  // Busca na API
  const fetchApi = async (props: any) => {
    loading.value = true;
    try {
      if (!props.pagination) pagination.value.rowsPerPage = 10;
      else pagination.value.rowsPerPage = props.pagination.rowsPerPage;

      if (!props.pagination) pagination.value.page = 1;
      else pagination.value.page = props.pagination.page;

      const response = await apiClientes.get(
        `/clientes-api/v1/customers-management/contact-preferences/search?page=${pagination.value.page}&size=${pagination.value.rowsPerPage}`,
        {
          headers: {
            'Content-Type': 'application/json',
          },
        }
      );

      preferencias.value = response.data.content;
      pagination.value.rowsNumber = response.data.totalElements || 0;
    } catch (error: any) {
      if (error.response?.status === 403) {
        router.push('/login');
      } else {
        console.error('Erro ao buscar informacoes:', error);
      }
    } finally {
      loading.value = false;
    }
  };
  // Atualiza a lista de resultados ao mudar paginação
  watch(pagination, fetchApi, { immediate: true });
</script>

<style scoped>
  .text-h6 {
    font-size: 1.25rem;
    font-weight: bold;
  }
</style>
