<template>
  <q-page class="q-pa-md">
    <!-- ROW do cabeçalho (somente ele) -->
    <div class="row q-col-gutter-lg">
      <div class="col-12">
        <div class="row items-center">
          <div class="col">
            <div class="text-h5">Bem-vindo, {{ userName }}</div>
            <div class="text-caption text-grey">{{ today }}</div>
          </div>
        </div>
      </div>
    </div>

    <!-- ROW do conteúdo (coluna principal + lateral) -->
    <div class="row q-col-gutter-lg">
      <!-- COLUNA PRINCIPAL -->
      <div class="col-12 col-lg-8">
        <!-- KPIs -->
        <div class="row q-col-gutter-md q-mb-lg items-stretch">
          <div class="col-12 col-sm-6 col-md-3" v-for="kpi in kpis" :key="kpi.label">
            <q-card flat bordered class="q-pa-md text-center kpi-card">
              <q-icon :name="kpi.icon" size="2em" class="q-mb-sm text-primary" />
              <div class="text-h6">{{ kpi.value }}</div>
              <div class="text-caption text-grey">{{ kpi.label }}</div>
            </q-card>
          </div>
        </div>

        <!-- Gráfico -->
        <q-card flat bordered class="q-mb-lg">
          <q-card-section>
            <div class="text-subtitle1 q-mb-sm">Recebimentos últimos 30 dias</div>
            <div class="chart-wrap">
              <line-chart :data="chartData" :labels="chartLabels" />
            </div>
          </q-card-section>
        </q-card>

        <!-- Últimas atividades -->
        <q-card flat bordered>
          <q-card-section>
            <div class="text-subtitle1 q-mb-md">Últimas atividades</div>
            <q-timeline color="primary">
              <q-timeline-entry
                v-for="log in logs"
                :key="log.id"
                :title="log.title"
                :subtitle="log.time"
                :icon="log.icon"
              >
                {{ log.desc }}
              </q-timeline-entry>
            </q-timeline>
          </q-card-section>
        </q-card>
      </div>

      <!-- COLUNA LATERAL -->
      <div class="col-12 col-lg-4">
        <q-card flat bordered class="q-mb-lg sticky-shortcuts">
          <q-card-section>
            <div class="text-subtitle1 q-mb-md">Atalhos rápidos</div>
            <q-list>
              <q-item clickable v-ripple @click="goTo('/admin/app/clientes')">
                <q-item-section avatar><q-icon name="person_add" /></q-item-section>
                <q-item-section>Novo Cliente</q-item-section>
              </q-item>
              <q-item clickable v-ripple @click="goTo('/admin/app/contratos')">
                <q-item-section avatar><q-icon name="description" /></q-item-section>
                <q-item-section>Contratos</q-item-section>
              </q-item>
              <q-item clickable v-ripple @click="goTo('/admin/app/config-boletos')">
                <q-item-section avatar><q-icon name="receipt_long" /></q-item-section>
                <q-item-section>Configurações de Boletos</q-item-section>
              </q-item>
            </q-list>
          </q-card-section>
        </q-card>
      </div>
    </div>
  </q-page>
</template>

<script setup lang="ts">
  import { ref } from 'vue';

  const userName = 'Fabio';
  const today = new Date().toLocaleDateString('pt-BR', {
    weekday: 'long',
    day: '2-digit',
    month: 'long',
    year: 'numeric',
  });

  // KPIs
  const kpis = ref([
    { label: 'Clientes ativos', value: 128, icon: 'people' },
    { label: 'Contratos ativos', value: 54, icon: 'description' },
    { label: 'Títulos a vencer hoje', value: 7, icon: 'event' },
    { label: 'Mensagens enviadas hoje', value: 312, icon: 'mail' },
  ]);

  // Gráfico (mock)
  const chartLabels = ['01', '05', '10', '15', '20', '25', '30'];
  const chartData = [1000, 1200, 800, 1500, 1700, 900, 2000];

  // Logs
  const logs = ref([
    {
      id: 1,
      title: 'Contrato criado',
      time: '10:15',
      icon: 'description',
      desc: 'Contrato #123 criado para Cliente X',
    },
    {
      id: 2,
      title: 'Boleto enviado',
      time: '09:40',
      icon: 'receipt_long',
      desc: 'Boleto de R$ 1.200 enviado para Cliente Y',
    },
    {
      id: 3,
      title: 'Login suspeito',
      time: '08:22',
      icon: 'warning',
      desc: 'Tentativa inválida de login do IP 177.xxx',
    },
  ]);

  function goTo(path: string) {
    // router.push(path)
    console.log('Ir para', path);
  }
</script>

<style scoped>
  /* Mesma altura para os cards de KPI */
  .kpi-card {
    height: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
  }

  /* Dá uma altura mínima para a área do gráfico (ajuda a manter alinhamento visual) */
  .chart-wrap {
    min-height: 260px;
    display: flex;
    align-items: center;
  }

  .sticky-shortcuts {
    position: sticky;
    top: 80px; /* ajusta conforme altura do header */
    z-index: 10; /* garante que não fique atrás de outros elementos */
  }
</style>
