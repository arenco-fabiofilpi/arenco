import { useState, useEffect, useRef } from 'react'
import { useNavigate } from 'react-router-dom'
import {
  Box,
  Card,
  CardContent,
  CardActions,
  TextField,
  Button,
  Typography,
  Alert,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Divider,
  Collapse,
} from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import HelpIcon from '@mui/icons-material/Help'
import type { AxiosError } from 'axios'
import { apiClientes } from '../../api/axios'

type ContactMethod = 'EMAIL' | 'CELLPHONE'

interface UserContactDto {
  id: string
  contactMethod: ContactMethod
  contactValue: string
}

interface ErrorPayload {
  code?: string
  message?: string
  otpTokenValid?: boolean
}

type AlertType = { type: 'info' | 'success' | 'error'; message: string } | null

export default function Login2FAPage() {
  const navigate = useNavigate()
  const [contacts, setContacts] = useState<UserContactDto[]>([])
  const [selectedContactId, setSelectedContactId] = useState<string>('')
  const [otp, setOtp] = useState('')
  const [lastSentTo, setLastSentTo] = useState<string | null>(null)
  const [cooldown, setCooldown] = useState(0)
  const [alert, setAlert] = useState<AlertType>(null)
  const [showHelp, setShowHelp] = useState(false)
  const [loading, setLoading] = useState({ contacts: false, trigger: false, validate: false })
  const cooldownRef = useRef<number | null>(null)

  function formatLabel(c: UserContactDto) {
    return `${c.contactMethod === 'CELLPHONE' ? 'Celular' : 'E-mail'} • ${c.contactValue}`
  }

  function startCooldown(secs = 10) {
    setCooldown(secs)
    if (cooldownRef.current) clearInterval(cooldownRef.current)
    cooldownRef.current = window.setInterval(() => {
      setCooldown(prev => {
        if (prev <= 1) {
          clearInterval(cooldownRef.current!)
          cooldownRef.current = null
          return 0
        }
        return prev - 1
      })
    }, 1000)
  }

  useEffect(() => {
    loadContacts()
    return () => { if (cooldownRef.current) clearInterval(cooldownRef.current) }
  }, [])

  async function loadContacts() {
    setLoading(l => ({ ...l, contacts: true }))
    setAlert(null)
    try {
      const { data } = await apiClientes.get<UserContactDto[]>('/clientes-api/v1/otp/contacts-available')
      setContacts(data)
      if (!data.length) setAlert({ type: 'info', message: 'Nenhum contato disponível. Volte e faça login novamente.' })
      else setSelectedContactId(data[0]!.id)
    } catch (err) {
      const e = err as AxiosError<ErrorPayload>
      setAlert({ type: 'error', message: e.response?.data?.message ?? 'Falha ao carregar contatos.' })
    } finally {
      setLoading(l => ({ ...l, contacts: false }))
    }
  }

  async function triggerOtp() {
    if (!selectedContactId) return
    setLoading(l => ({ ...l, trigger: true }))
    setAlert(null)
    try {
      await apiClientes.post('/clientes-api/v1/otp/trigger', { contactId: selectedContactId })
      const found = contacts.find(c => c.id === selectedContactId)
      setLastSentTo(found ? formatLabel(found) : null)
      setAlert({ type: 'success', message: 'Código enviado. Verifique sua caixa de entrada/SMS.' })
      startCooldown(10)
    } catch (err) {
      const e = err as AxiosError<ErrorPayload>
      setAlert({ type: 'error', message: e.response?.data?.message ?? 'Não foi possível enviar o código.' })
      if (e.response?.status === 429) startCooldown(10)
    } finally {
      setLoading(l => ({ ...l, trigger: false }))
    }
  }

  async function validateOtp() {
    if (otp.trim().length < 4) return
    setLoading(l => ({ ...l, validate: true }))
    setAlert(null)
    try {
      await apiClientes.post('/clientes-api/v1/otp/authentication', { otp: otp.trim() })
      setAlert({ type: 'success', message: 'Verificação concluída. Redirecionando...' })
      setTimeout(() => navigate('/admin/app'), 300)
    } catch (err) {
      const e = err as AxiosError<ErrorPayload>
      const msg = e.response?.data?.message ?? 'Falha na validação do código.'
      setAlert({ type: 'error', message: msg })
    } finally {
      setLoading(l => ({ ...l, validate: false }))
    }
  }

  async function logout() {
    try { await apiClientes.post('/clientes-api/v1/auth/logout') } catch {}
    navigate('/admin')
  }

  return (
    <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '80vh', px: 2 }}>
      <Card sx={{ maxWidth: 480, width: '100%' }}>
        <CardContent>
          <Typography variant="h6" align="center">Verificação em duas etapas</Typography>
          <Typography variant="subtitle2" align="center" color="text.secondary" sx={{ mb: 2 }}>
            Escolha um contato para receber o código
          </Typography>

          {alert && <Alert severity={alert.type} sx={{ mb: 2 }}>{alert.message}</Alert>}

          <FormControl fullWidth size="small" sx={{ mb: 1 }} disabled={loading.contacts || loading.trigger || cooldown > 0}>
            <InputLabel>Contato</InputLabel>
            <Select value={selectedContactId} label="Contato" onChange={e => setSelectedContactId(e.target.value)}>
              {contacts.map(c => (
                <MenuItem key={c.id} value={c.id}>{formatLabel(c)}</MenuItem>
              ))}
            </Select>
          </FormControl>

          <Typography variant="caption" color="text.secondary" display="block" sx={{ mb: 1 }}>
            {cooldown > 0
              ? `Aguarde ${cooldown}s para reenviar`
              : 'Selecione um contato e clique em Enviar código'}
          </Typography>

          <Button
            fullWidth
            variant="contained"
            disabled={!selectedContactId || loading.contacts || cooldown > 0}
            loading={loading.trigger}
            onClick={triggerOtp}
            sx={{ mb: 1 }}
          >
            Enviar código
          </Button>

          {lastSentTo && (
            <Typography variant="caption" color="text.secondary" display="block" sx={{ mb: 1 }}>
              Último envio para: <strong>{lastSentTo}</strong>
              {cooldown > 0 && ` — aguarde ${cooldown}s`}
            </Typography>
          )}

          <Divider sx={{ my: 2 }} />

          <TextField
            fullWidth
            size="small"
            label="Código de verificação (OTP)"
            value={otp}
            onChange={e => setOtp(e.target.value)}
            onKeyDown={e => e.key === 'Enter' && validateOtp()}
            inputProps={{ maxLength: 10, inputMode: 'numeric' }}
            disabled={loading.validate}
            sx={{ mb: 2 }}
          />

          <Button
            fullWidth
            variant="contained"
            color="secondary"
            disabled={otp.trim().length < 4 || loading.validate}
            loading={loading.validate}
            onClick={validateOtp}
          >
            Validar código
          </Button>
        </CardContent>

        <CardActions sx={{ justifyContent: 'space-between' }}>
          <Button startIcon={<ArrowBackIcon />} onClick={logout} size="small">
            Voltar ao login
          </Button>
          <Button startIcon={<HelpIcon />} onClick={() => setShowHelp(h => !h)} size="small">
            Precisa de ajuda?
          </Button>
        </CardActions>

        <Collapse in={showHelp}>
          <Box sx={{ px: 2, pb: 2 }}>
            <Typography variant="caption" color="text.secondary">
              • Verifique caixa de entrada/SMS.<br />
              • Se não recebeu, aguarde o cooldown e tente novamente.<br />
              • Se o token expirar, peça um novo envio.
            </Typography>
          </Box>
        </Collapse>
      </Card>
    </Box>
  )
}
