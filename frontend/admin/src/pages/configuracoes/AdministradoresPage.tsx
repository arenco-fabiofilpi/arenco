import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { Box, Typography, IconButton, Menu, MenuItem } from '@mui/material'
import MoreVertIcon from '@mui/icons-material/MoreVert'
import DeleteIcon from '@mui/icons-material/Delete'
import { useSnackbar } from 'notistack'
import { apiClientes } from '../../api/axios'
import { ServerTable, type ColumnDef, type PaginationState } from '../../components/ServerTable'

type Usuario = Record<string, unknown>

export default function AdministradoresPage() {
  const navigate = useNavigate()
  const { enqueueSnackbar } = useSnackbar()
  const [usuarios, setUsuarios] = useState<Usuario[]>([])
  const [loading, setLoading] = useState(false)
  const [pagination, setPagination] = useState<PaginationState>({ page: 1, rowsPerPage: 10, rowsNumber: 0 })
  const [menuAnchor, setMenuAnchor] = useState<{ el: HTMLElement; row: Usuario } | null>(null)

  const columns: ColumnDef[] = [
    { field: 'name', headerName: 'Nome', sortable: false },
    { field: 'username', headerName: 'Username', sortable: false },
    { field: 'loginMethod', headerName: 'Método de Login', sortable: false },
    { field: 'enabled', headerName: 'Habilitado', sortable: false, renderCell: row => row.enabled ? 'Sim' : 'Não' },
    {
      field: 'actions',
      headerName: 'Ações',
      sortable: false,
      align: 'center',
      renderCell: row => (
        <IconButton
          size="small"
          onClick={e => { e.stopPropagation(); setMenuAnchor({ el: e.currentTarget, row: row as Usuario }) }}
        >
          <MoreVertIcon fontSize="small" />
        </IconButton>
      ),
    },
  ]

  const fetchUsuarios = async (pag: PaginationState) => {
    setLoading(true)
    try {
      const resp = await apiClientes.get(
        `/clientes-api/v1/admin-management/users?page=${pag.page}&size=${pag.rowsPerPage}`
      )
      setUsuarios(resp.data.content ?? [])
      setPagination(p => ({ ...p, rowsNumber: resp.data.totalElements ?? 0 }))
    } catch (error: unknown) {
      if ((error as { response?: { status: number } }).response?.status === 403) navigate('/admin')
      else enqueueSnackbar('Erro ao buscar administradores.', { variant: 'error' })
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { fetchUsuarios(pagination) }, [pagination.page, pagination.rowsPerPage])

  async function removerUsuario(usuario: Usuario) {
    try {
      const resp = await apiClientes.delete(`/clientes-api/v1/admin-management/users/${usuario.id}`)
      if (resp.status === 202)
        enqueueSnackbar(`Usuário ${usuario.username} removido com sucesso.`, { variant: 'success' })
    } catch {
      enqueueSnackbar('Erro ao remover usuário.', { variant: 'error' })
    } finally {
      setMenuAnchor(null)
      fetchUsuarios(pagination)
    }
  }

  return (
    <Box>
      <Typography variant="h4" sx={{ mb: 2 }}>Administradores</Typography>

      <ServerTable
        rows={usuarios}
        columns={columns}
        loading={loading}
        pagination={pagination}
        onPaginationChange={setPagination}
        noDataLabel="Nenhum usuário encontrado."
      />

      <Menu anchorEl={menuAnchor?.el} open={!!menuAnchor} onClose={() => setMenuAnchor(null)}>
        <MenuItem onClick={() => menuAnchor && removerUsuario(menuAnchor.row)}>
          <DeleteIcon fontSize="small" sx={{ mr: 1, color: 'primary.main' }} />
          Remover Usuário
        </MenuItem>
      </Menu>
    </Box>
  )
}
