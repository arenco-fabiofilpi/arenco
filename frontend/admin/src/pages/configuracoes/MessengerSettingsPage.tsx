import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import {
  Box, Typography, Button, Card, CardContent, Stack, Grid,
  Select, MenuItem, FormControl, InputLabel, Chip,
  IconButton, Menu, MenuItem as MuiMenuItem, LinearProgress,
} from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import SaveIcon from '@mui/icons-material/Save'
import RefreshIcon from '@mui/icons-material/Refresh'
import MoreVertIcon from '@mui/icons-material/MoreVert'
import PersonRemoveIcon from '@mui/icons-material/PersonRemove'
import { useSnackbar } from 'notistack'
import { apiClientes } from '../../api/axios'
import { ServerTable, type ColumnDef, type PaginationState } from '../../components/ServerTable'

interface SettingsDTO {
  id: string
  version: number
  state: 'ENABLED' | 'DISABLED'
  strategy: 'EMAIL_ONLY'
  targetUsers: 'NO_USERS' | 'BETA_USERS' | 'ALL_USERS'
}

type BetaUser = Record<string, unknown>

export default function MessengerSettingsPage() {
  const navigate = useNavigate()
  const { enqueueSnackbar } = useSnackbar()
  const [settings, setSettings] = useState<SettingsDTO | null>(null)
  const [form, setForm] = useState<Partial<SettingsDTO>>({})
  const [original, setOriginal] = useState<Partial<SettingsDTO>>({})
  const [loading, setLoading] = useState(false)
  const [saving, setSaving] = useState(false)
  const [betaUsers, setBetaUsers] = useState<BetaUser[]>([])
  const [pagination, setPagination] = useState<PaginationState>({ page: 1, rowsPerPage: 10, rowsNumber: 0 })
  const [menuAnchor, setMenuAnchor] = useState<{ el: HTMLElement; row: BetaUser } | null>(null)

  const columns: ColumnDef[] = [
    { field: 'username', headerName: 'Username', sortable: false },
    { field: 'name', headerName: 'Nome', sortable: false },
    { field: 'idErp', headerName: 'ID ERP', sortable: false },
    {
      field: 'actions',
      headerName: 'Ações',
      sortable: false,
      align: 'center',
      renderCell: row => (
        <IconButton
          size="small"
          onClick={e => { e.stopPropagation(); setMenuAnchor({ el: e.currentTarget, row: row as BetaUser }) }}
        >
          <MoreVertIcon fontSize="small" />
        </IconButton>
      ),
    },
  ]

  async function loadSettings() {
    setLoading(true)
    try {
      const { data } = await apiClientes.get<SettingsDTO>('/clientes-api/v1/settings')
      setSettings(data)
      setForm({ ...data })
      setOriginal({ ...data })
    } catch {
      enqueueSnackbar('Falha ao carregar configurações.', { variant: 'error' })
    } finally {
      setLoading(false)
    }
  }

  const fetchBetaUsers = async (pag: PaginationState) => {
    setLoading(true)
    try {
      const resp = await apiClientes.get(
        `/clientes-api/v1/settings/beta-users?page=${pag.page}&size=${pag.rowsPerPage}`
      )
      setBetaUsers(resp.data.content ?? [])
      setPagination(p => ({ ...p, rowsNumber: resp.data.totalElements ?? 0 }))
    } catch (error: unknown) {
      if ((error as { response?: { status: number } }).response?.status === 403) navigate('/admin')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { loadSettings() }, [])
  useEffect(() => { fetchBetaUsers(pagination) }, [pagination.page, pagination.rowsPerPage])

  async function onSave() {
    const body: Partial<SettingsDTO> = {}
    if (form.state !== original.state) body.state = form.state
    if (form.strategy !== original.strategy) body.strategy = form.strategy
    if (form.targetUsers !== original.targetUsers) body.targetUsers = form.targetUsers
    if (Object.keys(body).length === 0) {
      enqueueSnackbar('Nenhuma alteração para salvar.', { variant: 'info' })
      return
    }
    setSaving(true)
    try {
      await apiClientes.patch('/clientes-api/v1/settings', body)
      enqueueSnackbar('Configurações salvas.', { variant: 'success' })
      await loadSettings()
    } catch {
      enqueueSnackbar('Erro ao salvar configurações.', { variant: 'error' })
    } finally {
      setSaving(false)
    }
  }

  async function removerBetaUser(cliente: BetaUser) {
    try {
      await apiClientes.delete(`/clientes-api/v1/settings/beta-users?userModelId=${cliente.id}`)
      enqueueSnackbar(`Cliente ${cliente.name} removido da lista de Beta Users.`, { variant: 'success' })
    } catch {
      enqueueSnackbar('Erro ao remover cliente da lista de Beta Users.', { variant: 'error' })
    } finally {
      setMenuAnchor(null)
      fetchBetaUsers(pagination)
    }
  }

  return (
    <Box>
      <Stack direction="row" alignItems="center" sx={{ mb: 2 }} spacing={1}>
        <Button startIcon={<ArrowBackIcon />} onClick={() => navigate(-1)}>Voltar</Button>
        <Box sx={{ flex: 1 }} />
        <Button startIcon={<RefreshIcon />} onClick={loadSettings} disabled={loading}>Atualizar</Button>
        <Button variant="contained" startIcon={<SaveIcon />} onClick={onSave} disabled={saving || loading} loading={saving}>
          Salvar alterações
        </Button>
      </Stack>

      <Stack direction="row" alignItems="center" sx={{ mb: 2 }} spacing={1}>
        <Typography variant="h5">Configurações de Disparo de Cobranças</Typography>
        <Box sx={{ flex: 1 }} />
        {settings?.id && <Chip label={`ID: ${settings.id}`} variant="outlined" color="primary" size="small" />}
        {settings?.version !== undefined && <Chip label={`v${settings.version}`} variant="outlined" color="secondary" size="small" />}
      </Stack>

      <Card variant="outlined" sx={{ mb: 3 }}>
        {loading && <LinearProgress />}
        {settings && (
          <CardContent>
            <Grid container spacing={2}>
              <Grid item xs={12} sm={4}>
                <FormControl fullWidth size="small">
                  <InputLabel>Estado</InputLabel>
                  <Select
                    value={form.state ?? ''}
                    label="Estado"
                    onChange={e => setForm(f => ({ ...f, state: e.target.value as SettingsDTO['state'] }))}
                  >
                    <MuiMenuItem value="ENABLED">Habilitado</MuiMenuItem>
                    <MuiMenuItem value="DISABLED">Desabilitado</MuiMenuItem>
                  </Select>
                </FormControl>
              </Grid>
              <Grid item xs={12} sm={4}>
                <FormControl fullWidth size="small">
                  <InputLabel>Estratégia de envio</InputLabel>
                  <Select
                    value={form.strategy ?? ''}
                    label="Estratégia de envio"
                    onChange={e => setForm(f => ({ ...f, strategy: e.target.value as SettingsDTO['strategy'] }))}
                  >
                    <MuiMenuItem value="EMAIL_ONLY">Somente E-mail</MuiMenuItem>
                  </Select>
                </FormControl>
              </Grid>
              <Grid item xs={12} sm={4}>
                <FormControl fullWidth size="small">
                  <InputLabel>Usuários Alvos</InputLabel>
                  <Select
                    value={form.targetUsers ?? ''}
                    label="Usuários Alvos"
                    onChange={e => setForm(f => ({ ...f, targetUsers: e.target.value as SettingsDTO['targetUsers'] }))}
                  >
                    <MuiMenuItem value="NO_USERS">Nenhum Usuário</MuiMenuItem>
                    <MuiMenuItem value="BETA_USERS">Somente Beta Users</MuiMenuItem>
                    <MuiMenuItem value="ALL_USERS">Todos os Usuários</MuiMenuItem>
                  </Select>
                </FormControl>
              </Grid>
            </Grid>
          </CardContent>
        )}
      </Card>

      <ServerTable
        rows={betaUsers}
        columns={columns}
        loading={loading}
        pagination={pagination}
        onPaginationChange={setPagination}
        noDataLabel="Nenhum usuário Beta encontrado."
      />

      <Menu anchorEl={menuAnchor?.el} open={!!menuAnchor} onClose={() => setMenuAnchor(null)}>
        <MuiMenuItem onClick={() => menuAnchor && removerBetaUser(menuAnchor.row)}>
          <PersonRemoveIcon fontSize="small" sx={{ mr: 1, color: 'primary.main' }} />
          Remover da lista de Beta Users
        </MuiMenuItem>
      </Menu>
    </Box>
  )
}
