import { useEffect } from 'react'
import { Outlet, useNavigate } from 'react-router-dom'
import { Box, CircularProgress } from '@mui/material'
import { useAuthCheck } from '../hooks/useAuthGuard'

export default function MinimalLayout() {
  const authState = useAuthCheck()
  const navigate = useNavigate()

  useEffect(() => {
    if (authState === 'AUTH_OK') navigate('/admin/app/home', { replace: true })
    if (authState === 'PENDING_2FA') navigate('/admin/login-2fa', { replace: true })
  }, [authState, navigate])

  if (authState === 'loading') {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
        <CircularProgress />
      </Box>
    )
  }

  return (
    <Box sx={{ minHeight: '100vh', bgcolor: 'grey.100' }}>
      <Outlet />
    </Box>
  )
}
