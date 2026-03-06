import { useState, useEffect, useCallback } from 'react'
import { useNavigate } from 'react-router-dom'
import { Box, Typography, Button } from '@mui/material'
import FilterListIcon from '@mui/icons-material/FilterList'
import { useSnackbar } from 'notistack'
import { apiClientes } from '../../api/axios'
import { ServerTable, type ColumnDef, type PaginationState } from '../../components/ServerTable'
import { FilterPanel, type FilterItem } from '../../components/FilterPanel'
import { useTriStateMultiSort } from '../../hooks/useTriStateMultiSort'
import { getColunasTitulosRecebidos } from '../../hooks/useColunasTitulos'

const FILTER_OPTIONS = [
  'uuidContrato', 'empresa', 'cliente', 'numeDoc', 'sequencia', 'dtEmissao',
  'dataBase', 'vencimento', 'nomeClte', 'fatura', 'numeFatura', 'observacao',
  'nomeCcusto', 'ccusto', 'descricao', 'lote', 'serie', 'dtDeposito',
]
const OPERATORS = ['EQUALS', 'CONTAINS', 'STARTS_WITH', 'ENDS_WITH'] as const

export default function TitulosRecebidosPage() {
  const navigate = useNavigate()
  const { enqueueSnackbar } = useSnackbar()
  const columns = getColunasTitulosRecebidos()
  const [titulos, setTitulos] = useState<Record<string, unknown>[]>([])
  const [loading, setLoading] = useState(false)
  const [pagination, setPagination] = useState<PaginationState>({ page: 1, rowsPerPage: 10, rowsNumber: 0 })
  const [filtros, setFiltros] = useState<FilterItem[]>([])
  const [currentFilter, setCurrentFilter] = useState<FilterItem>({ key: '', value: '', operator: 'EQUALS' })
  const [joinOperator, setJoinOperator] = useState<'AND' | 'OR'>('AND')
  const [filtrosVisiveis, setFiltrosVisiveis] = useState(false)
  const { dirOf, cycleSort, buildSortQuery } = useTriStateMultiSort({ storageKey: 'titulos-recebidos:list:sort', persist: true })

  const fetchTitulos = useCallback(async (pag: PaginationState, filters: FilterItem[], join: 'AND' | 'OR') => {
    setLoading(true)
    try {
      const url = `/clientes-api/v1/admin/recebidos/search?page=${pag.page}&size=${pag.rowsPerPage}${buildSortQuery()}`
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
      else enqueueSnackbar('Erro ao buscar títulos recebidos.', { variant: 'error' })
    } finally {
      setLoading(false)
    }
  }, [buildSortQuery, navigate, enqueueSnackbar])

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

  return (
    <Box>
      <Typography variant="h4" sx={{ mb: 2 }}>Títulos Recebidos</Typography>

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

      <ServerTable
        rows={titulos}
        columns={columns}
        loading={loading}
        pagination={pagination}
        onPaginationChange={setPagination}
        dirOf={dirOf}
        onSort={handleSort}
        onRowClick={row => navigate(`/admin/app/recebidos/${row.id}`)}
        noDataLabel="Nenhum título encontrado."
        rowsPerPageOptions={[10, 20, 50]}
      />
    </Box>
  )
}
