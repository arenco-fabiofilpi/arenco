<template>
  <q-layout view="hHh lpR fFf">
    <!-- Cabeçalho -->
    <q-header reveal elevated class="text-white">
      <q-toolbar>
        <q-btn dense flat icon="menu" aria-label="Abrir menu" @click="toggleLeftDrawer" />
        <q-toolbar-title class="q-mx-auto flex flex-center">
          <q-img
            src="/logo-arenco.png"
            @click="goToPage('/admin/app')"
            id="logoArenco"
            spinner-color="white"
            alt="Arenco"
            style="height: 80px; max-width: 150px; cursor: pointer"
          />
        </q-toolbar-title>
      </q-toolbar>
    </q-header>

    <!-- Menu Lateral -->
    <q-drawer show-if-above v-model="leftDrawerOpen" side="left" elevated>
      <q-list padding>
        <q-item clickable v-ripple @click="goToPage('/admin/app')" aria-label="Home">
          <q-item-section avatar><q-icon name="home" /></q-item-section>
          <q-item-section>Home</q-item-section>
        </q-item>

        <q-separator spaced />

        <!-- Clientes -->
        <q-expansion-item icon="people" label="Clientes" expand-separator dense>
          <q-item
            :to="{ path: '/admin/app/clientes' }"
            clickable
            v-ripple
            exact
            active-class="text-primary"
          >
            <q-item-section avatar><q-icon name="person" /></q-item-section>
            <q-item-section>Clientes</q-item-section>
          </q-item>
          <q-item
            :to="{ path: '/admin/app/enderecos' }"
            clickable
            v-ripple
            exact
            active-class="text-primary"
          >
            <q-item-section avatar><q-icon name="place" /></q-item-section>
            <q-item-section>Endereços</q-item-section>
          </q-item>
          <q-item
            :to="{ path: '/admin/app/contatos' }"
            clickable
            v-ripple
            exact
            active-class="text-primary"
          >
            <q-item-section avatar><q-icon name="contact_phone" /></q-item-section>
            <q-item-section>Contatos</q-item-section>
          </q-item>
          <q-item
            :to="{ path: '/admin/app/preferencias-contatos' }"
            clickable
            v-ripple
            exact
            active-class="text-primary"
          >
            <q-item-section avatar><q-icon name="tune" /></q-item-section>
            <q-item-section>Preferências de contato</q-item-section>
          </q-item>
        </q-expansion-item>

        <q-separator spaced />

        <!-- Contratos -->
        <q-expansion-item icon="description" label="Contratos" expand-separator dense>
          <q-item
            :to="{ path: '/admin/app/contratos' }"
            clickable
            v-ripple
            exact
            active-class="text-primary"
          >
            <q-item-section avatar><q-icon name="description" /></q-item-section>
            <q-item-section>Contratos</q-item-section>
          </q-item>
          <q-item
            :to="{ path: '/admin/app/a-receber' }"
            clickable
            v-ripple
            exact
            active-class="text-primary"
          >
            <q-item-section avatar>
              <q-icon name="request_page" />
            </q-item-section>

            <!-- Texto alinhado à esquerda -->
            <q-item-section> A Receber </q-item-section>

            <!-- Badge alinhado à direita -->
            <q-item-section side v-if="kpis?.titulosVencendoHoje">
              <q-badge color="negative" outline>
                {{ kpis.titulosVencendoHoje }}
              </q-badge>
            </q-item-section>
          </q-item>
          <q-item
            :to="{ path: '/admin/app/recebidos' }"
            clickable
            v-ripple
            exact
            active-class="text-primary"
          >
            <q-item-section avatar><q-icon name="check_circle" /></q-item-section>
            <q-item-section>Recebidos</q-item-section>
          </q-item>
          <q-item
            :to="{ path: '/admin/app/boletos' }"
            clickable
            v-ripple
            exact
            active-class="text-primary"
          >
            <q-item-section avatar><q-icon name="receipt_long" /></q-item-section>
            <q-item-section>Boletos</q-item-section>
          </q-item>
        </q-expansion-item>

        <q-separator spaced />

        <!-- Sincronizações -->
        <q-expansion-item icon="sync" label="Sincronizações" expand-separator dense>
          <q-item
            :to="{ path: '/admin/app/sincronizacoes/bases' }"
            clickable
            v-ripple
            exact
            active-class="text-primary"
          >
            <q-item-section avatar><q-icon name="storage" /></q-item-section>
            <q-item-section>Sincronização de Bases</q-item-section>
          </q-item>

          <q-item
            :to="{ path: '/admin/app/sincronizacoes/boletos' }"
            clickable
            v-ripple
            exact
            active-class="text-primary"
          >
            <q-item-section avatar><q-icon name="receipt_long" /></q-item-section>
            <q-item-section>Sincronização de Boletos</q-item-section>
          </q-item>
        </q-expansion-item>

        <q-separator spaced />

        <!-- Mensageria -->
        <q-expansion-item icon="mark_email_unread" label="Mensageria" expand-separator dense>
          <q-item
            :to="{ path: '/admin/app/emails-enviados' }"
            clickable
            v-ripple
            exact
            active-class="text-primary"
          >
            <q-item-section avatar><q-icon name="mail" /></q-item-section>
            <q-item-section>E-mails enviados</q-item-section>
          </q-item>
          <q-item
            :to="{ path: '/admin/app/sms-enviados' }"
            clickable
            v-ripple
            exact
            active-class="text-primary"
          >
            <q-item-section avatar><q-icon name="sms" /></q-item-section>
            <q-item-section>SMS enviados</q-item-section>
          </q-item>
          <q-item
            :to="{ path: '/admin/app/templates-email' }"
            clickable
            v-ripple
            exact
            active-class="text-primary"
          >
            <q-item-section avatar><q-icon name="drafts" /></q-item-section>
            <q-item-section>Templates de e-mail</q-item-section>
          </q-item>
        </q-expansion-item>

        <q-separator spaced />

        <!-- Segurança -->
        <q-expansion-item icon="security" label="Segurança" expand-separator dense>
          <q-item
            :to="{ path: '/admin/app/sessoes-ativas' }"
            clickable
            v-ripple
            exact
            active-class="text-primary"
            v-if="can('security.sessions:view')"
          >
            <q-item-section avatar><q-icon name="schedule" /></q-item-section>
            <q-item-section>Sessões ativas</q-item-section>
          </q-item>
          <q-item
            :to="{ path: '/admin/app/atividades-suspeitas' }"
            clickable
            v-ripple
            exact
            active-class="text-primary"
            v-if="can('security.threats:view')"
          >
            <q-item-section avatar><q-icon name="warning" /></q-item-section>
            <q-item-section>Atividades suspeitas</q-item-section>
          </q-item>
          <q-item
            :to="{ path: '/admin/app/ips-bloqueados' }"
            clickable
            v-ripple
            exact
            active-class="text-primary"
            v-if="can('security.ips:view')"
          >
            <q-item-section avatar><q-icon name="block" /></q-item-section>
            <q-item-section>IPs bloqueados</q-item-section>
          </q-item>
          <q-item
            :to="{ path: '/admin/app/usuarios-bloqueados' }"
            clickable
            v-ripple
            exact
            active-class="text-primary"
            v-if="can('security.users:view')"
          >
            <q-item-section avatar><q-icon name="person_off" /></q-item-section>
            <q-item-section>Usuários bloqueados</q-item-section>
          </q-item>
        </q-expansion-item>

        <q-separator spaced />

        <!-- Configurações -->
        <q-expansion-item icon="settings" label="Configurações" expand-separator dense>
          <q-item
            :to="{ path: '/admin/app/administradores' }"
            clickable
            v-ripple
            exact
            active-class="text-primary"
            v-if="can('admins:view')"
          >
            <q-item-section avatar><q-icon name="supervisor_account" /></q-item-section>
            <q-item-section>Administradores</q-item-section>
          </q-item>
          <q-item
            :to="{ path: '/admin/app/config-envios-automaticos' }"
            clickable
            v-ripple
            exact
            active-class="text-primary"
            v-if="can('messaging:auto-config:view')"
          >
            <q-item-section avatar><q-icon name="send" /></q-item-section>
            <q-item-section>Configurações de envios automáticos</q-item-section>
          </q-item>
          <q-item
            :to="{ path: '/admin/app/config-boletos' }"
            clickable
            v-ripple
            exact
            active-class="text-primary"
            v-if="can('billing:slips-config:view')"
          >
            <q-item-section avatar><q-icon name="receipt_long" /></q-item-section>
            <q-item-section>Configurações de geração de boletos</q-item-section>
          </q-item>
          <q-item
            :to="{ path: '/admin/app/config-login' }"
            clickable
            v-ripple
            exact
            active-class="text-primary"
            v-if="can('security:login-config:view')"
          >
            <q-item-section avatar><q-icon name="login" /></q-item-section>
            <q-item-section>Configurações de login</q-item-section>
          </q-item>
        </q-expansion-item>

        <q-separator spaced />

        <!-- Sair -->
        <q-item clickable v-ripple @click="logout" aria-label="Sair da aplicação">
          <q-item-section avatar><q-icon name="logout" /></q-item-section>
          <q-item-section>Sair</q-item-section>
        </q-item>
      </q-list>
    </q-drawer>

    <!-- Conteúdo -->
    <q-page-container>
      <router-view />
    </q-page-container>
  </q-layout>
</template>

<script>
  import { ref } from 'vue';
  import { useRouter } from 'vue-router';
  import { apiClientes } from 'src/boot/axios';

  export default {
    setup() {
      const leftDrawerOpen = ref(false);
      const router = useRouter();

      // AGORA visíveis no template:
      const can = (p) => true;
      const kpis = { titulosVencendoHoje: 3 };

      function toggleLeftDrawer() {
        leftDrawerOpen.value = !leftDrawerOpen.value;
      }

      function goToPage(path) {
        router.push(path);
      }

      async function logout() {
        try {
          await apiClientes.post('/clientes-api/v1/auth/logout');
        } catch (error) {
          console.error('Erro ao fazer logout:', error);
        } finally {
          // Limpa os tokens e redireciona para login
          router.push('/login');
        }
      }

      return {
        leftDrawerOpen,
        toggleLeftDrawer,
        goToPage,
        logout,
        can,
        kpis,
      };
    },
  };
</script>
<style>
  .q-drawer__content {
    box-sizing: border-box;
    overflow-y: auto;
  }

  /* Aplica padding-top só em telas pequenas */
  @media (max-width: 1023px) {
    .q-drawer__content {
      padding-top: 80px; /* altura do header no mobile */
    }
  }

  /* Para telas maiores, remove o padding ou reduz */
  @media (min-width: 1024px) {
    .q-drawer__content {
      padding-top: 0; /* ou um valor menor, se quiser */
    }
  }
</style>

<style scoped>
  .q-toolbar-title {
    font-weight: bold;
    font-size: 18px;
  }

  .q-header {
    z-index: 10;
  }

  .q-drawer {
    background-color: #f5f5f5;
  }

  .q-item {
    font-size: 16px;
  }
</style>
