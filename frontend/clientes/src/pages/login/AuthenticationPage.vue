<template>
  <div class="page-login q-pa-md flex flex-center">
    <q-card class="q-pa-md" style="max-width: 480px; width: 100%">
      <q-card-section class="text-center">
        <div class="text-h6">Verificação em duas etapas</div>
        <div class="text-subtitle2 q-mt-xs">
          Se houver um usuário correspondente, enviaremos uma mensagem para o canal de contato
          previamente cadastrado em nosso sistema (como e-mail ou SMS).
        </div>
      </q-card-section>

      <!-- Avisos -->
      <q-card-section v-if="alert.message">
        <q-banner
          :class="
            alert.type === 'error'
              ? 'bg-negative text-white'
              : alert.type === 'success'
                ? 'bg-positive text-white'
                : 'bg-grey-3'
          "
          rounded
          dense
        >
          <div class="row items-center no-wrap">
            <q-icon :name="alertIcon" class="q-mr-sm" />
            <div class="col">{{ alert.message }}</div>
          </div>
        </q-banner>
      </q-card-section>

      <q-separator spaced />

      <!-- Input do OTP -->
      <q-card-section>
        <q-input
          v-model="otp"
          label="Código de verificação (OTP)"
          outlined
          dense
          maxlength="10"
          inputmode="numeric"
          :disable="loading.validate"
          :rules="[validaOtp]"
          @keyup.enter="validateOtp"
        >
          <template #prepend>
            <q-icon name="vpn_key" />
          </template>
        </q-input>

        <q-btn
          label="Validar código"
          color="secondary"
          class="full-width q-mt-md"
          :loading="loading.validate"
          :disable="!isOtpReady || loading.validate"
          @click="validateOtp"
        />
      </q-card-section>

      <!-- Rodapé -->
      <q-card-actions align="between">
        <q-btn flat icon="arrow_back" label="Voltar ao login" @click="logout" />
        <q-separator spaced />
        <q-btn flat icon="help" label="Precisa de ajuda?" @click="showHelp = !showHelp" />
      </q-card-actions>

      <q-slide-transition>
        <div v-show="showHelp" class="q-pa-md text-caption text-grey-7">
          • Verifique caixa de entrada/SMS.<br />
          • Se o token expirar, tente novamente.
        </div>
      </q-slide-transition>
    </q-card>
  </div>
</template>

<script setup lang="ts">
  import { ref, computed, onMounted } from 'vue';
  import { useRouter } from 'vue-router';
  import { apiClientes } from 'src/boot/axios';
  import type { AxiosError } from 'axios';
  import { useAuthStore } from 'src/stores/auth';

  interface ErrorPayload {
    code?: string; // "429 TOO_MANY_REQUESTS" | "401 UNAUTHORIZED" | "400 BAD_REQUEST" | ...
    message?: string; // mensagem amigável do back
    otpTokenValid?: boolean; // presente em 400 BAD_REQUEST
  }

  const router = useRouter();
  const auth = useAuthStore();

  const loading = ref({
    validate: false,
  });

  const otp = ref('');

  const showHelp = ref(false);

  const alert = ref<
    { type: 'info' | 'success' | 'error'; message: string } | { type: null; message: '' }
  >({
    type: null,
    message: '',
  });

  const alertIcon = computed(() => {
    if (alert.value.type === 'error') return 'error';
    if (alert.value.type === 'success') return 'check_circle';
    return 'info';
  });

  const isOtpReady = computed(() => otp.value.trim().length >= 4); // ajuste se seu OTP tiver tamanho fixo

  function setAlert(type: 'info' | 'success' | 'error', message: string) {
    alert.value = { type, message };
  }

  function clearAlert() {
    alert.value = { type: null, message: '' } as never;
  }

  async function logout() {
    try {
      await apiClientes.post('/clientes-api/v1/auth/logout');
    } catch (error) {
      console.error('Erro ao fazer logout:', error);
    } finally {
      auth.clearPendingUsername();
      router.push('/login');
    }
  }

  const validaOtp = (val: string) => !!val?.trim() || 'Informe o código enviado';

  async function validateOtp() {
    if (!isOtpReady.value) return;
    loading.value.validate = true;
    clearAlert();
    try {
      await apiClientes.post('/clientes-api/v1/customer/auth/authentication', {
        username: auth.pendingUsername,
        token: otp.value.trim(),
      });
      setAlert('success', 'Verificação concluída. Redirecionando...');
      // pequeno delay para UX antes do push
      setTimeout(() => router.push('/clientes/app'), 300);
    } catch (err) {
      const e = err as AxiosError<ErrorPayload>;
      const code = e.response?.data?.code || '';
      const msg = e.response?.data?.message || 'Falha na validação do código.';
      if (code.startsWith('401')) {
        // token inexistente/expirado/inválido -> peça novo envio
        setAlert('error', msg + ' Solicite um novo código.');
      } else if (code.startsWith('400')) {
        setAlert('error', msg); // exibe "Token Inválido. Restam 2 tentativas"
      } else {
        setAlert('error', msg);
      }
    } finally {
      loading.value.validate = false;
    }
  }

  onMounted(() => {
    // Se o usuário caiu direto no /login-2fa sem passar pelo login, volte
    if (!auth.pendingUsername) {
      router.replace('/login');
      return;
    }
    // Se quiser, você pode usar auth.pendingUsername aqui (ex.: exibir, logar, passar para chamadas)
  });
</script>

<style scoped>
  .page-login {
    min-height: 80vh;
  }
  .full-width {
    width: 100%;
  }
</style>
