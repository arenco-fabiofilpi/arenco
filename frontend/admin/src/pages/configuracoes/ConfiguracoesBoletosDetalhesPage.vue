<template>
  <div class="q-pa-md">
    <div class="row items-center q-mb-md">
      <q-btn flat icon="arrow_back" label="Voltar" @click="goBack" />
      <q-space />
      <q-btn
        color="negative"
        icon="delete"
        label="Remover"
        :loading="removing"
        :disable="removing || loading"
        @click="onRemove"
        class="q-mr-sm"
      />
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
      <div class="text-h5">Detalhes da Configuração de Boleto</div>
      <q-space />
      <q-badge v-if="model?.id" outline color="primary" class="q-ml-sm">
        ID: {{ model.id }}
      </q-badge>
      <q-badge outline color="secondary" class="q-ml-sm" v-if="model?.version !== undefined">
        v{{ model.version }}
      </q-badge>

      <!-- NOVO: datas -->
      <q-badge v-if="model?.dateCreated" outline color="grey-8" class="q-ml-sm">
        Criado: {{ model.dateCreated }}
      </q-badge>
      <q-badge v-if="model?.lastUpdated" outline color="grey-8" class="q-ml-sm">
        Atualizado: {{ model.lastUpdated }}
      </q-badge>
    </div>

    <q-card flat bordered>
      <q-linear-progress v-if="loading" indeterminate />
      <q-card-section v-if="!loading">
        <q-form ref="formRef">
          <div class="row q-col-gutter-md">
            <div class="col-12 col-md-4">
              <q-select
                v-model="model.banco"
                :options="bankOptions"
                label="Banco *"
                emit-value
                map-options
                :rules="[req]"
                dense
                fill-input
              />
            </div>

            <div class="col-12 col-md-4">
              <q-input
                v-model="model.nomeBeneficiario"
                label="Nome do Beneficiário *"
                :rules="[req]"
                dense
              />
            </div>

            <div class="col-12 col-md-4">
              <q-input
                v-model="model.documento"
                label="Documento (CNPJ/CPF) *"
                :rules="[req]"
                dense
              />
            </div>

            <div class="col-12 col-md-3">
              <q-input v-model="model.agencia" label="Agência *" :rules="[req]" dense />
            </div>
            <div class="col-12 col-md-4">
              <q-input
                v-model="model.codigoBeneficiario"
                label="Código Beneficiário *"
                :rules="[req]"
                dense
              />
            </div>
            <div class="col-12 col-md-3">
              <q-input v-model="model.carteira" label="Carteira *" :rules="[req]" dense />
            </div>
            <div class="col-12 col-md-2">
              <q-input v-model="model.digitoCodigoBeneficiario" label="Dígito" dense />
            </div>
            <div class="col-12">
              <q-input v-model="model.instrucoes" label="Instruções (texto no boleto)" dense />
            </div>

            <div class="col-12 col-md-6">
              <q-input v-model="model.localDePagamento" label="Local de Pagamento" dense />
            </div>

            <div class="col-12 col-md-6">
              <q-input v-model="model.especieDocumento" label="Espécie de Documento" dense />
            </div>

            <div class="col-12 col-md-6">
              <q-input v-model="model.logradouro" label="Logradouro" dense />
            </div>
            <div class="col-12 col-md-6">
              <q-input v-model="model.bairro" label="Bairro" dense />
            </div>

            <div class="col-12 col-md-5">
              <q-input v-model="model.cidade" label="Cidade" dense />
            </div>
            <div class="col-12 col-md-2">
              <q-input v-model="model.uf" label="UF" dense mask="AA" fill-mask />
            </div>
            <div class="col-12 col-md-5">
              <q-input v-model="model.cep" label="CEP" dense mask="#####-###" fill-mask />
            </div>
          </div>
        </q-form>
      </q-card-section>
    </q-card>
  </div>
</template>

<script setup lang="ts">
  import { reactive, ref, onMounted } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { apiClientes } from 'src/boot/axios';
  import { useQuasar, Notify } from 'quasar';

  type Model = {
    version?: number;
    id?: string;
    dateCreated?: string | Date | null; // <-- NOVO
    lastUpdated?: string | Date | null; // <-- NOVO
    localDePagamento?: string | null;
    nomeBeneficiario?: string | null;
    instrucoes?: string | null;
    banco?: string | null;
    documento?: string | null;
    agencia?: string | null;
    codigoBeneficiario?: string | null;
    digitoCodigoBeneficiario?: string | null;
    carteira?: string | null;
    logradouro?: string | null;
    bairro?: string | null;
    cep?: string | null;
    cidade?: string | null;
    uf?: string | null;
    especieDocumento?: string | null;
  };

  const route = useRoute();
  const router = useRouter();
  const $q = useQuasar();

  const id = route.query.id as string;
  const loading = ref(false);
  const saving = ref(false);
  const removing = ref(false);
  const formRef = ref();

  const model = reactive<Model>({});

  const bankOptions = [{ label: 'Bradesco', value: 'BRADESCO' }];

  const req = (v: any) => !!v || 'Obrigatório';

  function goBack() {
    router.back();
  }

  async function load() {
    loading.value = true;
    if (!id || typeof id !== 'string') {
      loading.value = false;
      return;
    }

    try {
      const url = `/clientes-api/v1/admin/payment-slip-settings/${id}`;
      const resp = await apiClientes.get(url, { headers: { 'Content-Type': 'application/json' } });
      Object.assign(model, resp.data || {});
    } catch (error: any) {
      if (error.response?.status === 403) {
        router.push('/login');
        return;
      }
      console.error('Erro ao carregar detalhes:', error);
      ($q.notify ?? Notify.create)({ type: 'negative', message: 'Erro ao carregar detalhes.' });
    } finally {
      loading.value = false;
    }
  }

  async function onSave() {
    const valid = await formRef.value?.validate?.();
    if (!valid) return;

    saving.value = true;
    try {
      const url = `/clientes-api/v1/admin/payment-slip-settings/${id}`;
      await apiClientes.put(url, model, { headers: { 'Content-Type': 'application/json' } });
      ($q.notify ?? Notify.create)({
        type: 'positive',
        message: 'Configuração atualizada com sucesso.',
      });
      await load();
    } catch (error: any) {
      console.error('Erro ao salvar:', error);
      const msg = error.response?.data?.message || 'Erro ao salvar configuração.';
      ($q.notify ?? Notify.create)({ type: 'negative', message: msg });
    } finally {
      saving.value = false;
    }
  }

  async function onRemove() {
    if (!id) return;

    $q.dialog({
      title: 'Confirmar',
      message: 'Tem certeza que deseja remover esta configuração?',
      cancel: true,
      persistent: true,
    }).onOk(async () => {
      removing.value = true;
      try {
        const url = `/clientes-api/v1/admin/payment-slip-settings/${id}`;
        await apiClientes.delete(url);
        ($q.notify ?? Notify.create)({
          type: 'positive',
          message: 'Configuração removida com sucesso.',
        });
        router.push('/admin/app/config-boletos');
      } catch (error: any) {
        console.error('Erro ao remover:', error);
        const msg = error.response?.data?.message || 'Erro ao remover configuração.';
        ($q.notify ?? Notify.create)({ type: 'negative', message: msg });
      } finally {
        removing.value = false;
      }
    });
  }

  onMounted(load);
</script>

<style scoped>
  .text-h5 {
    font-weight: 600;
  }
</style>
