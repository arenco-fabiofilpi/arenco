<template>
  <div class="q-pa-md q-mt-lg">
    <q-btn label="Voltar" icon="arrow_back" flat @click="router.back()" class="q-mb-md" />

    <!-- Detalhes -->
    <ContratoDetalhesCard v-if="contrato" :contrato="contrato" />

    <div v-if="carregando">
      <q-spinner />
    </div>

    <div v-else-if="!contrato">
      <q-banner type="warning" label="Contrato não encontrado." />
    </div>
  </div>
</template>

<script setup lang="ts">
  import { ref, onMounted } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { apiClientes } from 'src/boot/axios';
  import ContratoDetalhesCard from 'src/components/ContratoDetalhesCard.vue';

  const route = useRoute();
  const router = useRouter();

  const contrato = ref<any | null>(null);
  const carregando = ref(true);

  async function carregarDetalhes() {
    const id = route.query.id;
    if (!id || typeof id !== 'string') {
      carregando.value = false;
      return;
    }

    try {
      const headers = {};

      const response = await apiClientes.get(`/clientes-api/v1/admin/contratos?id=${id}`, {
        headers,
      });
      contrato.value = response.data;
    } catch (err) {
      console.error('Erro ao carregar titulo:', err);
    } finally {
      carregando.value = false;
    }
  }

  onMounted(carregarDetalhes);
</script>
