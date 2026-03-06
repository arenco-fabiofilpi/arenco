<template>
  <!-- o MainLayout já entrega a q-page + container -->
  <div>
    <!-- Cabeçalho local -->
    <div class="row items-center justify-between q-mb-lg">
      <div class="col">
        <div class="text-h5">Bem-vindo, {{ firstName }}</div>
        <div class="text-caption text-grey-7">Último acesso: {{ lastLoginFormatted }}</div>
      </div>

      <div class="row items-center q-gutter-sm">
        <q-btn icon="refresh" label="Atualizar" :loading="loadingAll" flat @click="refreshAll" />
        <q-btn
          color="primary"
          unelevated
          icon="receipt_long"
          label="2ª via de boleto"
          @click="goToBoletos"
        />
      </div>
    </div>

    <!-- KPIs -->
    <div class="row q-col-gutter-md q-mb-lg">
      <div class="col-12 col-sm-6 col-md-3" v-for="k in kpis" :key="k.label">
        <q-card flat bordered class="kpi-card">
          <q-card-section class="row items-center no-wrap">
            <q-avatar square size="42px" class="q-mr-md">
              <q-icon :name="k.icon" size="28px" />
            </q-avatar>
            <div class="col">
              <div class="text-caption text-grey-7">{{ k.label }}</div>
              <div class="text-h5 q-mt-xs">
                <q-skeleton v-if="loadingAll" type="text" width="60%" />
                <span v-else>{{ k.value }}</span>
              </div>
            </div>
          </q-card-section>
        </q-card>
      </div>
    </div>

    <div class="row q-col-gutter-lg">
      <!-- COLUNA PRINCIPAL -->
      <div class="col-12 col-lg-8">
        <!-- Próximas parcelas -->
        <q-card flat bordered class="q-mb-lg">
          <q-card-section class="row items-center justify-between">
            <div class="text-subtitle1">Próximas parcelas</div>
            <q-btn
              size="sm"
              flat
              icon="download"
              label="Baixar tudo (ZIP)"
              @click="baixarBoletosPendentesZip"
            />
          </q-card-section>

          <q-separator />

          <q-card-section>
            <q-skeleton v-if="loadingAll" type="rect" height="180px" />
            <q-table
              v-else
              :rows="proximasParcelas"
              :columns="parcelasColumns"
              row-key="id"
              flat
              dense
              :pagination="parcelasPagination"
              @update:pagination="(val) => (parcelasPagination = val)"
              no-data-label="Nenhuma parcela encontrada."
            >
              <template #body-cell-vencimento="props">
                <q-td :props="props">
                  <div class="row items-center no-wrap q-gutter-xs">
                    <span>{{ formatDate(props.row.vencimento) }}</span>
                    <q-badge
                      v-if="badgeVencimento(props.row) !== null"
                      :color="badgeVencimento(props.row)?.color"
                      outline
                      :label="badgeVencimento(props.row)?.label"
                    />
                  </div>
                </q-td>
              </template>

              <template #body-cell-status="props">
                <q-td :props="props">
                  <q-badge
                    :color="props.row.status === 'EM_ABERTO' ? 'orange' : 'positive'"
                    outline
                    :label="statusLabel(props.row.status)"
                  />
                </q-td>
              </template>

              <template #body-cell-boleto="props">
                <q-td :props="props" class="text-right">
                  <q-btn
                    size="sm"
                    icon="picture_as_pdf"
                    flat
                    :disable="!props.row.boletoDisponivel"
                    @click="baixarBoleto(props.row.boletoId)"
                    :label="props.row.boletoDisponivel ? 'PDF' : 'Indisp.'"
                  />
                </q-td>
              </template>
            </q-table>
          </q-card-section>
        </q-card>

        <!-- Últimos pagamentos -->
        <q-card flat bordered>
          <q-card-section class="row items-center justify-between">
            <div class="text-subtitle1">Últimos pagamentos</div>
            <q-btn flat size="sm" icon="open_in_new" label="Ver todos" @click="goToPagamentos" />
          </q-card-section>
          <q-separator />
          <q-card-section>
            <q-skeleton v-if="loadingAll" type="rect" height="140px" />
            <q-list v-else separator>
              <q-item v-for="p in ultimosPagamentos" :key="p.id">
                <q-item-section avatar>
                  <q-avatar color="positive" text-color="white">
                    <q-icon name="paid" />
                  </q-avatar>
                </q-item-section>
                <q-item-section>
                  <q-item-label class="text-weight-medium">
                    {{ p.contratoNumero }} — Parcela {{ p.parcela }}
                  </q-item-label>
                  <q-item-label caption>
                    Pago em {{ formatDate(p.dataPagamento) }} via {{ p.metodo }}
                  </q-item-label>
                </q-item-section>
                <q-item-section side>
                  <div class="text-weight-bold">{{ formatCurrency(p.valor) }}</div>
                </q-item-section>
              </q-item>
              <div v-if="!ultimosPagamentos.length" class="text-grey-7">
                Nenhum pagamento recente.
              </div>
            </q-list>
          </q-card-section>
        </q-card>
      </div>

      <!-- COLUNA LATERAL -->
      <div class="col-12 col-lg-4">
        <!-- Ações rápidas -->
        <q-card flat bordered class="q-mb-lg quick-actions sticky">
          <q-card-section class="row items-center justify-between">
            <div class="text-subtitle1">Ações rápidas</div>
          </q-card-section>
          <q-separator />
          <q-list padding>
            <q-item clickable v-ripple @click="goToContratos">
              <q-item-section avatar><q-icon name="description" /></q-item-section>
              <q-item-section>Meus contratos</q-item-section>
              <q-item-section side><q-icon name="chevron_right" /></q-item-section>
            </q-item>
            <q-item clickable v-ripple @click="goToBoletos">
              <q-item-section avatar><q-icon name="receipt_long" /></q-item-section>
              <q-item-section>Boletos / 2ª via</q-item-section>
              <q-item-section side><q-icon name="chevron_right" /></q-item-section>
            </q-item>
            <q-item clickable v-ripple @click="goToPagamentos">
              <q-item-section avatar><q-icon name="paid" /></q-item-section>
              <q-item-section>Comprovantes</q-item-section>
              <q-item-section side><q-icon name="chevron_right" /></q-item-section>
            </q-item>
            <q-item clickable v-ripple @click="goToDadosCadastrais">
              <q-item-section avatar><q-icon name="manage_accounts" /></q-item-section>
              <q-item-section>Dados cadastrais</q-item-section>
              <q-item-section side><q-icon name="chevron_right" /></q-item-section>
            </q-item>
            <q-item clickable v-ripple @click="goToSuporte">
              <q-item-section avatar><q-icon name="support_agent" /></q-item-section>
              <q-item-section>Falar com suporte</q-item-section>
              <q-item-section side><q-icon name="chevron_right" /></q-item-section>
            </q-item>
          </q-list>
        </q-card>

        <!-- Meus contratos (resumo) -->
        <q-card flat bordered>
          <q-card-section class="row items-center justify-between">
            <div class="text-subtitle1">Meus contratos</div>
            <q-btn flat size="sm" icon="open_in_new" label="Ver todos" @click="goToContratos" />
          </q-card-section>
          <q-separator />
          <q-card-section>
            <q-skeleton v-if="loadingAll" type="rect" height="180px" />
            <div v-else class="column q-gutter-md">
              <q-card
                v-for="c in contratosResumo"
                :key="c.id"
                flat
                bordered
                class="q-pa-sm"
                clickable
                @click="goToContrato(c.id)"
              >
                <div class="row items-center justify-between q-col-gutter-sm">
                  <div class="col">
                    <div class="text-body1 text-weight-medium">
                      {{ c.numero }} — {{ c.empreendimento }}
                    </div>
                    <div class="text-caption text-grey-7">
                      Saldo: {{ formatCurrency(c.saldoDevedor) }}
                    </div>
                  </div>
                  <div class="col-12 q-mt-sm">
                    <q-linear-progress :value="c.percentualPago" rounded />
                    <div class="text-caption text-grey-7 q-mt-xs">
                      {{ Math.round(c.percentualPago * 100) }}% pago
                    </div>
                  </div>
                </div>
              </q-card>
              <div v-if="!contratosResumo.length" class="text-grey-7">
                Nenhum contrato encontrado.
              </div>
            </div>
          </q-card-section>
        </q-card>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { ref, computed, onMounted } from 'vue';
  import { useRouter } from 'vue-router';
  import { date, useQuasar } from 'quasar';
  import type { QTableColumn } from 'quasar';

  type StatusParcela = 'EM_ABERTO' | 'PAGO';

  interface Parcela {
    id: string;
    contratoId: string;
    contratoNumero: string;
    parcela: number;
    vencimento: string; // ISO
    valor: number;
    status: StatusParcela;
    boletoDisponivel: boolean;
    boletoId?: string;
  }

  interface Pagamento {
    id: string;
    contratoNumero: string;
    parcela: number;
    dataPagamento: string; // ISO
    valor: number;
    metodo: 'PIX' | 'BOLETO' | 'TED' | 'CARTAO';
  }

  interface ResumoContrato {
    id: string;
    numero: string;
    empreendimento: string;
    saldoDevedor: number;
    percentualPago: number; // 0..1
  }

  const $q = useQuasar();
  const router = useRouter();

  // estado
  const loadingAll = ref(true);
  const userName = ref<string>('Cliente Arenco');
  const lastLoginIso = ref<string>(new Date().toISOString());

  const kpis = ref([
    { label: 'Contratos', value: 0, icon: 'description' },
    { label: 'Parcelas pagas', value: 0, icon: 'task_alt' },
    { label: 'Parcelas em aberto', value: 0, icon: 'pending_actions' },
    { label: 'Boletos pendentes', value: 0, icon: 'receipt_long' },
  ]);

  const proximasParcelas = ref<Parcela[]>([]);
  const parcelasColumns: QTableColumn<Parcela>[] = [
    {
      name: 'contratoNumero',
      label: 'Contrato',
      field: (row) => row.contratoNumero,
      align: 'left',
      sortable: true,
    },
    {
      name: 'parcela',
      label: 'Parcela',
      field: (row) => row.parcela,
      align: 'left',
      sortable: true,
    },
    {
      name: 'vencimento',
      label: 'Vencimento',
      field: (row) => row.vencimento,
      align: 'left',
      format: (val: string) => formatDate(val),
      sortable: true,
    },
    {
      name: 'valor',
      label: 'Valor',
      field: (row) => row.valor,
      align: 'right',
      format: (val: number) => formatCurrency(val),
      sortable: true,
    },
    {
      name: 'status',
      label: 'Status',
      field: (row) => row.status,
      align: 'left',
    },
    {
      name: 'boleto',
      label: 'Boleto',
      // como não existe a propriedade 'boleto' em Parcela, use função:
      field: (row) => row.boletoDisponivel,
      align: 'right',
    },
  ];

  // se quiser tipar a paginação também:
  type TablePagination = {
    page: number;
    rowsPerPage: number;
    sortBy?: string | null;
    descending?: boolean;
  };
  const parcelasPagination = ref<TablePagination>({ page: 1, rowsPerPage: 5 });
  const ultimosPagamentos = ref<Pagamento[]>([]);
  const contratosResumo = ref<ResumoContrato[]>([]);

  // computeds
  const firstName = computed(() => userName.value.split(' ')[0] || userName.value);
  const lastLoginFormatted = computed(() =>
    date.formatDate(lastLoginIso.value, 'DD/MM/YYYY HH:mm')
  );

  // helpers
  function formatDate(iso: string) {
    return date.formatDate(iso, 'DD/MM/YYYY');
  }
  function formatCurrency(n: number) {
    return n?.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
  }
  function statusLabel(s: StatusParcela) {
    return s === 'EM_ABERTO' ? 'Em aberto' : 'Pago';
  }

  // badge de vencimento: vencida/vermelho, hoje/amarelo, senão null
  function badgeVencimento(p: Parcela) {
    if (p.status === 'PAGO') return null;
    const hoje = new Date();
    hoje.setHours(0, 0, 0, 0);
    const venc = new Date(p.vencimento);
    venc.setHours(0, 0, 0, 0);
    if (venc.getTime() < hoje.getTime()) return { label: 'Vencida', color: 'negative' };
    if (venc.getTime() === hoje.getTime()) return { label: 'Vence hoje', color: 'warning' };
    return null;
  }

  // navegação
  function goToContratos() {
    router.push({ name: 'contratos' });
  }
  function goToContrato(id: string) {
    router.push({ name: 'contrato-detalhe', params: { id } });
  }
  function goToBoletos() {
    router.push({ name: 'boletos' });
  }
  function goToPagamentos() {
    router.push({ name: 'pagamentos' });
  }
  function goToDadosCadastrais() {
    router.push({ name: 'dados-cadastrais' });
  }
  function goToSuporte() {
    router.push({ name: 'suporte' });
  }

  // ações
  async function baixarBoleto(boletoId?: string) {
    if (!boletoId) return;
    try {
      $q.loading.show();
      // const { data } = await apiFinanceiro.get(`/boletos/${boletoId}`, { responseType: 'blob' })
      await new Promise((r) => setTimeout(r, 800));
      $q.notify({ type: 'positive', message: 'Boleto baixado com sucesso.' });
    } catch {
      $q.notify({ type: 'negative', message: 'Não foi possível baixar o boleto.' });
    } finally {
      $q.loading.hide();
    }
  }

  async function baixarBoletosPendentesZip() {
    try {
      $q.loading.show();
      // await apiFinanceiro.get('/boletos/pendentes/zip', { responseType: 'blob' })
      await new Promise((r) => setTimeout(r, 1000));
      $q.notify({ type: 'positive', message: 'Arquivo ZIP gerado com sucesso.' });
    } catch {
      $q.notify({ type: 'negative', message: 'Falha ao gerar ZIP.' });
    } finally {
      $q.loading.hide();
    }
  }

  async function refreshAll() {
    loadingAll.value = true;
    try {
      // chamadas reais aqui...
      await new Promise((r) => setTimeout(r, 600));
      const kpi = { contratos: 2, pagas: 18, abertas: 6, boletosPend: 3 };
      const parcelas: Parcela[] = [
        {
          id: '1',
          contratoId: 'C1',
          contratoNumero: 'A-101',
          parcela: 19,
          vencimento: addDaysIso(5),
          valor: 1850.5,
          status: 'EM_ABERTO',
          boletoDisponivel: true,
          boletoId: 'B001',
        },
        {
          id: '2',
          contratoId: 'C1',
          contratoNumero: 'A-101',
          parcela: 20,
          vencimento: addDaysIso(0),
          valor: 1850.5,
          status: 'EM_ABERTO',
          boletoDisponivel: false,
        },
        {
          id: '3',
          contratoId: 'C2',
          contratoNumero: 'B-220',
          parcela: 7,
          vencimento: addDaysIso(12),
          valor: 2300,
          status: 'EM_ABERTO',
          boletoDisponivel: true,
          boletoId: 'B002',
        },
        {
          id: '4',
          contratoId: 'C2',
          contratoNumero: 'B-220',
          parcela: 6,
          vencimento: addDaysIso(-10),
          valor: 2300,
          status: 'PAGO',
          boletoDisponivel: false,
        },
        {
          id: '5',
          contratoId: 'C1',
          contratoNumero: 'A-101',
          parcela: 18,
          vencimento: addDaysIso(-25),
          valor: 1850.5,
          status: 'EM_ABERTO',
          boletoDisponivel: false,
        },
      ];
      const pagamentos: Pagamento[] = [
        {
          id: 'p1',
          contratoNumero: 'A-101',
          parcela: 18,
          dataPagamento: addDaysIso(-22),
          valor: 1850.5,
          metodo: 'PIX',
        },
        {
          id: 'p2',
          contratoNumero: 'B-220',
          parcela: 6,
          dataPagamento: addDaysIso(-8),
          valor: 2300,
          metodo: 'BOLETO',
        },
      ];
      const contratos: ResumoContrato[] = [
        {
          id: 'C1',
          numero: 'A-101',
          empreendimento: 'Residencial Ipê',
          saldoDevedor: 38200.68,
          percentualPago: 0.47,
        },
        {
          id: 'C2',
          numero: 'B-220',
          empreendimento: 'Vila Araucária',
          saldoDevedor: 51800.12,
          percentualPago: 0.31,
        },
      ];

      kpis.value = [
        { label: 'Contratos', value: kpi.contratos, icon: 'description' },
        { label: 'Parcelas pagas', value: kpi.pagas, icon: 'task_alt' },
        { label: 'Parcelas em aberto', value: kpi.abertas, icon: 'pending_actions' },
        { label: 'Boletos pendentes', value: kpi.boletosPend, icon: 'receipt_long' },
      ];
      proximasParcelas.value = parcelas;
      ultimosPagamentos.value = pagamentos;
      contratosResumo.value = contratos;
    } finally {
      loadingAll.value = false;
    }
  }

  onMounted(() => {
    refreshAll();
  });

  function addDaysIso(delta: number) {
    const d = new Date();
    d.setHours(0, 0, 0, 0);
    d.setDate(d.getDate() + delta);
    return d.toISOString();
  }
</script>

<style scoped>
  .kpi-card {
    border-radius: 14px;
  }
  .quick-actions.sticky {
    position: sticky;
    top: 12px;
  }

  /* deixa a página mais agradável em telas muito largas */
  :deep(.q-table__bottom) {
    padding-left: 0;
    padding-right: 0;
  }
</style>
