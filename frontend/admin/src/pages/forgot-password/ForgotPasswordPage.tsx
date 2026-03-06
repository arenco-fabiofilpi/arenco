import { useState, useRef, useEffect } from 'react'
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
  Stepper,
  Step,
  StepLabel,
  Collapse,
  InputAdornment,
  IconButton,
} from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import HelpIcon from '@mui/icons-material/Help'
import VisibilityIcon from '@mui/icons-material/Visibility'
import VisibilityOffIcon from '@mui/icons-material/VisibilityOff'
import type { AxiosError } from 'axios'
import { apiClientes } from '../../api/axios'

interface ErrorPayload {
  code?: string
  message?: string
  seconds?: number
  otpTokenValid?: boolean
}

type AlertData = { type: 'info' | 'success' | 'error'; message: string } | null

export default function ForgotPasswordPage() {
  const navigate = useNavigate()
  const [activeStep, setActiveStep] = useState(0)
  const [username, setUsername] = useState('')
  const [otp, setOtp] = useState('')
  const [password, setPassword] = useState('')
  const [passwordConfirm, setPasswordConfirm] = useState('')
  const [showPwd, setShowPwd] = useState(false)
  const [showPwdConfirm, setShowPwdConfirm] = useState(false)
  const [loading, setLoading] = useState({ request: false, reset: false })
  const [cooldown, setCooldown] = useState(0)
  const [alert, setAlert] = useState<AlertData>(null)
  const [showHelp, setShowHelp] = useState(false)
  const cooldownRef = useRef<number | null>(null)

  useEffect(() => {
    return () => { if (cooldownRef.current) clearInterval(cooldownRef.current) }
  }, [])

  function startCooldown(secs = 60) {
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

  async function requestReset(isResend = false) {
    if (!username.trim()) return
    setLoading(l => ({ ...l, request: true }))
    setAlert(null)
    try {
      await apiClientes.post('/clientes-api/v1/password-reset', { username: username.trim() })
      setAlert({
        type: 'info',
        message: isResend
          ? 'Se possível, reenviamos um novo código. Verifique SMS/E-mail.'
          : 'Se o usuário existir e tiver contato válido, você receberá um código por SMS/E-mail.',
      })
      setActiveStep(1)
    } catch (err) {
      const e = err as AxiosError<ErrorPayload>
      if (e.response?.status === 429) {
        const secs = e.response.data?.seconds ?? 60
        setAlert({ type: 'error', message: e.response.data?.message ?? `Muitas tentativas. Aguarde ${secs}s.` })
        startCooldown(secs)
      } else {
        setAlert({ type: 'error', message: e.response?.data?.message ?? 'Não foi possível iniciar a recuperação.' })
      }
    } finally {
      setLoading(l => ({ ...l, request: false }))
    }
  }

  async function resetPassword() {
    if (!otp.trim() || !password.trim() || password !== passwordConfirm) return
    setLoading(l => ({ ...l, reset: true }))
    setAlert(null)
    try {
      await apiClientes.post('/clientes-api/v1/password-reset/reset', {
        username: username.trim(),
        token: otp.trim(),
        password,
      })
      setAlert({ type: 'success', message: 'Senha alterada com sucesso. Redirecionando para o login...' })
      setTimeout(() => navigate('/admin'), 700)
    } catch (err) {
      const e = err as AxiosError<ErrorPayload>
      const msg = e.response?.data?.message ?? 'Falha ao redefinir a senha.'
      if (e.response?.status === 400) {
        if (e.response.data?.otpTokenValid === false) {
          setAlert({ type: 'error', message: `${msg} — Solicite um novo código.` })
          setOtp('')
          setActiveStep(0)
          startCooldown(30)
        } else {
          setAlert({ type: 'error', message: msg })
        }
      } else if (e.response?.status === 401) {
        setAlert({ type: 'error', message: msg })
        setOtp('')
        setActiveStep(0)
      } else if (e.response?.status === 429) {
        const secs = e.response.data?.seconds ?? 60
        setAlert({ type: 'error', message: msg })
        startCooldown(secs)
      } else {
        setAlert({ type: 'error', message: msg })
      }
    } finally {
      setLoading(l => ({ ...l, reset: false }))
    }
  }

  return (
    <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '80vh', px: 2 }}>
      <Card sx={{ maxWidth: 520, width: '100%' }}>
        <CardContent>
          <Typography variant="h6" align="center">Esqueci minha senha</Typography>
          <Typography variant="subtitle2" align="center" color="text.secondary" sx={{ mb: 2 }}>
            Redefina sua senha em dois passos
          </Typography>

          {alert && <Alert severity={alert.type} sx={{ mb: 2 }}>{alert.message}</Alert>}

          <Stepper activeStep={activeStep} sx={{ mb: 3 }}>
            <Step><StepLabel>Identificar usuário</StepLabel></Step>
            <Step><StepLabel>Validar código e redefinir</StepLabel></Step>
          </Stepper>

          {activeStep === 0 && (
            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
              <TextField
                label="Usuário"
                value={username}
                onChange={e => setUsername(e.target.value)}
                size="small"
                fullWidth
                disabled={loading.request}
              />
              <Button
                variant="contained"
                disabled={!username.trim() || loading.request || cooldown > 0}
                loading={loading.request}
                onClick={() => requestReset()}
              >
                {cooldown > 0 ? `Enviar código (${cooldown}s)` : 'Enviar código'}
              </Button>
            </Box>
          )}

          {activeStep === 1 && (
            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
              <Typography variant="caption" color="text.secondary">
                Usuário: <strong>{username}</strong>{' '}
                <Button size="small" onClick={() => setActiveStep(0)}>trocar</Button>
              </Typography>

              <TextField
                label="Código (OTP)"
                value={otp}
                onChange={e => setOtp(e.target.value)}
                size="small"
                fullWidth
                inputProps={{ maxLength: 10, inputMode: 'numeric' }}
                disabled={loading.reset}
              />

              <TextField
                label="Nova senha"
                type={showPwd ? 'text' : 'password'}
                value={password}
                onChange={e => setPassword(e.target.value)}
                size="small"
                fullWidth
                disabled={loading.reset}
                InputProps={{
                  endAdornment: (
                    <InputAdornment position="end">
                      <IconButton size="small" onClick={() => setShowPwd(p => !p)}>
                        {showPwd ? <VisibilityOffIcon /> : <VisibilityIcon />}
                      </IconButton>
                    </InputAdornment>
                  ),
                }}
              />

              <TextField
                label="Confirmar nova senha"
                type={showPwdConfirm ? 'text' : 'password'}
                value={passwordConfirm}
                onChange={e => setPasswordConfirm(e.target.value)}
                size="small"
                fullWidth
                disabled={loading.reset}
                error={passwordConfirm !== '' && password !== passwordConfirm}
                helperText={passwordConfirm !== '' && password !== passwordConfirm ? 'As senhas não conferem' : ''}
                InputProps={{
                  endAdornment: (
                    <InputAdornment position="end">
                      <IconButton size="small" onClick={() => setShowPwdConfirm(p => !p)}>
                        {showPwdConfirm ? <VisibilityOffIcon /> : <VisibilityIcon />}
                      </IconButton>
                    </InputAdornment>
                  ),
                }}
              />

              <Box sx={{ display: 'flex', gap: 1 }}>
                <Button
                  variant="contained"
                  color="secondary"
                  disabled={!otp.trim() || !password.trim() || password !== passwordConfirm || loading.reset}
                  loading={loading.reset}
                  onClick={resetPassword}
                  sx={{ flex: 1 }}
                >
                  Validar e redefinir
                </Button>
                <Button
                  variant="outlined"
                  disabled={cooldown > 0 || loading.request}
                  onClick={() => requestReset(true)}
                  sx={{ flex: 1 }}
                >
                  {cooldown > 0 ? `Reenviar (${cooldown}s)` : 'Reenviar código'}
                </Button>
              </Box>
            </Box>
          )}
        </CardContent>

        <CardActions sx={{ justifyContent: 'space-between' }}>
          <Button startIcon={<ArrowBackIcon />} onClick={() => navigate('/admin')} size="small">
            Voltar ao login
          </Button>
          <Button startIcon={<HelpIcon />} onClick={() => setShowHelp(h => !h)} size="small">
            Ajuda
          </Button>
        </CardActions>

        <Collapse in={showHelp}>
          <Box sx={{ px: 2, pb: 2 }}>
            <Typography variant="caption" color="text.secondary">
              • Se o usuário existir e tiver contato válido, você receberá um OTP por SMS/E-mail.<br />
              • Tente novamente após o cooldown se precisar reenviar.<br />
              • Se o token esgotar as tentativas, solicite um novo envio.
            </Typography>
          </Box>
        </Collapse>
      </Card>
    </Box>
  )
}
