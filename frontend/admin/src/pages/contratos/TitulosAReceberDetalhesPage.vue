<template>
  <div class="q-pa-md q-mt-lg">
    <q-btn label="Voltar" icon="arrow_back" flat @click="router.back()" class="q-mb-md" />

    <div v-if="carregando">
      <q-spinner />
    </div>

    <div v-else-if="!titulo">
      <q-banner type="warning" label="Título não encontrado." />
    </div>

    <div v-else>
      <TituloAReceberDetalhesCard :titulo="titulo" />
    </div>
  </div>
</template>

<script setup lang="ts">
  import { ref, onMounted } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { apiClientes } from 'src/boot/axios';
  import TituloAReceberDetalhesCard from 'src/components/TituloAReceberDetalhesCard.vue';

  interface Titulo {
    id: string;
    // Adicione aqui outras propriedades conforme necessário
  }

  const route = useRoute();
  const router = useRouter();

  const titulo = ref<Titulo | null>(null);
  const carregando = ref(true);

  onMounted(() => {
    carregarTituloAReceber();
  });

  async function carregarTituloAReceber() {
    const id = route.query.id;
    if (!id || typeof id !== 'string') {
      carregando.value = false;
      return;
    }

    try {
      const headers = {};
      const url = `/clientes-api/v1/admin/a-receber?id=${id}`;
      const { data } = await apiClientes.get<Titulo>(url, { headers });
      titulo.value = data;
    } catch (err) {
      console.error('Erro ao carregar título:', err);
    } finally {
      carregando.value = false;
    }
  }
</script>
