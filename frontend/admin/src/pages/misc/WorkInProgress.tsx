import { Box, Typography } from '@mui/material'
import ConstructionIcon from '@mui/icons-material/Construction'

export default function WorkInProgress() {
  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        minHeight: '60vh',
        bgcolor: 'grey.100',
        borderRadius: 2,
        p: 4,
      }}
    >
      <ConstructionIcon sx={{ fontSize: 100, color: 'warning.dark', mb: 2 }} />
      <Typography variant="h4" gutterBottom>
        Página em Construção
      </Typography>
      <Typography variant="subtitle1" color="text.secondary">
        Estamos trabalhando para trazer algo incrível. Volte em breve!
      </Typography>
    </Box>
  )
}
