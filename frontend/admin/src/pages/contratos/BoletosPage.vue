<template>
  <div class="q-pa-md">
    <h1 class="q-mt-lg q-mb-md">Boletos</h1>

    <!-- Filtros -->
    <q-btn
      color="primary"
      icon="filter_list"
      label="Filtros"
      class="q-mb-md"
      @click="filtrosVisiveis = !filtrosVisiveis"
    />

    <!-- Tabela -->
    <q-table
      :rows="lista"
      :columns="columns"
      row-key="id"
      :loading="loading"
      v-model:pagination="pagination"
      @request="fetchApi"
      no-data-label="Nenhum Boleto encontrado."
      @row-click="baixar"
    />
  </div>
</template>

<script setup lang="ts">
  import { ref, watch } from 'vue';
  import { useRouter } from 'vue-router';
  import { apiClientes } from 'src/boot/axios';

  // Dados da tabela
  const lista = ref([]);
  const filtrosVisiveis = ref(false);
  const loading = ref(false);
  // Router para redirecionamento
  const router = useRouter();

  const pagination = ref({
    page: 1,
    rowsPerPage: 10,
    rowsNumber: 0,
  });

  // Configuração das colunas
  const columns = [
    { name: 'dateCreated', align: 'left' as const, label: 'Data de Criação', field: 'dateCreated' },
    { name: 'fileType', align: 'left' as const, label: 'Tipo de Arquivo', field: 'fileType' },
    { name: 'cliente', align: 'left' as const, label: 'Cliente', field: 'cliente' },
    { name: 'nomeClte', align: 'left' as const, label: 'Nome Cliente', field: 'nomeClte' },
    { name: 'numeDoc', align: 'left' as const, label: 'Contrato', field: 'numeDoc' },
    { name: 'sequencia', align: 'left' as const, label: 'Sequência', field: 'sequencia' },
    { name: 'lote', align: 'left' as const, label: 'Lote', field: 'lote' },
    { name: 'dtEmissao', align: 'left' as const, label: 'Data Emissão', field: 'dtEmissao' },
    { name: 'dataBase', align: 'left' as const, label: 'Data Base', field: 'dataBase' },
    { name: 'ccusto', align: 'left' as const, label: 'Centro de Custos', field: 'ccusto' },
    {
      name: 'nomeCcusto',
      align: 'left' as const,
      label: 'Nome Centro de Custos',
      field: 'nomeCcusto',
    },
    { name: 'fatura', align: 'left' as const, label: 'Fatura', field: 'fatura' },
    { name: 'numeFatura', align: 'left' as const, label: 'Número da Fatura', field: 'numeFatura' },
    { name: 'vencimento', align: 'left' as const, label: 'Vencimento', field: 'vencimento' },
    { name: 'valor', align: 'left' as const, label: 'Valor', field: 'valor' },
    { name: 'tipoDoc', align: 'left' as const, label: 'Tipo Documento', field: 'tipoDoc' },
    { name: 'serie', align: 'left' as const, label: 'Série', field: 'serie' },
    { name: 'observacao', align: 'left' as const, label: 'Observação', field: 'observacao' },
  ];

  // Busca na API
  const fetchApi = async (props: any) => {
    loading.value = true;
    try {
      if (!props.pagination) pagination.value.rowsPerPage = 10;
      else pagination.value.rowsPerPage = props.pagination.rowsPerPage;

      if (!props.pagination) pagination.value.page = 1;
      else pagination.value.page = props.pagination.page;

      const response = await apiClientes.post(
        `/clientes-api/v1/admin/boletos/search?page=${pagination.value.page}&size=${pagination.value.rowsPerPage}`,
        {
          headers: {
            'Content-Type': 'application/json',
          },
        },
        {}
      );

      lista.value = response.data.content;
      pagination.value.rowsNumber = response.data.totalElements || 0;
    } catch (error: any) {
      if (error.response?.status === 403) {
        router.push('/login');
      } else {
        console.error('Erro ao buscar info:', error);
      }
    } finally {
      loading.value = false;
    }
  };

  // Atualiza a lista ao mudar paginação
  watch(pagination, fetchApi, { immediate: true });

  function extrairFilename(contentDisposition?: string | null, fallback = 'arquivo') {
    if (!contentDisposition) return fallback;
    // tenta filename*=UTF-8''nome.ext (RFC 5987)
    const utf8 = /filename\*\s*=\s*UTF-8''([^;]+)/i.exec(contentDisposition);
    if (utf8?.[1]) return decodeURIComponent(utf8[1]);

    // tenta filename="nome.ext"
    const ascii = /filename\s*=\s*"([^"]+)"/i.exec(contentDisposition);
    if (ascii?.[1]) return ascii[1];

    // tenta filename=nome.ext
    const plain = /filename\s*=\s*([^;]+)/i.exec(contentDisposition);
    if (plain?.[1]) return plain[1].trim();

    return fallback;
  }

  async function baixar(evt: Event, row: any) {
    const boletoId = row.id;
    const formato = row.fileType;
    if (!boletoId) return;

    try {
      const url = `/clientes-api/v1/admin/boletos/${encodeURIComponent(boletoId)}`;
      const response = await apiClientes.get(url, {
        responseType: 'blob', // <- importante!
      });

      const contentType =
        response.headers['content-type'] || (formato === 'PDF' ? 'application/pdf' : 'image/png');
      const contentDisposition = response.headers['content-disposition'] as string | undefined;

      const nomeFallback = `boleto-${boletoId}.${formato}`;
      const filename = extrairFilename(contentDisposition, nomeFallback);

      const blob = new Blob([response.data], { type: contentType });
      const href = URL.createObjectURL(blob);

      const a = document.createElement('a');
      a.href = href;
      a.download = filename;
      // se quiser abrir em nova aba quando for inline:
      // a.target = '_blank'
      document.body.appendChild(a);
      a.click();
      a.remove();
      URL.revokeObjectURL(href);
    } catch (err) {
      console.error(`Erro ao baixar boleto ${formato}:`, err);
    }
  }
</script>

<style scoped>
  .text-h6 {
    font-size: 1.25rem;
    font-weight: bold;
  }
</style>
