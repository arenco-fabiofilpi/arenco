<template>
  <div class="page-login">
    <div class="q-pt-xl q-pa-md">
      <q-card class="q-pa-xs q-pb-lg" style="max-width: 400px; margin: auto">
        <q-card-section class="text-center">
          <q-img
            src="/logo-arenco.png"
            id="logoArenco"
            spinner-color="white"
            alt="Arenco"
            style="height: 80px; max-width: 150px"
          />
          <div class="text-h6">Login</div>
        </q-card-section>

        <q-card-section>
          <form @submit.prevent="login">
            <q-input
              v-model="username"
              label="Usuário"
              outlined
              dense
              :rules="[validaUsername]"
              lazy-rules
              class="q-mb-md full-width"
            />
            <q-input
              v-model="password"
              label="Senha"
              type="password"
              outlined
              dense
              :rules="[validaPassword]"
              lazy-rules
              class="full-width"
            />
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

        <q-card-actions align="center">
          <q-btn
            flat
            color="primary"
            no-caps
            label="Esqueci minha senha"
            to="/esqueci-minha-senha"
          />
        </q-card-actions>
      </q-card>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { ref, watch } from 'vue';
  import { useRouter } from 'vue-router';
  import type { AxiosError } from 'axios';
  import { apiClientes } from 'src/boot/axios';

  const username = ref('');
  const password = ref('');
  const errorMessage = ref('');
  const router = useRouter();

  // Validações
  const validaUsername = (val: string) => !!val || 'O nome de usuário é obrigatório';
  const validaPassword = (val: string) => !!val || 'A senha é obrigatória';

  const formValido = ref(false);

  // Atualiza o estado do formulário
  watch([username, password], () => {
    formValido.value = username.value.trim() !== '' && password.value.trim() !== '';
  });

  async function login() {
    try {
      const resp = await apiClientes.post(
        '/clientes-api/v1/auth/login',
        {
          username: username.value,
          password: password.value,
        },
        {
          headers: {
            'Content-Type': 'application/json',
          },
        }
      );
      if (resp.status === 206) {
        return router.push('/login-2fa');
      }
      return router.push('/admin/app');
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
