import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import {
  Box, Typography, Button, Dialog, DialogTitle, DialogContent, DialogActions,
  Grid, TextField, Select, MenuItem, FormControl, InputLabel, FormHelperText, Stack,
} from '@mui/material'
import AddIcon from '@mui/icons-material/Add'
import { useSnackbar } from 'notistack'
import { apiClientes } from '../../api/axios'
import { ServerTable, type ColumnDef, type PaginationState } from '../../components/ServerTable'

const UFS = new Set([
  'AC','AL','AP','AM','BA','CE','DF','ES','GO','MA','MT','MS','MG',
  'PA','PB','PR','PE','PI','RJ','RN','RS','RO','RR','SC','SP','SE','TO',
])

type FormState = {
  banco: string
  nomeBeneficiario: string
  documento: string
  agencia: string
  carteira: string
  codigoBeneficiario: string
  digitoCodigoBeneficiario: string
  localDePagamento: string
  instrucoes: string
  logradouro: string
  bairro: string
  cep: string
  cidade: string
  uf: string
  especieDocumento: string
}

const emptyForm = (): FormState => ({
  banco: '',
  nomeBeneficiario: '',
  documento: '',
  agencia: '',
  carteira: '',
  codigoBeneficiario: '',
  digitoCodigoBeneficiario: '',
  localDePagamento: '',
  instrucoes: '',
  logradouro: '',
  bairro: '',
  cep: '',
  cidade: '',
  uf: '',
  especieDocumento: '',
})

const columns: ColumnDef[] = [
  { field: 'banco', headerName: 'Banco', sortable: false },
  { field: 'nomeBeneficiario', headerName: 'Beneficiário', sortable: false },
  { field: 'documento', headerName: 'Documento', sortable: false },
]

export default function ConfiguracoesBoletosPage() {
  const navigate = useNavigate()
  const { enqueueSnackbar } = useSnackbar()
  const [configs, setConfigs] = useState<Record<string, unknown>[]>([])
  const [loading, setLoading] = useState(false)
  const [pagination, setPagination] = useState<PaginationState>({ page: 1, rowsPerPage: 10, rowsNumber: 0 })
  const [dialogOpen, setDialogOpen] = useState(false)
  const [saving, setSaving] = useState(false)
  const [form, setForm] = useState<FormState>(emptyForm())
  const [fieldErr, setFieldErr] = useState<Record<string, string>>({})

  const fetchConfigs = async (pag: PaginationState) => {
    setLoading(true)
    try {
      const resp = await apiClientes.get(
        `/clientes-api/v1/admin/payment-slip-settings?page=${pag.page}&size=${pag.rowsPerPage}`
      )
      setConfigs(resp.data.content ?? [])
      setPagination(p => ({ ...p, rowsNumber: resp.data.totalElements ?? 0 }))
    } catch (error: unknown) {
      if ((error as { response?: { status: number } }).response?.status === 403) navigate('/admin')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { fetchConfigs(pagination) }, [pagination.page, pagination.rowsPerPage])

  function setField(key: keyof FormState, value: string) {
    setForm(f => ({ ...f, [key]: value }))
  }

  function validate(): boolean {
    const errs: Record<string, string> = {}
    if (!form.banco) errs.banco = 'Banco é obrigatório'
    if (!form.nomeBeneficiario) errs.nomeBeneficiario = 'Nome do beneficiário é obrigatório'
    if (!form.documento) errs.documento = 'Documento é obrigatório'
    if (!form.agencia) errs.agencia = 'Agência é obrigatória'
    if (!form.carteira) errs.carteira = 'Carteira é obrigatória'
    if (!form.codigoBeneficiario) errs.codigoBeneficiario = 'Código do beneficiário é obrigatório'
    if (!form.digitoCodigoBeneficiario) errs.digitoCodigoBeneficiario = 'Dígito é obrigatório'
    if (!form.logradouro) errs.logradouro = 'Logradouro é obrigatório'
    if (!form.bairro) errs.bairro = 'Bairro é obrigatório'
    if (!form.cep) errs.cep = 'CEP é obrigatório'
    if (!form.cidade) errs.cidade = 'Cidade é obrigatória'
    if (!form.uf || !UFS.has(form.uf.toUpperCase())) errs.uf = 'UF inválida'
    if (!form.especieDocumento) errs.especieDocumento = 'Espécie do documento é obrigatória'
    setFieldErr(errs)
    return Object.keys(errs).length === 0
  }

  async function onSubmit() {
    if (!validate()) return
    setSaving(true)
    try {
      await apiClientes.post('/clientes-api/v1/admin/payment-slip-settings', { ...form, banco: form.banco || null })
      enqueueSnackbar('Configuração salva com sucesso!', { variant: 'success' })
      setDialogOpen(false)
      fetchConfigs(pagination)
    } catch (error: unknown) {
      const e = error as { response?: { status: number; data?: { message?: string; errors?: Record<string, string[]> } } }
      if (e.response?.status === 403) { navigate('/admin'); return }
      if (e.response?.status === 400 && e.response.data?.errors) {
        const errs: Record<string, string> = {}
        Object.entries(e.response.data.errors).forEach(([field, msgs]) => {
          errs[field] = Array.isArray(msgs) ? msgs[0] : String(msgs)
        })
        setFieldErr(errs)
        enqueueSnackbar(e.response.data.message ?? 'Erro de validação', { variant: 'error' })
      } else {
        enqueueSnackbar(e.response?.data?.message ?? 'Não foi possível salvar.', { variant: 'error' })
      }
    } finally {
      setSaving(false)
    }
  }

  return (
    <Box>
      <Stack direction="row" alignItems="center" sx={{ mb: 2 }}>
        <Typography variant="h4">Configurações para Geração de Boletos</Typography>
        <Box sx={{ flex: 1 }} />
        <Button variant="contained" startIcon={<AddIcon />} onClick={() => { setForm(emptyForm()); setFieldErr({}); setDialogOpen(true) }}>
          Adicionar
        </Button>
      </Stack>

      <ServerTable
        rows={configs}
        columns={columns}
        loading={loading}
        pagination={pagination}
        onPaginationChange={setPagination}
        onRowClick={row => navigate(`/admin/app/config-boletos/${row.id}`)}
        noDataLabel="Nenhuma configuração encontrada."
      />

      <Dialog open={dialogOpen} onClose={() => setDialogOpen(false)} maxWidth="md" fullWidth>
        <DialogTitle>Nova Configuração de Boleto</DialogTitle>
        <DialogContent>
          <Grid container spacing={2} sx={{ mt: 0.5 }}>
            <Grid item xs={12} md={6}>
              <FormControl fullWidth size="small" error={!!fieldErr.banco}>
                <InputLabel>Banco *</InputLabel>
                <Select value={form.banco} label="Banco *" onChange={e => setField('banco', e.target.value)}>
                  <MenuItem value="BRADESCO">Bradesco</MenuItem>
                </Select>
                {fieldErr.banco && <FormHelperText>{fieldErr.banco}</FormHelperText>}
              </FormControl>
            </Grid>
            <Grid item xs={12} md={6}>
              <TextField fullWidth size="small" label="Nome do Beneficiário *" value={form.nomeBeneficiario}
                onChange={e => setField('nomeBeneficiario', e.target.value)}
                error={!!fieldErr.nomeBeneficiario} helperText={fieldErr.nomeBeneficiario} />
            </Grid>
            <Grid item xs={12} md={6}>
              <TextField fullWidth size="small" label="Documento (CNPJ) *" value={form.documento}
                onChange={e => setField('documento', e.target.value)}
                error={!!fieldErr.documento} helperText={fieldErr.documento} />
            </Grid>
            <Grid item xs={6} md={3}>
              <TextField fullWidth size="small" label="Agência *" value={form.agencia}
                onChange={e => setField('agencia', e.target.value)}
                error={!!fieldErr.agencia} helperText={fieldErr.agencia} />
            </Grid>
            <Grid item xs={6} md={3}>
              <TextField fullWidth size="small" label="Carteira *" value={form.carteira}
                onChange={e => setField('carteira', e.target.value)}
                error={!!fieldErr.carteira} helperText={fieldErr.carteira} />
            </Grid>
            <Grid item xs={6} md={4}>
              <TextField fullWidth size="small" label="Código do Beneficiário *" value={form.codigoBeneficiario}
                onChange={e => setField('codigoBeneficiario', e.target.value)}
                error={!!fieldErr.codigoBeneficiario} helperText={fieldErr.codigoBeneficiario} />
            </Grid>
            <Grid item xs={6} md={2}>
              <TextField fullWidth size="small" label="Dígito *" value={form.digitoCodigoBeneficiario}
                inputProps={{ maxLength: 1 }}
                onChange={e => setField('digitoCodigoBeneficiario', e.target.value)}
                error={!!fieldErr.digitoCodigoBeneficiario} helperText={fieldErr.digitoCodigoBeneficiario} />
            </Grid>
            <Grid item xs={12} md={6}>
              <TextField fullWidth size="small" label="Local de Pagamento" value={form.localDePagamento}
                onChange={e => setField('localDePagamento', e.target.value)}
                error={!!fieldErr.localDePagamento} helperText={fieldErr.localDePagamento} />
            </Grid>
            <Grid item xs={12} md={6}>
              <TextField fullWidth size="small" label="Instruções" multiline rows={2} value={form.instrucoes}
                onChange={e => setField('instrucoes', e.target.value)}
                error={!!fieldErr.instrucoes} helperText={fieldErr.instrucoes} />
            </Grid>
            <Grid item xs={6} md={3}>
              <TextField fullWidth size="small" label="Logradouro *" value={form.logradouro}
                onChange={e => setField('logradouro', e.target.value)}
                error={!!fieldErr.logradouro} helperText={fieldErr.logradouro} />
            </Grid>
            <Grid item xs={6} md={3}>
              <TextField fullWidth size="small" label="Bairro *" value={form.bairro}
                onChange={e => setField('bairro', e.target.value)}
                error={!!fieldErr.bairro} helperText={fieldErr.bairro} />
            </Grid>
            <Grid item xs={6} md={3}>
              <TextField fullWidth size="small" label="CEP *" value={form.cep}
                onChange={e => setField('cep', e.target.value)}
                error={!!fieldErr.cep} helperText={fieldErr.cep} />
            </Grid>
            <Grid item xs={12} md={6}>
              <TextField fullWidth size="small" label="Cidade *" value={form.cidade}
                onChange={e => setField('cidade', e.target.value)}
                error={!!fieldErr.cidade} helperText={fieldErr.cidade} />
            </Grid>
            <Grid item xs={6} md={2}>
              <TextField fullWidth size="small" label="UF *" value={form.uf}
                inputProps={{ maxLength: 2 }}
                onChange={e => setField('uf', e.target.value.toUpperCase().slice(0, 2))}
                error={!!fieldErr.uf} helperText={fieldErr.uf} />
            </Grid>
            <Grid item xs={12} md={4}>
              <TextField fullWidth size="small" label="Espécie de Documento *" value={form.especieDocumento}
                onChange={e => setField('especieDocumento', e.target.value)}
                error={!!fieldErr.especieDocumento} helperText={fieldErr.especieDocumento} />
            </Grid>
          </Grid>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDialogOpen(false)} disabled={saving}>Cancelar</Button>
          <Button onClick={() => { setForm(emptyForm()); setFieldErr({}) }} disabled={saving} color="warning">Limpar</Button>
          <Button variant="contained" onClick={onSubmit} disabled={saving} loading={saving}>Salvar</Button>
        </DialogActions>
      </Dialog>
    </Box>
  )
}
