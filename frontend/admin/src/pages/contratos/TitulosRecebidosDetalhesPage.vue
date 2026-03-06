<template>
  <div class="q-pa-md q-mt-lg">
    <q-btn label="Voltar" icon="arrow_back" flat @click="router.back()" class="q-mb-md" />

    <!-- Detalhes do titulo -->
    <TituloRecebidoDetalhesCard v-if="titulo" :titulo="titulo" />

    <div v-if="carregando">
      <q-spinner />
    </div>

    <div v-else-if="!titulo">
      <q-banner type="warning" label="Titulo não encontrado." />
    </div>
  </div>
</template>

<script setup lang="ts">
  import { ref, onMounted } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { apiClientes } from 'src/boot/axios';
  import TituloRecebidoDetalhesCard from 'src/components/TituloRecebidoDetalhesCard.vue';

  const route = useRoute();
  const router = useRouter();

  const titulo = ref<any | null>(null);
  const carregando = ref(true);

  async function carregarTituloRecebidos() {
    const id = route.query.id;
    if (!id || typeof id !== 'string') {
      carregando.value = false;
      return;
    }

    try {
      const headers = {};

      const tituloResp = await apiClientes.get(`/clientes-api/v1/admin/recebidos?id=${id}`, {
        headers,
      });
      titulo.value = tituloResp.data;
    } catch (err) {
      console.error('Erro ao carregar titulo:', err);
    } finally {
      carregando.value = false;
    }
  }

  onMounted(carregarTituloRecebidos);
</script>
