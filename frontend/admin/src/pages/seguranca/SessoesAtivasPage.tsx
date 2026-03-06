import { useState, useEffect, useCallback } from 'react'
import { useNavigate } from 'react-router-dom'
import { Box, Typography, Button, IconButton, Menu, MenuItem } from '@mui/material'
import FilterListIcon from '@mui/icons-material/FilterList'
import MoreVertIcon from '@mui/icons-material/MoreVert'
import DeleteIcon from '@mui/icons-material/Delete'
import { useSnackbar } from 'notistack'
import { apiClientes } from '../../api/axios'
import { ServerTable, type ColumnDef, type PaginationState } from '../../components/ServerTable'
import { useTriStateMultiSort } from '../../hooks/useTriStateMultiSort'

type Sessao = Record<string, unknown>

export default function SessoesAtivasPage() {
  const navigate = useNavigate()
  const { enqueueSnackbar } = useSnackbar()
  const [sessoes, setSessoes] = useState<Sessao[]>([])
  const [loading, setLoading] = useState(false)
  const [pagination, setPagination] = useState<PaginationState>({ page: 1, rowsPerPage: 10, rowsNumber: 0 })
  const [menuAnchor, setMenuAnchor] = useState<{ el: HTMLElement; row: Sessao } | null>(null)
  const { dirOf, cycleSort, buildSortQuery } = useTriStateMultiSort({ storageKey: 'active-sessions:list:sort', persist: true })

  const columns: ColumnDef[] = [
    { field: 'username', headerName: 'Username' },
    { field: 'ip', headerName: 'IP' },
    { field: 'userAgent', headerName: 'User Agent' },
    { field: 'createdAt', headerName: 'Data de Criação' },
    { field: 'expiresAt', headerName: 'Data de Expiração' },
    {
      field: 'actions',
      headerName: 'Ações',
      sortable: false,
      align: 'center',
      renderCell: row => (
        <IconButton
          size="small"
          onClick={e => { e.stopPropagation(); setMenuAnchor({ el: e.currentTarget, row: row as Sessao }) }}
        >
          <MoreVertIcon fontSize="small" />
        </IconButton>
      ),
    },
  ]

  const fetchSessoes = useCallback(async (pag: PaginationState) => {
    setLoading(true)
    try {
      const url = `/clientes-api/v1/auth-manager/active-sessions?page=${pag.page}&size=${pag.rowsPerPage}${buildSortQuery()}`
      const resp = await apiClientes.get(url)
      setSessoes(resp.data.content ?? [])
      setPagination(p => ({ ...p, rowsNumber: resp.data.totalElements ?? 0 }))
    } catch (error: unknown) {
      if ((error as { response?: { status: number } }).response?.status === 403) navigate('/admin')
      else enqueueSnackbar('Erro ao buscar sessões.', { variant: 'error' })
    } finally {
      setLoading(false)
    }
  }, [buildSortQuery, navigate, enqueueSnackbar])

  useEffect(() => { fetchSessoes(pagination) }, [pagination.page, pagination.rowsPerPage])

  function handleSort(field: string) {
    cycleSort(field)
    fetchSessoes({ ...pagination, page: 1 })
  }

  async function removerSessao(sessao: Sessao) {
    try {
      const resp = await apiClientes.delete(`/clientes-api/v1/auth-manager/active-sessions/${sessao.id}`)
      if (resp.status === 201)
        enqueueSnackbar(`Sessão ${sessao.id} removida com sucesso.`, { variant: 'success' })
    } catch {
      enqueueSnackbar('Erro ao remover sessão.', { variant: 'error' })
    } finally {
      setMenuAnchor(null)
      fetchSessoes(pagination)
    }
  }

  return (
    <Box>
      <Typography variant="h4" sx={{ mb: 2 }}>Sessões Ativas</Typography>
      <Button variant="contained" startIcon={<FilterListIcon />} sx={{ mb: 2 }}>Filtros</Button>

      <ServerTable
        rows={sessoes}
        columns={columns}
        loading={loading}
        pagination={pagination}
        onPaginationChange={setPagination}
        dirOf={dirOf}
        onSort={handleSort}
        noDataLabel="Nenhuma sessão encontrada."
        rowsPerPageOptions={[10, 20, 50]}
      />

      <Menu anchorEl={menuAnchor?.el} open={!!menuAnchor} onClose={() => setMenuAnchor(null)}>
        <MenuItem onClick={() => menuAnchor && removerSessao(menuAnchor.row)}>
          <DeleteIcon fontSize="small" sx={{ mr: 1, color: 'primary.main' }} />
          Remover Sessão
        </MenuItem>
      </Menu>
    </Box>
  )
}
