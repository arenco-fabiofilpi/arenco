import { useState, useEffect, useCallback } from 'react'
import { useNavigate } from 'react-router-dom'
import { Box, Typography, Button, Stack, IconButton, Tooltip } from '@mui/material'
import RefreshIcon from '@mui/icons-material/Refresh'
import SortIcon from '@mui/icons-material/Sort'
import { useSnackbar } from 'notistack'
import { apiClientes } from '../../api/axios'
import { ServerTable, type ColumnDef, type PaginationState } from '../../components/ServerTable'
import { useTriStateMultiSort } from '../../hooks/useTriStateMultiSort'

const columns: ColumnDef[] = [
  { field: 'startedAt', headerName: 'Iniciado em' },
  { field: 'finishedAt', headerName: 'Finalizado em' },
  { field: 'type', headerName: 'Tipo' },
  { field: 'status', headerName: 'Status' },
  { field: 'message', headerName: 'Mensagem' },
]

export default function SincronizacaoBoletosPage() {
  const navigate = useNavigate()
  const { enqueueSnackbar } = useSnackbar()
  const [syncJobs, setSyncJobs] = useState<Record<string, unknown>[]>([])
  const [loading, setLoading] = useState(false)
  const [pagination, setPagination] = useState<PaginationState>({ page: 1, rowsPerPage: 10, rowsNumber: 0 })
  const { dirOf, cycleSort, clearSort, buildSortQuery, sortState } = useTriStateMultiSort({ storageKey: 'sync-boletos:sort', persist: true })

  const fetchApi = useCallback(async (pag: PaginationState) => {
    setLoading(true)
    try {
      // Note: this endpoint uses 0-based pages (Spring Data default)
      const url = `/clientes-api/v1/admin/sync/SINCRONIZAR_BOLETOS?page=${pag.page - 1}&size=${pag.rowsPerPage}${buildSortQuery()}`
      const resp = await apiClientes.get(url)
      setSyncJobs(resp.data.content ?? [])
      setPagination(p => ({ ...p, rowsNumber: resp.data.totalElements ?? 0 }))
    } catch (error: unknown) {
      if ((error as { response?: { status: number } }).response?.status === 403) navigate('/admin')
      else enqueueSnackbar('Erro ao buscar sincronizações.', { variant: 'error' })
    } finally {
      setLoading(false)
    }
  }, [buildSortQuery, navigate, enqueueSnackbar])

  useEffect(() => { fetchApi(pagination) }, [pagination.page, pagination.rowsPerPage])

  function handleSort(field: string) {
    cycleSort(field)
    fetchApi({ ...pagination, page: 1 })
  }

  function handleClearSort() {
    clearSort()
    fetchApi({ ...pagination, page: 1 })
  }

  return (
    <Box>
      <Stack direction="row" alignItems="center" sx={{ mb: 2 }}>
        <Typography variant="h4">Sincronização de Boletos</Typography>
        <Box sx={{ flex: 1 }} />
        <Button
          startIcon={<SortIcon />}
          disabled={sortState.length === 0}
          onClick={handleClearSort}
          sx={{ mr: 1 }}
        >
          Limpar ordenação
        </Button>
        <Tooltip title="Atualizar">
          <IconButton onClick={() => fetchApi(pagination)} disabled={loading}>
            <RefreshIcon />
          </IconButton>
        </Tooltip>
      </Stack>

      <ServerTable
        rows={syncJobs}
        columns={columns}
        loading={loading}
        pagination={pagination}
        onPaginationChange={setPagination}
        dirOf={dirOf}
        onSort={handleSort}
        noDataLabel="Nenhuma sincronização encontrada."
        rowsPerPageOptions={[10, 20, 50]}
      />
    </Box>
  )
}
