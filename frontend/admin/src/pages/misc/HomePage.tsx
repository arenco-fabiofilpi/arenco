import { useNavigate } from 'react-router-dom'
import {
  Box,
  Card,
  CardContent,
  Grid,
  Typography,
  List,
  ListItemButton,
  ListItemIcon,
  ListItemText,
} from '@mui/material'
import PeopleIcon from '@mui/icons-material/People'
import DescriptionIcon from '@mui/icons-material/Description'
import ReceiptLongIcon from '@mui/icons-material/ReceiptLong'
import EventIcon from '@mui/icons-material/Event'
import MailIcon from '@mui/icons-material/Mail'
import PersonAddIcon from '@mui/icons-material/PersonAdd'

const userName = 'Fabio'
const today = new Date().toLocaleDateString('pt-BR', {
  weekday: 'long',
  day: '2-digit',
  month: 'long',
  year: 'numeric',
})

const kpis = [
  { label: 'Clientes ativos', value: 128, icon: <PeopleIcon color="primary" fontSize="large" /> },
  { label: 'Contratos ativos', value: 54, icon: <DescriptionIcon color="primary" fontSize="large" /> },
  { label: 'Títulos a vencer hoje', value: 7, icon: <EventIcon color="primary" fontSize="large" /> },
  { label: 'Mensagens enviadas hoje', value: 312, icon: <MailIcon color="primary" fontSize="large" /> },
]

const logs = [
  { id: 1, title: 'Contrato criado', time: '10:15', desc: 'Contrato #123 criado para Cliente X' },
  { id: 2, title: 'Boleto enviado', time: '09:40', desc: 'Boleto de R$ 1.200 enviado para Cliente Y' },
  { id: 3, title: 'Login suspeito', time: '08:22', desc: 'Tentativa inválida de login do IP 177.xxx' },
]

export default function HomePage() {
  const navigate = useNavigate()

  return (
    <Box>
      <Box sx={{ mb: 3 }}>
        <Typography variant="h5">Bem-vindo, {userName}</Typography>
        <Typography variant="caption" color="text.secondary">
          {today}
        </Typography>
      </Box>

      <Grid container spacing={3}>
        <Grid item xs={12} lg={8}>
          <Grid container spacing={2} sx={{ mb: 3 }}>
            {kpis.map(kpi => (
              <Grid item xs={12} sm={6} md={3} key={kpi.label}>
                <Card variant="outlined" sx={{ textAlign: 'center', p: 2, height: '100%' }}>
                  {kpi.icon}
                  <Typography variant="h5" sx={{ mt: 1 }}>
                    {kpi.value}
                  </Typography>
                  <Typography variant="caption" color="text.secondary">
                    {kpi.label}
                  </Typography>
                </Card>
              </Grid>
            ))}
          </Grid>

          <Card variant="outlined">
            <CardContent>
              <Typography variant="subtitle1" sx={{ mb: 2 }}>
                Últimas atividades
              </Typography>
              {logs.map(log => (
                <Box key={log.id} sx={{ mb: 2, pl: 2, borderLeft: 2, borderColor: 'primary.main' }}>
                  <Typography variant="body2" fontWeight={600}>
                    {log.title} <Typography component="span" variant="caption" color="text.secondary">• {log.time}</Typography>
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    {log.desc}
                  </Typography>
                </Box>
              ))}
            </CardContent>
          </Card>
        </Grid>

        <Grid item xs={12} lg={4}>
          <Card variant="outlined" sx={{ position: 'sticky', top: 80 }}>
            <CardContent>
              <Typography variant="subtitle1" sx={{ mb: 1 }}>
                Atalhos rápidos
              </Typography>
              <List dense>
                <ListItemButton onClick={() => navigate('/admin/app/clientes')}>
                  <ListItemIcon><PersonAddIcon /></ListItemIcon>
                  <ListItemText primary="Novo Cliente" />
                </ListItemButton>
                <ListItemButton onClick={() => navigate('/admin/app/contratos')}>
                  <ListItemIcon><DescriptionIcon /></ListItemIcon>
                  <ListItemText primary="Contratos" />
                </ListItemButton>
                <ListItemButton onClick={() => navigate('/admin/app/config-boletos')}>
                  <ListItemIcon><ReceiptLongIcon /></ListItemIcon>
                  <ListItemText primary="Configurações de Boletos" />
                </ListItemButton>
              </List>
            </CardContent>
          </Card>
        </Grid>
      </Grid>
    </Box>
  )
}
