<template>
  <div class="q-pa-md">
    <div class="row items-center justify-between q-mb-md">
      <h1 class="q-mt-lg q-mb-none">Configurações para Geração de Boletos</h1>
      <q-btn label="Adicionar" color="primary" icon="add" unelevated @click="openCreateDialog" />
    </div>

    <!-- Tabela -->
    <q-table
      :rows="configs"
      :columns="columns"
      row-key="id"
      :loading="loading"
      v-model:pagination="pagination"
      @request="fetchApi"
      no-data-label="Nenhuma Configuração encontrada."
      @row-click="verDetalhes"
    />

    <!-- Dialog de criação/edição -->
    <q-dialog v-model="dialogOpen" persistent>
      <q-card style="min-width: 360px; max-width: 92vw">
        <q-card-section class="row items-center q-gutter-sm">
          <div class="text-h6">Nova Configuração de Boleto</div>
        </q-card-section>

        <q-separator />

        <q-card-section>
          <q-form @submit="onSubmit" @reset="onReset" ref="formRef" class="q-gutter-md">
            <div class="row q-col-gutter-md">
              <div class="col-12 col-md-6">
                <q-select
                  v-model="form.banco"
                  :options="banks"
                  label="Banco *"
                  emit-value
                  map-options
                  option-value="value"
                  option-label="label"
                  :rules="[(v) => !!v || 'Banco é obrigatório']"
                  :error="!!fieldErr.banco"
                  :error-message="fieldErr.banco"
                />
              </div>

              <div class="col-12 col-md-6">
                <q-input
                  v-model="form.nomeBeneficiario"
                  label="Nome do Beneficiário *"
                  :rules="[
                    (v) => !!v || 'Nome do beneficiário é obrigatório',
                    (v) => !v || v.length <= 120 || 'Nome do beneficiário muito longo (máx 120)',
                  ]"
                  :error="!!fieldErr.nomeBeneficiario"
                  :error-message="fieldErr.nomeBeneficiario"
                />
              </div>

              <div class="col-12 col-md-6">
                <q-input
                  v-model="form.documento"
                  label="Documento (CNPJ) *"
                  inputmode="numeric"
                  hint="Informe o CNPJ (validação feita no servidor)"
                  :rules="[(v) => !!v || 'Documento (CNPJ) é obrigatório']"
                  :error="!!fieldErr.documento"
                  :error-message="fieldErr.documento"
                />
              </div>

              <div class="col-6 col-md-3">
                <q-input
                  v-model="form.agencia"
                  label="Agência *"
                  inputmode="numeric"
                  :rules="[
                    (v) => !!v || 'Agência é obrigatória',
                    (v) =>
                      /^\d{1,10}$/.test(v || '') || 'Agência deve conter apenas dígitos (1 a 10)',
                  ]"
                  :error="!!fieldErr.agencia"
                  :error-message="fieldErr.agencia"
                />
              </div>

              <div class="col-6 col-md-3">
                <q-input
                  v-model="form.carteira"
                  label="Carteira *"
                  inputmode="numeric"
                  :rules="[
                    (v) => !!v || 'Carteira é obrigatória',
                    (v) => /^\d{1,3}$/.test(v || '') || 'Carteira deve conter 1 a 3 dígitos',
                  ]"
                  :error="!!fieldErr.carteira"
                  :error-message="fieldErr.carteira"
                />
              </div>

              <div class="col-6 col-md-4">
                <q-input
                  v-model="form.codigoBeneficiario"
                  label="Código do Beneficiário *"
                  inputmode="numeric"
                  :rules="[
                    (v) => !!v || 'Código do beneficiário é obrigatório',
                    (v) =>
                      /^\d{1,20}$/.test(v || '') ||
                      'Código do beneficiário deve conter apenas dígitos (1 a 20)',
                  ]"
                  :error="!!fieldErr.codigoBeneficiario"
                  :error-message="fieldErr.codigoBeneficiario"
                />
              </div>

              <div class="col-6 col-md-2">
                <q-input
                  v-model="form.digitoCodigoBeneficiario"
                  label="Dígito *"
                  maxlength="1"
                  inputmode="numeric"
                  :rules="[
                    (v) => !!v || 'Dígito do beneficiário é obrigatório',
                    (v) => /^\d$/.test(v || '') || 'Dígito do beneficiário deve ter 1 dígito',
                  ]"
                  :error="!!fieldErr.digitoCodigoBeneficiario"
                  :error-message="fieldErr.digitoCodigoBeneficiario"
                />
              </div>

              <div class="col-12 col-md-6">
                <q-input
                  v-model="form.localDePagamento"
                  label="Local de Pagamento"
                  :rules="[
                    (v) => !v || v.length <= 255 || 'Local de pagamento muito longo (máx 255)',
                  ]"
                  :error="!!fieldErr.localDePagamento"
                  :error-message="fieldErr.localDePagamento"
                />
              </div>

              <div class="col-12 col-md-6">
                <q-input
                  v-model="form.instrucoes"
                  label="Instruções (impresso no boleto)"
                  type="textarea"
                  autogrow
                  stack-label
                  input-style="padding-top: 26px;"
                  :rules="[(v) => !v || v.length <= 1000 || 'Instruções muito longas (máx 1000)']"
                  :error="!!fieldErr.instrucoes"
                  :error-message="fieldErr.instrucoes"
                />
              </div>

              <div class="col-6 col-md-3">
                <q-input
                  v-model="form.logradouro"
                  label="Logradouro *"
                  :rules="[
                    (v) => !!v || 'Logradouro é obrigatório',
                    (v) => !v || v.length <= 120 || 'Logradouro muito longo (máx 120)',
                  ]"
                  :error="!!fieldErr.logradouro"
                  :error-message="fieldErr.logradouro"
                />
              </div>

              <div class="col-6 col-md-3">
                <q-input
                  v-model="form.bairro"
                  label="Bairro *"
                  :rules="[
                    (v) => !!v || 'Bairro é obrigatório',
                    (v) => !v || v.length <= 120 || 'Bairro muito longo (máx 120)',
                  ]"
                  :error="!!fieldErr.bairro"
                  :error-message="fieldErr.bairro"
                />
              </div>

              <div class="col-6 col-md-3">
                <q-input
                  v-model="form.cep"
                  label="CEP *"
                  inputmode="numeric"
                  hint="8 dígitos (99999999) ou 99999-999"
                  :rules="[
                    (v) => !!v || 'CEP é obrigatório',
                    (v) =>
                      /^\d{8}$|^\d{5}-\d{3}$/.test(v || '') ||
                      'CEP deve ser 8 dígitos ou no formato NNNNN-NNN',
                  ]"
                  :error="!!fieldErr.cep"
                  :error-message="fieldErr.cep"
                />
              </div>

              <div class="col-6 col-md-6">
                <q-input
                  v-model="form.cidade"
                  label="Cidade *"
                  :rules="[
                    (v) => !!v || 'Cidade é obrigatória',
                    (v) => !v || v.length <= 120 || 'Cidade muito longa (máx 120)',
                  ]"
                  :error="!!fieldErr.cidade"
                  :error-message="fieldErr.cidade"
                />
              </div>

              <div class="col-6 col-md-2">
                <q-input
                  v-model="form.uf"
                  label="UF *"
                  maxlength="2"
                  :rules="[(v) => !!v || 'UF é obrigatória', (v) => isValidUf(v) || 'UF inválida']"
                  :error="!!fieldErr.uf"
                  :error-message="fieldErr.uf"
                  @update:model-value="
                    (v) =>
                      (form.uf = String(v || '')
                        .toUpperCase()
                        .slice(0, 2))
                  "
                />
              </div>

              <div class="col-12 col-md-4">
                <q-input
                  v-model="form.especieDocumento"
                  label="Espécie de Documento *"
                  :rules="[
                    (v) => !!v || 'Espécie do documento é obrigatória',
                    (v) => !v || v.length <= 30 || 'Espécie de documento muito longa (máx 30)',
                  ]"
                  :error="!!fieldErr.especieDocumento"
                  :error-message="fieldErr.especieDocumento"
                />
              </div>
            </div>

            <div class="row justify-end q-gutter-sm q-mt-md">
              <q-btn flat label="Cancelar" color="primary" @click="closeDialog" :disable="saving" />
              <q-btn type="reset" flat label="Limpar" color="warning" :disable="saving" />
              <q-btn type="submit" label="Salvar" color="primary" :loading="saving" />
            </div>
          </q-form>
        </q-card-section>
      </q-card>
    </q-dialog>
  </div>
</template>

<script setup lang="ts">
  import { ref, reactive, watch, nextTick } from 'vue';
  import { useRouter } from 'vue-router';
  import { useQuasar } from 'quasar';
  import { apiClientes } from 'src/boot/axios';

  // Dados da tabela
  const configs = ref<any[]>([]);
  const loading = ref(false);
  const router = useRouter();
  const $q = useQuasar();

  const pagination = ref({ page: 1, rowsPerPage: 10, rowsNumber: 0 });

  const columns = [
    { name: 'banco', align: 'left' as const, label: 'Banco', field: 'banco' },
    {
      name: 'nomeBeneficiario',
      align: 'left' as const,
      label: 'Beneficiário',
      field: 'nomeBeneficiario',
    },
    { name: 'documento', align: 'left' as const, label: 'Documento', field: 'documento' },
  ];

  // -------- Formulário (criação) --------
  const dialogOpen = ref(false);
  const saving = ref(false);
  const formRef = ref();

  const emptyForm = () => ({
    banco: null as string | null,
    nomeBeneficiario: '',
    documento: '',
    agencia: '',
    codigoBeneficiario: '',
    digitoCodigoBeneficiario: '',
    carteira: '',
    localDePagamento: '',
    instrucoes: '',
    logradouro: '',
    bairro: '',
    cep: '',
    cidade: '',
    uf: '',
    especieDocumento: '',
  });

  const form = ref(emptyForm());

  // Erros vindos do servidor (HTTP 400) — mapeados campo-a-campo
  const fieldErr = reactive<Record<string, string>>({});

  // UFs válidas (mesmo set do backend)
  const UFS = new Set([
    'AC',
    'AL',
    'AP',
    'AM',
    'BA',
    'CE',
    'DF',
    'ES',
    'GO',
    'MA',
    'MT',
    'MS',
    'MG',
    'PA',
    'PB',
    'PR',
    'PE',
    'PI',
    'RJ',
    'RN',
    'RS',
    'RO',
    'RR',
    'SC',
    'SP',
    'SE',
    'TO',
  ]);

  // Sugestão de opções de bancos (troque para seu enum real)
  const banks = [{ label: 'Bradesco', value: 'BRADESCO' }];

  function openCreateDialog() {
    form.value = emptyForm();
    clearFieldErrors();
    dialogOpen.value = true;
    nextTick(() => formRef.value?.resetValidation?.());
  }

  function closeDialog() {
    dialogOpen.value = false;
  }

  function onReset() {
    form.value = emptyForm();
    clearFieldErrors();
    formRef.value?.resetValidation?.();
  }

  function clearFieldErrors() {
    Object.keys(fieldErr).forEach((k) => delete fieldErr[k]);
  }

  async function onSubmit() {
    const valid = await formRef.value?.validate?.();
    if (!valid) return;

    clearFieldErrors();
    saving.value = true;
    try {
      const url = '/clientes-api/v1/admin/payment-slip-settings';
      await apiClientes.post(url, form.value, { headers: { 'Content-Type': 'application/json' } });

      $q.notify({ type: 'positive', message: 'Configuração salva com sucesso!' });
      closeDialog();
      await fetchApi({ pagination: pagination.value });
    } catch (error: any) {
      if (error.response?.status === 403) {
        router.push('/login');
        return;
      }
      if (error.response?.status === 400 && error.response?.data?.errors) {
        // Mapeia o payload do backend: { errors: { campo: [mensagens...] } }
        const errors = error.response.data.errors || {};
        Object.keys(errors).forEach((field) => {
          const msgs = errors[field];
          fieldErr[field] = Array.isArray(msgs) ? msgs[0] || 'Campo inválido' : String(msgs);
        });
        $q.notify({
          type: 'negative',
          message: error.response.data.message || 'Erro de validação',
        });
        // Opcional: rolar até o primeiro erro
        const firstField = Object.keys(fieldErr)[0];
        if (firstField) {
          // No Quasar não há scroll-to-field nativo; você pode focar com refs individuais se quiser
        }
      } else if (error.response?.status === 409) {
        console.error('Conflito de configuração:', error);
        $q.notify({ type: 'negative', message: error.response?.data?.message });
      } else {
        console.error('Erro ao salvar configuração:', error);
        $q.notify({ type: 'negative', message: 'Não foi possível salvar. Tente novamente.' });
      }
    } finally {
      saving.value = false;
    }
  }

  // -------- Listagem (tabela) --------
  const fetchApi = async (props: any) => {
    loading.value = true;
    try {
      if (!props.pagination) pagination.value.rowsPerPage = 10;
      else pagination.value.rowsPerPage = props.pagination.rowsPerPage;

      if (!props.pagination) pagination.value.page = 1;
      else pagination.value.page = props.pagination.page;

      const response = await apiClientes.get(
        `/clientes-api/v1/admin/payment-slip-settings?page=${pagination.value.page}&size=${pagination.value.rowsPerPage}`,
        { headers: { 'Content-Type': 'application/json' } }
      );

      configs.value = response.data.content;
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

  function verDetalhes(evt: Event, row: any) {
    router.push({ path: 'config-boletos/detalhes', query: { id: row.id } });
  }

  // Helpers de validação (espelham o backend)
  function isValidUf(v?: string) {
    if (!v) return false;
    const uf = v.toUpperCase();
    return UFS.has(uf);
  }

  // Atualiza a lista ao mudar paginação
  watch(pagination, fetchApi, { immediate: true });
</script>

<style scoped>
  .text-h6 {
    font-size: 1.25rem;
    font-weight: bold;
  }
</style>
