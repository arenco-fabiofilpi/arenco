import { useState, useEffect, useCallback } from 'react'
import { useNavigate } from 'react-router-dom'
import { Box, Typography, Button, Stack, IconButton, Tooltip } from '@mui/material'
import RefreshIcon from '@mui/icons-material/Refresh'
import SyncIcon from '@mui/icons-material/Sync'
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

export default function SincronizacaoBasesPage() {
  const navigate = useNavigate()
  const { enqueueSnackbar } = useSnackbar()
  const [syncJobs, setSyncJobs] = useState<Record<string, unknown>[]>([])
  const [loading, setLoading] = useState(false)
  const [pagination, setPagination] = useState<PaginationState>({ page: 1, rowsPerPage: 10, rowsNumber: 0 })
  const { dirOf, cycleSort, buildSortQuery } = useTriStateMultiSort({ storageKey: 'sync-bases:list:sort', persist: true })

  const fetchApi = useCallback(async (pag: PaginationState) => {
    setLoading(true)
    try {
      const url = `/clientes-api/v1/admin/sync/SINCRONIZAR_BASES?page=${pag.page}&size=${pag.rowsPerPage}${buildSortQuery()}`
      const resp = await apiClientes.get(url)
      setSyncJobs(resp.data.content ?? [])
      setPagination(p => ({ ...p, rowsNumber: resp.data.totalElements ?? 0 }))
    } catch (error: unknown) {
      if ((error as { response?: { status: number } }).response?.status === 403) navigate('/admin')
    } finally {
      setLoading(false)
    }
  }, [buildSortQuery, navigate])

  useEffect(() => { fetchApi(pagination) }, [pagination.page, pagination.rowsPerPage])

  function handleSort(field: string) {
    cycleSort(field)
    fetchApi({ ...pagination, page: 1 })
  }

  async function triggerSync() {
    try {
      const resp = await apiClientes.post('/clientes-api/v1/admin/sync/SINCRONIZAR_BASES', {})
      if (resp.status === 201) enqueueSnackbar('Sincronização solicitada com sucesso.', { variant: 'success' })
    } catch {
      enqueueSnackbar('Erro ao solicitar sincronização.', { variant: 'error' })
    } finally {
      fetchApi(pagination)
    }
  }

  return (
    <Box>
      <Stack direction="row" alignItems="center" sx={{ mb: 2 }}>
        <Typography variant="h4">Sincronização de Bases de Dados</Typography>
        <Box sx={{ flex: 1 }} />
        <Tooltip title="Atualizar">
          <IconButton onClick={() => fetchApi(pagination)} disabled={loading}>
            <RefreshIcon />
          </IconButton>
        </Tooltip>
        <Button variant="contained" startIcon={<SyncIcon />} onClick={triggerSync} disabled={loading}>
          Sincronizar
        </Button>
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
