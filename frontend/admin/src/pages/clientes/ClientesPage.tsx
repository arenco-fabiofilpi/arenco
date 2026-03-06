import { useState, useEffect, useCallback } from 'react'
import { useNavigate } from 'react-router-dom'
import { Box, Typography, Button, Stack } from '@mui/material'
import FilterListIcon from '@mui/icons-material/FilterList'
import DoneAllIcon from '@mui/icons-material/DoneAll'
import ClearIcon from '@mui/icons-material/Clear'
import DownloadIcon from '@mui/icons-material/Download'
import MoreVertIcon from '@mui/icons-material/MoreVert'
import PersonAddIcon from '@mui/icons-material/PersonAdd'
import { Menu, MenuItem, IconButton } from '@mui/material'
import { useSnackbar } from 'notistack'
import { apiClientes } from '../../api/axios'
import { ServerTable, type ColumnDef, type PaginationState } from '../../components/ServerTable'
import { useTriStateMultiSort } from '../../hooks/useTriStateMultiSort'

type ClienteRow = {
  id: string | number
  idErp?: string
  name?: string
  username?: string
  grupoContabil?: string
  loginMethod?: string
  enabled?: boolean
}

const OPERATORS = ['EQUALS', 'CONTAINS', 'STARTS_WITH', 'ENDS_WITH'] as const

export default function ClientesPage() {
  const navigate = useNavigate()
  const { enqueueSnackbar } = useSnackbar()
  const [clientes, setClientes] = useState<ClienteRow[]>([])
  const [loading, setLoading] = useState(false)
  const [pagination, setPagination] = useState<PaginationState>({ page: 1, rowsPerPage: 10, rowsNumber: 0 })
  const [selectedIds, setSelectedIds] = useState<Set<string | number>>(new Set())
  const [menuAnchor, setMenuAnchor] = useState<{ el: HTMLElement; row: ClienteRow } | null>(null)
  const { dirOf, cycleSort, buildSortQuery } = useTriStateMultiSort({
    storageKey: 'clientes:list:sort',
    persist: true,
  })

  const columns: ColumnDef[] = [
    { field: 'idErp', headerName: 'ID Eficaz' },
    { field: 'name', headerName: 'Nome' },
    { field: 'username', headerName: 'CPF/CNPJ' },
    { field: 'grupoContabil', headerName: 'Grupo Contábil' },
    { field: 'loginMethod', headerName: 'Método de Login' },
    {
      field: 'enabled',
      headerName: 'Habilitado',
      renderCell: row => (row.enabled ? 'Sim' : 'Não'),
    },
    {
      field: 'actions',
      headerName: 'Ações',
      sortable: false,
      align: 'center',
      renderCell: row => (
        <IconButton
          size="small"
          onClick={e => {
            e.stopPropagation()
            setMenuAnchor({ el: e.currentTarget, row: row as ClienteRow })
          }}
        >
          <MoreVertIcon fontSize="small" />
        </IconButton>
      ),
    },
  ]

  const fetchClientes = useCallback(
    async (pag: PaginationState) => {
      setLoading(true)
      try {
        const url = `/clientes-api/v1/customers-management/search?page=${pag.page}&size=${pag.rowsPerPage}${buildSortQuery()}`
        const resp = await apiClientes.post(url, {}, { headers: { 'Content-Type': 'application/json' } })
        setClientes(resp.data.content ?? [])
        setPagination(p => ({ ...p, rowsNumber: resp.data.totalElements ?? 0 }))
      } catch (error: unknown) {
        if ((error as { response?: { status: number } }).response?.status === 403) navigate('/admin')
        else enqueueSnackbar('Erro ao buscar clientes.', { variant: 'error' })
      } finally {
        setLoading(false)
      }
    },
    [buildSortQuery, navigate, enqueueSnackbar],
  )

  useEffect(() => {
    fetchClientes(pagination)
  }, [pagination.page, pagination.rowsPerPage])

  function handleSort(field: string) {
    cycleSort(field)
    fetchClientes({ ...pagination, page: 1 })
  }

  function handlePaginationChange(p: PaginationState) {
    setPagination(p)
  }

  function toggleSelect(id: unknown) {
    setSelectedIds(prev => {
      const next = new Set(prev)
      if (next.has(id as string | number)) next.delete(id as string | number)
      else next.add(id as string | number)
      return next
    })
  }

  const allPageSelected = clientes.length > 0 && clientes.every(r => selectedIds.has(r.id))
  const somePageSelected = clientes.some(r => selectedIds.has(r.id))

  function toggleSelectAllPage(val: boolean) {
    setSelectedIds(prev => {
      const next = new Set(prev)
      clientes.forEach(r => (val ? next.add(r.id) : next.delete(r.id)))
      return next
    })
  }

  function downloadBlob(response: { data: BlobPart; headers: Record<string, string> }, fallbackName: string) {
    let nomeArquivo = fallbackName
    const disposition = response.headers['content-disposition']
    if (disposition) {
      const match = disposition.match(/filename="?(.+?)"?$/)
      if (match) nomeArquivo = match[1]!
    }
    const blob = new Blob([response.data], {
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
    })
    const link = document.createElement('a')
    link.href = URL.createObjectURL(blob)
    link.download = nomeArquivo
    link.click()
    URL.revokeObjectURL(link.href)
  }

  async function exportarSelecionados() {
    if (!selectedIds.size) return
    try {
      const resp = await apiClientes.post('/clientes-api/v1/exportar', Array.from(selectedIds), {
        responseType: 'blob',
      })
      downloadBlob(resp as { data: BlobPart; headers: Record<string, string> }, 'Clientes_Exportados.xlsx')
      enqueueSnackbar(`${selectedIds.size} clientes exportados.`, { variant: 'success' })
    } catch {
      enqueueSnackbar('Erro ao exportar.', { variant: 'error' })
    }
  }

  async function adicionarBetaUser(cliente: ClienteRow) {
    try {
      const resp = await apiClientes.post(`/clientes-api/v1/settings/beta-users?userModelId=${cliente.id}`, {})
      if (resp.status === 201)
        enqueueSnackbar(`Cliente ${cliente.name ?? ''} adicionado aos Beta Users.`, { variant: 'success' })
      else if (resp.status === 208)
        enqueueSnackbar(`Cliente ${cliente.name ?? ''} já existente na lista de Beta Users.`, { variant: 'warning' })
    } catch {
      enqueueSnackbar('Erro ao adicionar cliente aos Beta Users.', { variant: 'error' })
    }
    setMenuAnchor(null)
  }

  return (
    <Box>
      <Typography variant="h4" sx={{ mb: 2 }}>
        Clientes
      </Typography>

      <Stack direction="row" spacing={1} sx={{ mb: 2 }} flexWrap="wrap">
        <Button variant="contained" startIcon={<FilterListIcon />}>
          Filtros
        </Button>
        <Box sx={{ flex: 1 }} />
        <Button
          variant="outlined"
          color="error"
          startIcon={<ClearIcon />}
          disabled={selectedIds.size === 0}
          onClick={() => setSelectedIds(new Set())}
        >
          Limpar seleção
        </Button>
        <Button
          variant="outlined"
          startIcon={<DoneAllIcon />}
          disabled={clientes.length === 0}
          onClick={() => toggleSelectAllPage(!allPageSelected)}
        >
          {allPageSelected ? 'Desmarcar todos (página)' : 'Selecionar todos (página)'}
        </Button>
        <Button
          variant="contained"
          color="success"
          startIcon={<DownloadIcon />}
          disabled={selectedIds.size === 0}
          onClick={exportarSelecionados}
        >
          Exportar
        </Button>
      </Stack>

      <ServerTable
        rows={clientes as Record<string, unknown>[]}
        columns={columns}
        loading={loading}
        pagination={pagination}
        onPaginationChange={handlePaginationChange}
        dirOf={dirOf}
        onSort={handleSort}
        selection
        selectedIds={selectedIds as Set<unknown>}
        onToggleSelect={toggleSelect}
        allPageSelected={allPageSelected}
        somePageSelected={somePageSelected}
        onToggleSelectAllPage={toggleSelectAllPage}
        noDataLabel="Nenhum cliente encontrado."
        rowsPerPageOptions={[5, 10, 20, 50, 100]}
      />

      <Menu
        anchorEl={menuAnchor?.el}
        open={!!menuAnchor}
        onClose={() => setMenuAnchor(null)}
      >
        <MenuItem onClick={() => menuAnchor && adicionarBetaUser(menuAnchor.row)}>
          <PersonAddIcon fontSize="small" sx={{ mr: 1, color: 'primary.main' }} />
          Adicionar à lista de Beta Users
        </MenuItem>
      </Menu>
    </Box>
  )
}
