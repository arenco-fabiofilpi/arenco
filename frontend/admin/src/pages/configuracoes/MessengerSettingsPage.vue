<template>
  <div class="q-pa-lg column q-gutter-md">
    <div class="row items-center q-mb-md">
      <q-btn flat icon="arrow_back" label="Voltar" @click="goBack" />
      <q-space />
      <q-btn :loading="loading" color="primary" icon="refresh" flat round @click="loadSettings" />
      <q-btn
        color="primary"
        icon="save"
        label="Salvar alterações"
        :loading="saving"
        :disable="saving || loading"
        @click="onSave"
      />
    </div>

    <div class="row items-center q-mb-sm">
      <div class="text-h5">Configurações de Disparo de Cobranças</div>
      <q-space />
      <q-badge v-if="settings?.id" outline color="primary" class="q-ml-sm">
        ID: {{ settings.id }}
      </q-badge>
      <q-badge outline color="secondary" class="q-ml-sm" v-if="settings?.version !== undefined"
        >v{{ settings.version }}</q-badge
      >
    </div>

    <q-card bordered flat>
      <q-linear-progress v-if="loading" indeterminate />
      <q-card-section v-if="settings">
        <div class="row q-col-gutter-md">
          <div class="col-12 col-sm-4">
            <q-select
              v-model="form.state"
              :options="stateOptions"
              label="Estado"
              emit-value
              map-options
              dense
            />
          </div>
          <div class="col-12 col-sm-4">
            <q-select
              v-model="form.strategy"
              :options="strategyOptions"
              label="Estratégia de envio"
              emit-value
              map-options
              dense
            />
          </div>
          <div class="col-12 col-sm-4">
            <q-select
              v-model="form.targetUsers"
              :options="targetUsersOptions"
              label="Usuários Alvos"
              emit-value
              map-options
              dense
            />
          </div>
        </div>
      </q-card-section>
    </q-card>
    <q-table
      :rows="betaUsers"
      :columns="columns"
      row-key="id"
      :loading="loading"
      v-model:pagination="pagination"
      @request="fetchBetaUsers"
      no-data-label="Nenhum Usuário Beta encontrado."
    >
      <!-- Coluna de Ações -->
      <template v-slot:body-cell-actions="props">
        <q-td :props="props">
          <q-btn flat round dense icon="more_vert">
            <q-menu>
              <q-list style="min-width: 200px">
                <q-item clickable v-close-popup @click="removerBetaUser(props, props.row)">
                  <q-item-section avatar>
                    <q-icon name="remove" color="primary" />
                  </q-item-section>
                  <q-item-section> Remover da lista de Beta Users </q-item-section>
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
  import { ref, reactive, onMounted, watch } from 'vue';
  import { useRouter } from 'vue-router';
  import { useQuasar } from 'quasar';
  import { apiClientes } from 'src/boot/axios';

  interface SettingsDTO {
    id: string;
    version: number;
    state: 'ENABLED' | 'DISABLED';
    strategy: 'EMAIL_ONLY';
    targetUsers: 'NO_USERS' | 'BETA_USERS' | 'ALL_USERS';
  }

  const router = useRouter();
  const $q = useQuasar();
  const settings = ref<SettingsDTO | null>(null);
  const form = reactive<Partial<SettingsDTO>>({});
  const original = ref<Partial<SettingsDTO>>({});
  const loading = ref(false);
  const saving = ref(false);
  const betaUsers = ref([]);

  const pagination = ref({
    page: 1,
    rowsPerPage: 10,
    rowsNumber: 0,
  });

  const stateOptions = [
    { label: 'Habilitado', value: 'ENABLED' },
    { label: 'Desabilitado', value: 'DISABLED' },
  ];
  const strategyOptions = [{ label: 'Somente E-mail', value: 'EMAIL_ONLY' }];
  const targetUsersOptions = [
    { label: 'Nenhum Usuário', value: 'NO_USERS' },
    { label: 'Somente Beta Users', value: 'BETA_USERS' },
    { label: 'Todos os Usuários', value: 'ALL_USERS' },
  ];
  const columns = [
    { name: 'username', align: 'left' as const, label: 'Username', field: 'username' },
    { name: 'name', align: 'left' as const, label: 'Nome', field: 'name' },
    { name: 'idErp', align: 'left' as const, label: 'ID ERP', field: 'idErp' },
    { name: 'actions', align: 'center' as const, label: 'Ações', field: 'actions' },
  ];

  onMounted(loadSettings);

  async function loadSettings() {
    loading.value = true;
    try {
      const { data } = await apiClientes.get<SettingsDTO>('/clientes-api/v1/settings');
      settings.value = data;
      Object.assign(form, data);
      original.value = { ...data };
    } catch {
      $q.notify({ type: 'negative', message: 'Falha ao carregar configurações' });
    } finally {
      loading.value = false;
    }
  }

  function diffPatchBody() {
    const body: Partial<SettingsDTO> = {};
    if (form.state !== original.value.state) body.state = form.state as SettingsDTO['state'];
    if (form.strategy !== original.value.strategy)
      body.strategy = form.strategy as SettingsDTO['strategy'];
    if (form.targetUsers !== original.value.targetUsers)
      body.targetUsers = form.targetUsers as SettingsDTO['targetUsers'];
    return body;
  }

  async function onSave() {
    const body = diffPatchBody();
    if (Object.keys(body).length === 0) {
      $q.notify({ type: 'info', message: 'Nenhuma alteração para salvar.' });
      return;
    }
    saving.value = true;
    try {
      await apiClientes.patch('/clientes-api/v1/settings', body);
      $q.notify({ type: 'positive', message: 'Configurações salvas.' });
      await loadSettings();
    } catch {
      $q.notify({ type: 'negative', message: 'Erro ao salvar configurações.' });
    } finally {
      saving.value = false;
    }
  }
  // Busca na API
  const fetchBetaUsers = async (props: any) => {
    loading.value = true;
    try {
      if (!props.pagination) pagination.value.rowsPerPage = 10;
      else pagination.value.rowsPerPage = props.pagination.rowsPerPage;

      if (!props.pagination) pagination.value.page = 1;
      else pagination.value.page = props.pagination.page;

      const response = await apiClientes.get(
        `/clientes-api/v1/settings/beta-users?page=${pagination.value.page}&size=${pagination.value.rowsPerPage}`,
        {
          headers: {
            'Content-Type': 'application/json',
          },
        }
      );

      betaUsers.value = response.data.content;
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

  const removerBetaUser = async (props: any, cliente: any) => {
    try {
      await apiClientes.delete(
        `/clientes-api/v1/settings/beta-users?userModelId=` + cliente.id,
        {}
      );
      $q.notify({
        type: 'positive',
        message: `Cliente ${cliente.name} removido da lista de Beta Users.`,
      });
    } catch (e) {
      $q.notify({ type: 'negative', message: 'Erro ao remover cliente da lista de Beta Users.' });
    } finally {
      fetchBetaUsers(props);
    }
  };

  // Atualiza a lista ao mudar paginação
  watch(pagination, fetchBetaUsers, { immediate: true });

  function goBack() {
    router.back();
  }
</script>
