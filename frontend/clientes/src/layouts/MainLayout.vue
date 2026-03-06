<template>
  <q-layout view="lHh Lpr lFf">
    <!-- HEADER -->
    <q-header elevated class="q-px-md">
      <q-toolbar>
        <q-btn flat dense round icon="menu" aria-label="Abrir menu" @click="toggleDrawer" />

        <q-toolbar-title class="ellipsis"> Construtora Arenco — Área do Cliente </q-toolbar-title>

        <!-- empurra tudo pra direita -->
        <q-space />

        <!-- bloco direito sem quebra -->
        <div class="row items-center no-wrap q-gutter-xs">
          <q-chip
            dense
            square
            color="grey-3"
            text-color="grey-8"
            icon="badge"
            style="height: 28px"
            v-if="role"
            >{{ role }}</q-chip
          >

          <q-btn
            flat
            dense
            round
            :icon="$q.dark.isActive ? 'dark_mode' : 'light_mode'"
            aria-label="Alternar tema"
            @click="toggleDark"
          />

          <q-separator vertical spaced />

          <q-avatar size="28px">
            <img src="avatar-default.png" alt="Avatar" />
          </q-avatar>

          <span class="text-caption q-ml-xs">{{ firstName }}</span>

          <q-btn flat dense icon="logout" label="Sair" class="q-ml-sm" @click="logout" />
        </div>
      </q-toolbar>
    </q-header>

    <!-- DRAWER -->
    <q-drawer
      v-model="leftDrawerOpen"
      :behavior="$q.screen.gt.sm ? 'desktop' : 'mobile'"
      :mini="$q.screen.gt.sm ? mini : false"
      :mini-to-overlay="false"
      show-if-above
      :width="260"
      bordered
      @mouseover="mini = false"
      @mouseleave="mini = $q.screen.gt.sm ? true : false"
    >
      <div class="q-pa-md flex items-center">
        <q-img
          src="logo-arenco.png"
          alt="Arenco"
          style="height: 42px; width: auto"
          spinner-color="primary"
          :ratio="16 / 9"
        />
      </div>
      <q-separator />

      <q-scroll-area class="fit">
        <q-list padding>
          <q-item-label header class="text-grey-7 q-mt-none">Navegação</q-item-label>

          <q-item
            v-for="link in links"
            :key="link.to.name"
            clickable
            v-ripple
            :to="link.to"
            exact
            active-class="text-primary bg-grey-2"
          >
            <q-item-section avatar>
              <q-icon :name="link.icon" />
            </q-item-section>
            <q-item-section>
              <q-item-label>{{ link.label }}</q-item-label>
              <q-item-label caption v-if="link.caption">{{ link.caption }}</q-item-label>
            </q-item-section>
          </q-item>
        </q-list>
      </q-scroll-area>

      <div class="q-pa-md">
        <q-btn
          outline
          icon="support_agent"
          label="Suporte"
          class="full-width"
          :to="{ name: 'suporte' }"
        />
      </div>
    </q-drawer>

    <!-- CONTEÚDO -->
    <q-page-container>
      <q-page class="q-pa-md">
        <!-- wrapper para largura legível -->
        <div class="container">
          <router-view />
        </div>
      </q-page>
    </q-page-container>
  </q-layout>
</template>

<script setup lang="ts">
  import { ref, computed, onMounted, watch } from 'vue';
  import { useQuasar } from 'quasar';
  import { useRoute, useRouter } from 'vue-router';
  // import { useAuthStore } from 'src/stores/auth'

  const $q = useQuasar();
  const router = useRouter();
  const route = useRoute();

  const leftDrawerOpen = ref($q.screen.gt.sm);
  const mini = ref($q.screen.gt.sm); // mini-variant em desktop

  // troque por store real
  const userName = ref('Cliente Arenco');
  const role = ref('Cliente');
  const firstName = computed(() => userName.value.split(' ')[0] || userName.value);

  const links = [
    { to: { name: 'home' }, icon: 'home', label: 'Início', caption: 'Página de Início' },
    {
      to: { name: 'contratos' },
      icon: 'description',
      label: 'Meus contratos',
      caption: 'Página de Contratos',
    },
    {
      to: { name: 'boletos' },
      icon: 'receipt_long',
      label: 'Boletos / 2ª via',
      caption: 'Página de Boletos',
    },
    {
      to: { name: 'pagamentos' },
      icon: 'paid',
      label: 'Pagamentos',
      caption: 'Página de Pagamentos',
    },
    {
      to: { name: 'dados-cadastrais' },
      icon: 'manage_accounts',
      label: 'Dados cadastrais',
      caption: 'Página de Dados Cadastrais',
    },
  ];

  function toggleDark() {
    $q.dark.set(!$q.dark.isActive);
  }
  function toggleDrawer() {
    // se estiver mini e aberto, expandir; senão, alternar aberto/fechado
    if (leftDrawerOpen.value && mini.value) mini.value = false;
    else leftDrawerOpen.value = !leftDrawerOpen.value;
  }
  function logout() {
    router.push({ name: 'login' });
  }

  watch(
    () => route.fullPath,
    () => {
      if (!$q.screen.gt.sm) {
        leftDrawerOpen.value = false;
        mini.value = false;
      }
    }
  );
  onMounted(() => {
    leftDrawerOpen.value = $q.screen.gt.sm;
    mini.value = $q.screen.gt.sm;
  });
</script>

<style scoped>
  .container {
    max-width: 1200px;
    margin: 0 auto;
  }
  .toolbar-actions .q-btn,
  .toolbar-actions .q-avatar,
  .toolbar-actions .q-chip {
    align-self: center;
  }

  /* esconde elementos de texto em telas bem pequenas */
  .hide--xs {
    display: none;
  }
  @media (min-width: 480px) {
    .hide--xs {
      display: inline-flex;
    }
  }
  .hide--sm {
    display: none;
  }
  @media (min-width: 700px) {
    .hide--sm {
      display: inline;
    }
  }
</style>
