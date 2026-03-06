import { Box, Typography, Button } from '@mui/material'
import { useNavigate } from 'react-router-dom'

export default function NotFound() {
  const navigate = useNavigate()
  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        minHeight: '100vh',
        gap: 2,
      }}
    >
      <Typography variant="h1" color="text.secondary">
        404
      </Typography>
      <Typography variant="h5">Página não encontrada</Typography>
      <Button variant="contained" onClick={() => navigate('/admin/app/home')}>
        Voltar ao início
      </Button>
    </Box>
  )
}
