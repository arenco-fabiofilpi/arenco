<!-- src/layouts/MinimalLayout.vue -->
<template>
  <q-layout>
    <q-page-container>
      <!-- Garante que SEMPRE há um QPage sob QLayout -->
      <q-page>
        <router-view />
      </q-page>
    </q-page-container>
  </q-layout>
</template>

<script setup lang="ts">
  // vazio mesmo
</script>
