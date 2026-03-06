import { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import {
  Box, Button, Card, CardContent, Grid, TextField, Select, MenuItem,
  FormControl, InputLabel, LinearProgress, Stack, Chip, Typography,
} from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import SaveIcon from '@mui/icons-material/Save'
import DeleteIcon from '@mui/icons-material/Delete'
import { useSnackbar } from 'notistack'
import { apiClientes } from '../../api/axios'
import { ConfirmDialog } from '../../components/ConfirmDialog'

type Model = {
  version?: number
  id?: string
  dateCreated?: string | null
  lastUpdated?: string | null
  banco?: string | null
  nomeBeneficiario?: string | null
  documento?: string | null
  agencia?: string | null
  codigoBeneficiario?: string | null
  digitoCodigoBeneficiario?: string | null
  carteira?: string | null
  localDePagamento?: string | null
  instrucoes?: string | null
  logradouro?: string | null
  bairro?: string | null
  cep?: string | null
  cidade?: string | null
  uf?: string | null
  especieDocumento?: string | null
}

export default function ConfiguracoesBoletosDetalhesPage() {
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()
  const { enqueueSnackbar } = useSnackbar()
  const [model, setModel] = useState<Model>({})
  const [loading, setLoading] = useState(false)
  const [saving, setSaving] = useState(false)
  const [removing, setRemoving] = useState(false)
  const [confirmRemove, setConfirmRemove] = useState(false)

  function setField(key: keyof Model, value: string) {
    setModel(m => ({ ...m, [key]: value }))
  }

  async function load() {
    if (!id) return
    setLoading(true)
    try {
      const resp = await apiClientes.get(`/clientes-api/v1/admin/payment-slip-settings/${id}`)
      setModel(resp.data || {})
    } catch (error: unknown) {
      if ((error as { response?: { status: number } }).response?.status === 403) navigate('/admin')
      else enqueueSnackbar('Erro ao carregar detalhes.', { variant: 'error' })
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { load() }, [id])

  async function onSave() {
    setSaving(true)
    try {
      await apiClientes.put(`/clientes-api/v1/admin/payment-slip-settings/${id}`, model)
      enqueueSnackbar('Configuração atualizada com sucesso.', { variant: 'success' })
      await load()
    } catch (error: unknown) {
      const msg = (error as { response?: { data?: { message?: string } } }).response?.data?.message ?? 'Erro ao salvar configuração.'
      enqueueSnackbar(msg, { variant: 'error' })
    } finally {
      setSaving(false)
    }
  }

  async function onRemove() {
    setRemoving(true)
    try {
      await apiClientes.delete(`/clientes-api/v1/admin/payment-slip-settings/${id}`)
      enqueueSnackbar('Configuração removida com sucesso.', { variant: 'success' })
      navigate('/admin/app/config-boletos')
    } catch (error: unknown) {
      const msg = (error as { response?: { data?: { message?: string } } }).response?.data?.message ?? 'Erro ao remover configuração.'
      enqueueSnackbar(msg, { variant: 'error' })
    } finally {
      setRemoving(false)
    }
  }

  return (
    <Box>
      <Stack direction="row" alignItems="center" sx={{ mb: 2 }} spacing={1}>
        <Button startIcon={<ArrowBackIcon />} onClick={() => navigate(-1)}>Voltar</Button>
        <Box sx={{ flex: 1 }} />
        <Button variant="outlined" color="error" startIcon={<DeleteIcon />} onClick={() => setConfirmRemove(true)} disabled={removing || loading}>
          Remover
        </Button>
        <Button variant="contained" startIcon={<SaveIcon />} onClick={onSave} disabled={saving || loading} loading={saving}>
          Salvar alterações
        </Button>
      </Stack>

      <Stack direction="row" alignItems="center" sx={{ mb: 2 }} spacing={1}>
        <Typography variant="h5">Detalhes da Configuração de Boleto</Typography>
        <Box sx={{ flex: 1 }} />
        {model.id && <Chip label={`ID: ${model.id}`} variant="outlined" color="primary" size="small" />}
        {model.version !== undefined && <Chip label={`v${model.version}`} variant="outlined" color="secondary" size="small" />}
        {model.dateCreated && <Chip label={`Criado: ${model.dateCreated}`} variant="outlined" size="small" />}
        {model.lastUpdated && <Chip label={`Atualizado: ${model.lastUpdated}`} variant="outlined" size="small" />}
      </Stack>

      <Card variant="outlined">
        {loading && <LinearProgress />}
        {!loading && (
          <CardContent>
            <Grid container spacing={2}>
              <Grid item xs={12} md={4}>
                <FormControl fullWidth size="small">
                  <InputLabel>Banco *</InputLabel>
                  <Select value={model.banco ?? ''} label="Banco *" onChange={e => setField('banco', e.target.value)}>
                    <MenuItem value="BRADESCO">Bradesco</MenuItem>
                  </Select>
                </FormControl>
              </Grid>
              <Grid item xs={12} md={4}>
                <TextField fullWidth size="small" label="Nome do Beneficiário *" value={model.nomeBeneficiario ?? ''}
                  onChange={e => setField('nomeBeneficiario', e.target.value)} />
              </Grid>
              <Grid item xs={12} md={4}>
                <TextField fullWidth size="small" label="Documento (CNPJ/CPF) *" value={model.documento ?? ''}
                  onChange={e => setField('documento', e.target.value)} />
              </Grid>
              <Grid item xs={12} md={3}>
                <TextField fullWidth size="small" label="Agência *" value={model.agencia ?? ''}
                  onChange={e => setField('agencia', e.target.value)} />
              </Grid>
              <Grid item xs={12} md={4}>
                <TextField fullWidth size="small" label="Código Beneficiário *" value={model.codigoBeneficiario ?? ''}
                  onChange={e => setField('codigoBeneficiario', e.target.value)} />
              </Grid>
              <Grid item xs={12} md={3}>
                <TextField fullWidth size="small" label="Carteira *" value={model.carteira ?? ''}
                  onChange={e => setField('carteira', e.target.value)} />
              </Grid>
              <Grid item xs={12} md={2}>
                <TextField fullWidth size="small" label="Dígito" value={model.digitoCodigoBeneficiario ?? ''}
                  onChange={e => setField('digitoCodigoBeneficiario', e.target.value)} />
              </Grid>
              <Grid item xs={12}>
                <TextField fullWidth size="small" label="Instruções (texto no boleto)" value={model.instrucoes ?? ''}
                  onChange={e => setField('instrucoes', e.target.value)} />
              </Grid>
              <Grid item xs={12} md={6}>
                <TextField fullWidth size="small" label="Local de Pagamento" value={model.localDePagamento ?? ''}
                  onChange={e => setField('localDePagamento', e.target.value)} />
              </Grid>
              <Grid item xs={12} md={6}>
                <TextField fullWidth size="small" label="Espécie de Documento" value={model.especieDocumento ?? ''}
                  onChange={e => setField('especieDocumento', e.target.value)} />
              </Grid>
              <Grid item xs={12} md={6}>
                <TextField fullWidth size="small" label="Logradouro" value={model.logradouro ?? ''}
                  onChange={e => setField('logradouro', e.target.value)} />
              </Grid>
              <Grid item xs={12} md={6}>
                <TextField fullWidth size="small" label="Bairro" value={model.bairro ?? ''}
                  onChange={e => setField('bairro', e.target.value)} />
              </Grid>
              <Grid item xs={12} md={5}>
                <TextField fullWidth size="small" label="Cidade" value={model.cidade ?? ''}
                  onChange={e => setField('cidade', e.target.value)} />
              </Grid>
              <Grid item xs={12} md={2}>
                <TextField fullWidth size="small" label="UF" value={model.uf ?? ''}
                  inputProps={{ maxLength: 2 }}
                  onChange={e => setField('uf', e.target.value.toUpperCase().slice(0, 2))} />
              </Grid>
              <Grid item xs={12} md={5}>
                <TextField fullWidth size="small" label="CEP" value={model.cep ?? ''}
                  onChange={e => setField('cep', e.target.value)} />
              </Grid>
            </Grid>
          </CardContent>
        )}
      </Card>

      <ConfirmDialog
        open={confirmRemove}
        title="Confirmar"
        message="Tem certeza que deseja remover esta configuração?"
        confirmLabel="Remover"
        confirmColor="error"
        onConfirm={() => { setConfirmRemove(false); onRemove() }}
        onCancel={() => setConfirmRemove(false)}
      />
    </Box>
  )
}
