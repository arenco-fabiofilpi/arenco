<template>
  <div class="page-login">
    <div class="q-pt-xl q-pa-md">
      <q-card class="q-pa-xs q-pb-lg" style="max-width: 400px; margin: auto">
        <q-card-section class="text-center">
          <q-img
            src="/clientes/logo-arenco.png"
            id="logoArenco"
            spinner-color="white"
            alt="Arenco"
            style="height: 80px; max-width: 150px"
          />
          <q-separator spaced />
          <div class="text-h6">Clientes</div>
        </q-card-section>

        <q-card-section>
          <form @submit.prevent="login">
            <q-input
              v-model="username"
              label="Usuário (CPF ou CNPJ)"
              outlined
              dense
              :rules="[validaUsername]"
              lazy-rules
              class="q-mb-md full-width"
              maxlength="18"
            />
            <q-separator spaced />
            <q-card-actions align="center">
              <q-btn
                label="Entrar"
                color="primary"
                :disable="!formValido"
                class="q-mt-md"
                type="submit"
              />
            </q-card-actions>
          </form>
        </q-card-section>

        <q-card-section v-if="errorMessage" class="text-negative text-center">
          {{ errorMessage }}
        </q-card-section>
      </q-card>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { ref, watch } from 'vue';
  import { useRouter } from 'vue-router';
  import type { AxiosError } from 'axios';
  import { apiClientes } from 'src/boot/axios';
  import { useAuthStore } from 'src/stores/auth';

  const username = ref('');
  const errorMessage = ref('');
  const router = useRouter();
  const auth = useAuthStore();

  // Validações
  const validaUsername = (val: string) => !!val || 'O nome de usuário é obrigatório';
  const formValido = ref(false);

  // --- Funções de formatação ---
  function formatCpfCnpj(value: string): string {
    const digits = value.replace(/\D/g, ''); // só números

    if (digits.length <= 11) {
      // CPF: 000.000.000-00
      return digits
        .replace(/^(\d{3})(\d)/, '$1.$2')
        .replace(/^(\d{3})\.(\d{3})(\d)/, '$1.$2.$3')
        .replace(/\.(\d{3})(\d)/, '.$1-$2')
        .substring(0, 14);
    } else {
      // CNPJ: 00.000.000/0000-00
      return digits
        .replace(/^(\d{2})(\d)/, '$1.$2')
        .replace(/^(\d{2})\.(\d{3})(\d)/, '$1.$2.$3')
        .replace(/\.(\d{3})(\d)/, '.$1/$2')
        .replace(/(\d{4})(\d)/, '$1-$2')
        .substring(0, 18);
    }
  }

  // Atualiza o estado do formulário + aplica formatação
  watch(username, (newVal) => {
    formValido.value = newVal.trim() !== '';
    username.value = formatCpfCnpj(newVal);
  });

  // --- Login ---
  async function login() {
    try {
      await apiClientes.post(
        '/clientes-api/v1/customer/auth/login',
        { username: username.value },
        { headers: { 'Content-Type': 'application/json' } }
      );
      auth.setPendingUsername(username.value);
      return router.push('/clientes/authentication');
    } catch (err) {
      const error = err as AxiosError<{ message: string }>;
      if (error.response) {
        errorMessage.value = error.response.data?.message;
        console.error('Erro no login:', error.response.data);
      } else {
        console.error('Erro no login:', error.message);
      }
    }
  }
</script>

<style scoped>
  .full-width {
    width: 100%;
  }
  .text-center {
    text-align: center;
  }
</style>
