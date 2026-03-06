<template>
  <div class="q-pa-md flex flex-center" style="min-height: 80vh">
    <q-card class="q-pa-md" style="max-width: 520px; width: 100%">
      <q-card-section class="text-center">
        <div class="text-h6">Esqueci minha senha</div>
        <div class="text-subtitle2 text-grey-7">Redefina sua senha em dois passos</div>
      </q-card-section>

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

      <q-stepper v-model="step" animated flat header-nav>
        <!-- PASSO 1: Solicitar código -->
        <q-step name="request" title="Identificar usuário" icon="person" :done="step !== 'request'">
          <q-form @submit.prevent="requestReset()">
            <q-input
              v-model="username"
              label="Usuário"
              outlined
              dense
              :disable="loading.request || step !== 'request'"
              :rules="[validaUsername]"
            >
              <template #prepend><q-icon name="person" /></template>
            </q-input>

            <q-btn
              label="Enviar código"
              color="primary"
              class="full-width q-mt-md"
              type="submit"
              :loading="loading.request"
              :disable="!isUsernameReady || loading.request || cooldownSeconds > 0"
            />

            <div class="text-caption text-grey-7 q-mt-sm" v-if="cooldownSeconds > 0">
              Aguarde {{ cooldownSeconds }}s para reenviar.
            </div>
          </q-form>
        </q-step>

        <!-- PASSO 2: Validar OTP e redefinir senha -->
        <q-step name="reset" title="Validar código e redefinir" icon="vpn_key">
          <div class="text-caption text-grey-7 q-mb-sm">
            Usuário: <b>{{ username }}</b>
            <q-btn flat dense size="sm" class="q-ml-xs" label="trocar" @click="step = 'request'" />
          </div>

          <q-form @submit.prevent="resetPassword">
            <q-input
              v-model="otp"
              label="Código (OTP)"
              outlined
              dense
              inputmode="numeric"
              maxlength="10"
              :disable="loading.reset"
              :rules="[validaOtp]"
            >
              <template #prepend><q-icon name="confirmation_number" /></template>
            </q-input>

            <q-input
              v-model="password"
              :type="showPwd ? 'text' : 'password'"
              label="Nova senha"
              outlined
              dense
              :disable="loading.reset"
              :rules="[validaPassword]"
              class="q-mt-sm"
            >
              <template #prepend><q-icon name="lock" /></template>
              <template #append>
                <q-icon
                  :name="showPwd ? 'visibility_off' : 'visibility'"
                  class="cursor-pointer"
                  @click="showPwd = !showPwd"
                />
              </template>
            </q-input>

            <q-input
              v-model="passwordConfirm"
              :type="showPwdConfirm ? 'text' : 'password'"
              label="Confirmar nova senha"
              outlined
              dense
              :disable="loading.reset"
              :rules="[validaPasswordConfirm]"
              class="q-mt-sm"
            >
              <template #prepend><q-icon name="lock" /></template>
              <template #append>
                <q-icon
                  :name="showPwdConfirm ? 'visibility_off' : 'visibility'"
                  class="cursor-pointer"
                  @click="showPwdConfirm = !showPwdConfirm"
                />
              </template>
            </q-input>

            <div class="row q-col-gutter-sm q-mt-md">
              <div class="col-12 col-sm-6">
                <q-btn
                  label="Validar e redefinir"
                  color="secondary"
                  class="full-width"
                  type="submit"
                  :loading="loading.reset"
                  :disable="!isResetFormReady || loading.reset"
                />
              </div>
              <div class="col-12 col-sm-6">
                <q-btn
                  outline
                  color="primary"
                  class="full-width"
                  :label="
                    cooldownSeconds > 0 ? `Reenviar (${cooldownSeconds}s)` : 'Reenviar código'
                  "
                  :disable="cooldownSeconds > 0 || loading.request"
                  @click="requestReset(true)"
                />
              </div>
            </div>
          </q-form>
        </q-step>
      </q-stepper>

      <q-card-actions align="between" class="q-mt-md">
        <q-btn flat icon="arrow_back" label="Voltar ao login" @click="router.push('/login')" />
        <q-btn flat icon="help" label="Ajuda" @click="showHelp = !showHelp" />
      </q-card-actions>

      <q-slide-transition>
        <div v-show="showHelp" class="q-pa-md text-caption text-grey-7">
          • Se o usuário existir e tiver contato válido, você receberá um OTP por SMS/E-mail.<br />
          • Tente novamente após o cooldown se precisar reenviar.<br />
          • Se o token esgotar as tentativas, solicite um novo envio.
        </div>
      </q-slide-transition>
    </q-card>
  </div>
</template>

<script setup lang="ts">
  import { ref, computed, onBeforeUnmount } from 'vue';
  import { useRouter } from 'vue-router';
  import type { AxiosError } from 'axios';
  import { apiClientes } from 'src/boot/axios';

  interface ErrorPayload {
    code?: string; // "429 TOO_MANY_REQUESTS" | "401 UNAUTHORIZED" | "400 BAD_REQUEST" | ...
    message?: string; // mensagens do backend
    seconds?: number; // segundos para expiracao
    otpTokenValid?: boolean; // presente em 400 BAD_REQUEST no reset
  }

  const router = useRouter();

  const step = ref<'request' | 'reset'>('request');

  const username = ref('');
  const otp = ref('');
  const password = ref('');
  const passwordConfirm = ref('');

  const showHelp = ref(false);

  const showPwd = ref(false);
  const showPwdConfirm = ref(false);

  const loading = ref({ request: false, reset: false });

  const alert = ref<{ type: 'info' | 'success' | 'error' | null; message: string }>({
    type: null,
    message: '',
  });
  const alertIcon = computed(() =>
    alert.value.type === 'error'
      ? 'error'
      : alert.value.type === 'success'
        ? 'check_circle'
        : 'info'
  );

  const cooldownSeconds = ref(0);
  let cooldownTimer: number | null = null;

  function setAlert(type: 'info' | 'success' | 'error', message: string) {
    alert.value = { type, message };
  }
  function clearAlert() {
    alert.value = { type: null, message: '' };
  }

  const validaUsername = (v: string) => !!v?.trim() || 'Informe o usuário';
  const validaOtp = (v: string) => !!v?.trim() || 'Informe o código (OTP)';
  const validaPassword = (v: string) => !!v?.trim() || 'Informe a nova senha';
  const validaPasswordConfirm = (v: string) => v === password.value || 'As senhas não conferem';

  const isUsernameReady = computed(() => !!username.value.trim());
  const isResetFormReady = computed(
    () => !!otp.value.trim() && !!password.value.trim() && passwordConfirm.value === password.value
  );

  function startCooldown(seconds = 60) {
    cooldownSeconds.value = seconds;
    if (cooldownTimer) window.clearInterval(cooldownTimer);
    cooldownTimer = window.setInterval(() => {
      if (cooldownSeconds.value <= 1) {
        window.clearInterval(cooldownTimer!);
        cooldownTimer = null;
        cooldownSeconds.value = 0;
      } else {
        cooldownSeconds.value -= 1;
      }
    }, 1000);
  }

  /** Passo 1: solicitar envio do OTP (ou reenviar) */
  async function requestReset(isResend = false) {
    if (!isUsernameReady.value) return;
    loading.value.request = true;
    clearAlert();
    try {
      await apiClientes.post('/clientes-api/v1/password-reset', {
        username: username.value.trim(),
      });
      // Sempre 200: não revelar existência do usuário
      setAlert(
        'info',
        isResend
          ? 'Se possível, reenviamos um novo código. Verifique SMS/E-mail.'
          : 'Se o usuário existir e tiver contato válido, você receberá um código por SMS/E-mail.'
      );
      step.value = 'reset';
    } catch (err) {
      const e = err as AxiosError<ErrorPayload>;
      if (e.response?.status === 429) {
        const secs = e.response.data?.seconds ?? 60;
        setAlert('error', e.response.data?.message || `Muitas tentativas. Aguarde ${secs}s.`);
        startCooldown(secs);
      } else {
        setAlert(
          'error',
          e.response?.data?.message || 'Não foi possível iniciar a recuperação de senha.'
        );
      }
    } finally {
      loading.value.request = false;
    }
  }

  /** Passo 2: validar OTP e redefinir senha */
  async function resetPassword() {
    if (!isResetFormReady.value) return;
    loading.value.reset = true;
    clearAlert();
    try {
      const url = '/clientes-api/v1/password-reset/reset';
      await apiClientes.post(url, {
        username: username.value.trim(),
        token: otp.value.trim(),
        password: password.value,
      });
      setAlert('success', 'Senha alterada com sucesso. Redirecionando para o login...');
      setTimeout(() => router.push('/login'), 700);
    } catch (err) {
      const e = err as AxiosError<ErrorPayload>;
      const msg = e.response?.data?.message || 'Falha ao redefinir a senha.';

      if (e.response?.status === 400) {
        // Token inválido; verificar flag
        if (e.response.data?.otpTokenValid === false) {
          setAlert('error', `${msg} — Solicite um novo código.`);
          // limpar OTP e voltar para solicitar novo envio
          otp.value = '';
          step.value = 'request';
          startCooldown(30);
        } else {
          setAlert('error', msg); // "Token Inválido. Restam X tentativas"
        }
      } else if (e.response?.status === 401) {
        setAlert('error', msg); // Processo não encontrado -> reiniciar
        otp.value = '';
        step.value = 'request';
      } else if (e.response?.status === 429) {
        const secs = e.response.data?.seconds ?? 60;
        setAlert('error', msg);
        startCooldown(secs);
      } else {
        setAlert('error', msg);
      }
    } finally {
      loading.value.reset = false;
    }
  }

  onBeforeUnmount(() => {
    if (cooldownTimer) {
      window.clearInterval(cooldownTimer);
      cooldownTimer = null;
    }
  });
</script>

<style scoped>
  .full-width {
    width: 100%;
  }
</style>
