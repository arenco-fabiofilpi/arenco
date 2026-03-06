<template>
  <div class="flex flex-center bg-grey-1" style="min-height: 100vh;">
    <div class="text-center">
      <q-icon name="sentiment_dissatisfied" size="100px" color="grey-7" />
      <div class="text-h4 text-grey-8 q-mt-md">404 - Página não encontrada</div>
      <div class="text-subtitle2 text-grey-6 q-mt-sm">
        O endereço que você tentou acessar não existe ou foi movido.
      </div>

      <q-btn
        class="q-mt-lg"
        color="primary"
        unelevated
        label="Voltar para a página inicial"
        @click="$router.push('/admin/app')"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
  // nenhuma lógica extra por enquanto
</script>
