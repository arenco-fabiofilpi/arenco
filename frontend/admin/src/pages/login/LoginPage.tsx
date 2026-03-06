import { useState } from 'react'
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
} from '@mui/material'
import type { AxiosError } from 'axios'
import { apiClientes } from '../../api/axios'

export default function LoginPage() {
  const navigate = useNavigate()
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [errorMessage, setErrorMessage] = useState('')
  const [loading, setLoading] = useState(false)

  const formValido = username.trim() !== '' && password.trim() !== ''

  async function handleLogin(e: React.FormEvent) {
    e.preventDefault()
    setLoading(true)
    setErrorMessage('')
    try {
      const resp = await apiClientes.post('/clientes-api/v1/auth/login', { username, password })
      if (resp.status === 206) return navigate('/admin/login-2fa')
      navigate('/admin/app')
    } catch (err) {
      const error = err as AxiosError<{ message: string }>
      if (error.response) {
        setErrorMessage(error.response.data?.message ?? 'Erro ao fazer login.')
      }
    } finally {
      setLoading(false)
    }
  }

  return (
    <Box sx={{ display: 'flex', justifyContent: 'center', pt: 8, px: 2 }}>
      <Card sx={{ maxWidth: 400, width: '100%' }}>
        <CardContent>
          <Box sx={{ textAlign: 'center', mb: 2 }}>
            <Box
              component="img"
              src="/logo-arenco.png"
              alt="Arenco"
              sx={{ height: 80, maxWidth: 150 }}
            />
            <Typography variant="h6" sx={{ mt: 1 }}>
              Login
            </Typography>
          </Box>

          <Box component="form" onSubmit={handleLogin} sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
            <TextField
              label="Usuário"
              value={username}
              onChange={e => setUsername(e.target.value)}
              size="small"
              fullWidth
              required
            />
            <TextField
              label="Senha"
              type="password"
              value={password}
              onChange={e => setPassword(e.target.value)}
              size="small"
              fullWidth
              required
            />

            {errorMessage && <Alert severity="error">{errorMessage}</Alert>}

            <Button
              type="submit"
              variant="contained"
              disabled={!formValido || loading}
              loading={loading}
            >
              Entrar
            </Button>
          </Box>
        </CardContent>
        <CardActions sx={{ justifyContent: 'center' }}>
          <Button size="small" onClick={() => navigate('/admin/esqueci-minha-senha')}>
            Esqueci minha senha
          </Button>
        </CardActions>
      </Card>
    </Box>
  )
}
