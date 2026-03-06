<template>
  <div class="q-pa-md">
    <h1 class="q-mt-lg q-mb-md">Administradores</h1>

    <!-- Tabela -->
    <q-table
      :rows="usuarios"
      :columns="columns"
      row-key="id"
      :loading="loading"
      v-model:pagination="pagination"
      @request="fetchApi"
      no-data-label="Nenhum usuário encontrado."
    >
      <!-- Coluna de Ações -->
      <template v-slot:body-cell-actions="props">
        <q-td :props="props">
          <q-btn flat round dense icon="more_vert">
            <q-menu>
              <q-list style="min-width: 200px">
                <q-item clickable v-close-popup @click="removerUsuario(props, props.row)">
                  <q-item-section avatar>
                    <q-icon name="delete" color="primary" />
                  </q-item-section>
                  <q-item-section> Remover Usuário </q-item-section>
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
  import { ref, watch } from 'vue';
  import { useRouter } from 'vue-router';
  import { apiClientes } from 'src/boot/axios';
  import { useQuasar } from 'quasar';

  const $q = useQuasar();

  const usuarios = ref([]);
  const loading = ref(false);
  const router = useRouter();

  const pagination = ref({
    page: 1,
    rowsPerPage: 10,
    rowsNumber: 0,
  });

  // Configuração das colunas
  const columns = [
    { name: 'nome', align: 'left' as const, label: 'Nome', field: 'name' },
    { name: 'username', align: 'left' as const, label: 'Username', field: 'username' },
    { name: 'loginMethod', align: 'left' as const, label: 'Método de Login', field: 'loginMethod' },
    { name: 'enabled', align: 'left' as const, label: 'Habilitado', field: 'enabled' },
    { name: 'actions', align: 'center' as const, label: 'Ações', field: 'actions' },
  ];

  const fetchApi = async (props: any) => {
    loading.value = true;
    try {
      if (!props.pagination) pagination.value.rowsPerPage = 10;
      else pagination.value.rowsPerPage = props.pagination.rowsPerPage;

      if (!props.pagination) pagination.value.page = 1;
      else pagination.value.page = props.pagination.page;

      const response = await apiClientes.get(
        `/clientes-api/v1/admin-management/users?page=${pagination.value.page}&size=${pagination.value.rowsPerPage}`,
        {
          headers: {
            'Content-Type': 'application/json',
          },
        }
      );

      usuarios.value = response.data.content;
      pagination.value.rowsNumber = response.data.totalElements || 0;
    } catch (error: any) {
      if (error.response?.status === 403) {
        router.push('/login');
      } else {
        console.error('Erro ao buscar administradores:', error);
      }
    } finally {
      loading.value = false;
    }
  };

  watch(pagination, fetchApi, { immediate: true });

  const removerUsuario = async (props: any, usuario: any) => {
    try {
      const response = await apiClientes.delete(
        `/clientes-api/v1/admin-management/users/` + usuario.id,
        {}
      );
      if (response.status == 202) {
        $q.notify({
          type: 'positive',
          message: `Usuário ${usuario.username} removido com sucesso.`,
        });
      }
    } catch (e) {
      console.error(e);
      $q.notify({ type: 'negative', message: 'Erro ao remover usuário.' });
    } finally {
      fetchApi(props);
    }
  };
</script>
