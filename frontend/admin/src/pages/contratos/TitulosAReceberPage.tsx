import { useState, useEffect, useCallback } from 'react'
import { useNavigate } from 'react-router-dom'
import { Box, Typography, Button, Stack } from '@mui/material'
import FilterListIcon from '@mui/icons-material/FilterList'
import ClearAllIcon from '@mui/icons-material/ClearAll'
import ReceiptLongIcon from '@mui/icons-material/ReceiptLong'
import DeleteIcon from '@mui/icons-material/Delete'
import { useSnackbar } from 'notistack'
import { apiClientes } from '../../api/axios'
import { ServerTable, type ColumnDef, type PaginationState } from '../../components/ServerTable'
import { FilterPanel, type FilterItem } from '../../components/FilterPanel'
import { ConfirmDialog } from '../../components/ConfirmDialog'
import { useTriStateMultiSort } from '../../hooks/useTriStateMultiSort'
import { getColunasTitulosAReceber } from '../../hooks/useColunasTitulos'

const FILTER_OPTIONS = [
  'uuidContrato', 'empresa', 'cliente', 'numeDoc', 'sequencia', 'dtEmissao',
  'dataBase', 'vencimento', 'nomeClte', 'fatura', 'numeFatura', 'observacao',
  'nomeCcusto', 'ccusto', 'descricao',
]
const OPERATORS = ['EQUALS', 'CONTAINS', 'STARTS_WITH', 'ENDS_WITH'] as const

const ENDPOINT_PROCESSAR = '/clientes-api/v1/admin/a-receber/processamento-de-boletos'
type TipoProcessamento = 'INCLUSAO' | 'REMOCAO'

export default function TitulosAReceberPage() {
  const navigate = useNavigate()
  const { enqueueSnackbar } = useSnackbar()
  const columns = getColunasTitulosAReceber()
  const [titulos, setTitulos] = useState<Record<string, unknown>[]>([])
  const [loading, setLoading] = useState(false)
  const [pagination, setPagination] = useState<PaginationState>({ page: 1, rowsPerPage: 10, rowsNumber: 0 })
  const [selectedIds, setSelectedIds] = useState<Set<string | number>>(new Set())
  const [filtros, setFiltros] = useState<FilterItem[]>([])
  const [currentFilter, setCurrentFilter] = useState<FilterItem>({ key: '', value: '', operator: 'EQUALS' })
  const [joinOperator, setJoinOperator] = useState<'AND' | 'OR'>('AND')
  const [filtrosVisiveis, setFiltrosVisiveis] = useState(false)
  const [confirmDialog, setConfirmDialog] = useState<{ open: boolean; tipo: TipoProcessamento | null }>({ open: false, tipo: null })
  const { dirOf, cycleSort, buildSortQuery } = useTriStateMultiSort({ storageKey: 'titulos-a-receber:sort', persist: true })

  const fetchTitulos = useCallback(async (pag: PaginationState, filters: FilterItem[], join: 'AND' | 'OR') => {
    setLoading(true)
    try {
      const url = `/clientes-api/v1/admin/a-receber/search?page=${pag.page}&size=${pag.rowsPerPage}${buildSortQuery()}`
      let body: Record<string, string> = {}
      if (filters.length > 0) {
        body = filters.reduce((acc, f) => { acc[f.key] = f.value; return acc }, {} as Record<string, string>)
        body.joinFiltersOperator = join
      }
      const resp = await apiClientes.post(url, body)
      setTitulos(resp.data.content ?? [])
      setPagination(p => ({ ...p, rowsNumber: resp.data.totalElements ?? 0 }))
    } catch (error: unknown) {
      if ((error as { response?: { status: number } }).response?.status === 403) navigate('/admin')
      else enqueueSnackbar('Erro ao buscar títulos.', { variant: 'error' })
    } finally {
      setLoading(false)
    }
  }, [buildSortQuery, navigate, enqueueSnackbar])

  useEffect(() => {
    const raw = sessionStorage.getItem('titulosSelecionados')
    if (raw) setSelectedIds(new Set(JSON.parse(raw)))
  }, [])

  useEffect(() => {
    sessionStorage.setItem('titulosSelecionados', JSON.stringify(Array.from(selectedIds)))
  }, [selectedIds])

  useEffect(() => { fetchTitulos(pagination, filtros, joinOperator) }, [pagination.page, pagination.rowsPerPage])

  function handleSort(field: string) {
    cycleSort(field)
    fetchTitulos({ ...pagination, page: 1 }, filtros, joinOperator)
  }

  function handleAddFilter() {
    if (currentFilter.key && currentFilter.value) {
      setFiltros(prev => [...prev, { ...currentFilter }])
      setCurrentFilter(f => ({ ...f, value: '' }))
    }
  }

  function handleRemoveFilter(index: number) {
    setFiltros(prev => prev.filter((_, i) => i !== index))
  }

  function handleSearch() {
    fetchTitulos({ ...pagination, page: 1 }, filtros, joinOperator)
  }

  function handleClear() {
    setFiltros([])
    setCurrentFilter({ key: '', value: '', operator: 'EQUALS' })
    fetchTitulos({ ...pagination, page: 1 }, [], joinOperator)
  }

  function toggleSelect(id: unknown) {
    setSelectedIds(prev => {
      const next = new Set(prev)
      if (next.has(id as string | number)) next.delete(id as string | number)
      else next.add(id as string | number)
      return next
    })
  }

  const allPageSelected = titulos.length > 0 && titulos.every(r => selectedIds.has(r.id as string | number))
  const somePageSelected = titulos.some(r => selectedIds.has(r.id as string | number))

  function toggleSelectAllPage(val: boolean) {
    setSelectedIds(prev => {
      const next = new Set(prev)
      titulos.forEach(r => (val ? next.add(r.id as string | number) : next.delete(r.id as string | number)))
      return next
    })
  }

  async function processarBoletos(tipo: TipoProcessamento) {
    if (selectedIds.size === 0) return
    setLoading(true)
    try {
      await apiClientes.post(ENDPOINT_PROCESSAR, {
        tipoProcessamentoBoleto: tipo,
        titulosAReceberIdList: Array.from(selectedIds),
      })
      enqueueSnackbar(
        tipo === 'INCLUSAO'
          ? 'Solicitação de geração de boletos enviada. Acompanhe em Sincronizações de Boletos.'
          : 'Solicitação de exclusão de boletos enviada. Acompanhe em Sincronizações de Boletos.',
        { variant: 'info' }
      )
      setSelectedIds(new Set())
      fetchTitulos(pagination, filtros, joinOperator)
    } catch {
      enqueueSnackbar(
        tipo === 'INCLUSAO' ? 'Falha ao solicitar geração de boletos.' : 'Falha ao solicitar exclusão de boletos.',
        { variant: 'error' }
      )
    } finally {
      setLoading(false)
    }
  }

  return (
    <Box>
      <Typography variant="h4" sx={{ mb: 2 }}>Títulos a Receber</Typography>

      <Button
        variant="contained"
        startIcon={<FilterListIcon />}
        sx={{ mb: 2 }}
        onClick={() => setFiltrosVisiveis(v => !v)}
      >
        Filtros
      </Button>

      <FilterPanel
        open={filtrosVisiveis}
        filters={filtros}
        fieldOptions={FILTER_OPTIONS}
        operatorOptions={OPERATORS}
        currentFilter={currentFilter}
        onCurrentFilterChange={setCurrentFilter}
        onAddFilter={handleAddFilter}
        onRemoveFilter={handleRemoveFilter}
        joinOperator={joinOperator}
        onJoinOperatorChange={setJoinOperator}
        onSearch={handleSearch}
        onClear={handleClear}
      />

      <Stack direction="row" spacing={1} sx={{ mb: 1 }} flexWrap="wrap" alignItems="center">
        <Typography variant="caption">
          <strong>{selectedIds.size}</strong> selecionado(s) no total
        </Typography>
        <Box sx={{ flex: 1 }} />
        <Button
          variant="outlined"
          startIcon={<ClearAllIcon />}
          disabled={selectedIds.size === 0 || loading}
          onClick={() => setSelectedIds(new Set())}
        >
          Limpar seleção
        </Button>
        <Button
          variant="contained"
          color="success"
          startIcon={<ReceiptLongIcon />}
          disabled={selectedIds.size === 0 || loading}
          onClick={() => setConfirmDialog({ open: true, tipo: 'INCLUSAO' })}
        >
          Gerar Boletos
        </Button>
        <Button
          variant="contained"
          color="error"
          startIcon={<DeleteIcon />}
          disabled={selectedIds.size === 0 || loading}
          onClick={() => setConfirmDialog({ open: true, tipo: 'REMOCAO' })}
        >
          Apagar Boletos
        </Button>
      </Stack>

      <ServerTable
        rows={titulos}
        columns={columns}
        loading={loading}
        pagination={pagination}
        onPaginationChange={setPagination}
        dirOf={dirOf}
        onSort={handleSort}
        onRowClick={row => navigate(`/admin/app/a-receber/${row.id}`)}
        selection
        selectedIds={selectedIds as Set<unknown>}
        onToggleSelect={toggleSelect}
        allPageSelected={allPageSelected}
        somePageSelected={somePageSelected}
        onToggleSelectAllPage={toggleSelectAllPage}
        noDataLabel="Nenhum título encontrado."
        rowsPerPageOptions={[5, 10, 20, 50]}
      />

      <ConfirmDialog
        open={confirmDialog.open}
        title={confirmDialog.tipo === 'INCLUSAO' ? 'Gerar Boletos' : 'Apagar Boletos'}
        message={
          confirmDialog.tipo === 'INCLUSAO'
            ? `Deseja solicitar a geração de boletos para ${selectedIds.size} título(s)?`
            : 'Deseja solicitar a remoção dos boletos dos títulos selecionados? O processo será executado em segundo plano.'
        }
        confirmLabel="Solicitar"
        confirmColor={confirmDialog.tipo === 'INCLUSAO' ? 'success' : 'error'}
        onConfirm={() => {
          const tipo = confirmDialog.tipo!
          setConfirmDialog({ open: false, tipo: null })
          processarBoletos(tipo)
        }}
        onCancel={() => setConfirmDialog({ open: false, tipo: null })}
      />
    </Box>
  )
}
