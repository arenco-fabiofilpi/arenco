import { useState, useEffect } from 'react'
import { Outlet, useNavigate, useLocation } from 'react-router-dom'
import {
  AppBar,
  Box,
  CssBaseline,
  Drawer,
  IconButton,
  List,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Toolbar,
  Collapse,
  Divider,
  Badge,
  CircularProgress,
} from '@mui/material'
import MenuIcon from '@mui/icons-material/Menu'
import HomeIcon from '@mui/icons-material/Home'
import PeopleIcon from '@mui/icons-material/People'
import PersonIcon from '@mui/icons-material/Person'
import PlaceIcon from '@mui/icons-material/Place'
import ContactPhoneIcon from '@mui/icons-material/ContactPhone'
import TuneIcon from '@mui/icons-material/Tune'
import DescriptionIcon from '@mui/icons-material/Description'
import RequestPageIcon from '@mui/icons-material/RequestPage'
import CheckCircleIcon from '@mui/icons-material/CheckCircle'
import ReceiptLongIcon from '@mui/icons-material/ReceiptLong'
import SyncIcon from '@mui/icons-material/Sync'
import StorageIcon from '@mui/icons-material/Storage'
import MarkEmailUnreadIcon from '@mui/icons-material/MarkEmailUnread'
import MailIcon from '@mui/icons-material/Mail'
import SmsIcon from '@mui/icons-material/Sms'
import DraftsIcon from '@mui/icons-material/Drafts'
import SecurityIcon from '@mui/icons-material/Security'
import ScheduleIcon from '@mui/icons-material/Schedule'
import WarningIcon from '@mui/icons-material/Warning'
import BlockIcon from '@mui/icons-material/Block'
import PersonOffIcon from '@mui/icons-material/PersonOff'
import SettingsIcon from '@mui/icons-material/Settings'
import SupervisorAccountIcon from '@mui/icons-material/SupervisorAccount'
import SendIcon from '@mui/icons-material/Send'
import LoginIcon from '@mui/icons-material/Login'
import LogoutIcon from '@mui/icons-material/Logout'
import ExpandLessIcon from '@mui/icons-material/ExpandLess'
import ExpandMoreIcon from '@mui/icons-material/ExpandMore'
import { apiClientes } from '../api/axios'
import { useAuthCheck } from '../hooks/useAuthGuard'
import { Navigate } from 'react-router-dom'

const DRAWER_WIDTH = 280

const can = (_perm?: string) => true
const kpis = { titulosVencendoHoje: 3 }

interface NavGroup {
  id: string
  label: string
  icon: React.ReactNode
  items: { label: string; path: string; icon: React.ReactNode; badge?: number; show?: boolean }[]
}

const navGroups: NavGroup[] = [
  {
    id: 'clientes',
    label: 'Clientes',
    icon: <PeopleIcon />,
    items: [
      { label: 'Clientes', path: '/admin/app/clientes', icon: <PersonIcon /> },
      { label: 'Endereços', path: '/admin/app/enderecos', icon: <PlaceIcon /> },
      { label: 'Contatos', path: '/admin/app/contatos', icon: <ContactPhoneIcon /> },
      {
        label: 'Preferências de contato',
        path: '/admin/app/preferencias-contatos',
        icon: <TuneIcon />,
      },
    ],
  },
  {
    id: 'contratos',
    label: 'Contratos',
    icon: <DescriptionIcon />,
    items: [
      { label: 'Contratos', path: '/admin/app/contratos', icon: <DescriptionIcon /> },
      {
        label: 'A Receber',
        path: '/admin/app/a-receber',
        icon: <RequestPageIcon />,
        badge: kpis.titulosVencendoHoje,
      },
      { label: 'Recebidos', path: '/admin/app/recebidos', icon: <CheckCircleIcon /> },
      { label: 'Boletos', path: '/admin/app/boletos', icon: <ReceiptLongIcon /> },
    ],
  },
  {
    id: 'sincronizacoes',
    label: 'Sincronizações',
    icon: <SyncIcon />,
    items: [
      {
        label: 'Sincronização de Bases',
        path: '/admin/app/sincronizacoes/bases',
        icon: <StorageIcon />,
      },
      {
        label: 'Sincronização de Boletos',
        path: '/admin/app/sincronizacoes/boletos',
        icon: <ReceiptLongIcon />,
      },
    ],
  },
  {
    id: 'mensageria',
    label: 'Mensageria',
    icon: <MarkEmailUnreadIcon />,
    items: [
      { label: 'E-mails enviados', path: '/admin/app/emails-enviados', icon: <MailIcon /> },
      { label: 'SMS enviados', path: '/admin/app/sms-enviados', icon: <SmsIcon /> },
      { label: 'Templates de e-mail', path: '/admin/app/templates-email', icon: <DraftsIcon /> },
    ],
  },
  {
    id: 'seguranca',
    label: 'Segurança',
    icon: <SecurityIcon />,
    items: [
      {
        label: 'Sessões ativas',
        path: '/admin/app/sessoes-ativas',
        icon: <ScheduleIcon />,
        show: can('security.sessions:view'),
      },
      {
        label: 'Atividades suspeitas',
        path: '/admin/app/atividades-suspeitas',
        icon: <WarningIcon />,
        show: can('security.threats:view'),
      },
      {
        label: 'IPs bloqueados',
        path: '/admin/app/ips-bloqueados',
        icon: <BlockIcon />,
        show: can('security.ips:view'),
      },
      {
        label: 'Usuários bloqueados',
        path: '/admin/app/usuarios-bloqueados',
        icon: <PersonOffIcon />,
        show: can('security.users:view'),
      },
    ],
  },
  {
    id: 'configuracoes',
    label: 'Configurações',
    icon: <SettingsIcon />,
    items: [
      {
        label: 'Administradores',
        path: '/admin/app/administradores',
        icon: <SupervisorAccountIcon />,
        show: can('admins:view'),
      },
      {
        label: 'Configurações de envios automáticos',
        path: '/admin/app/config-envios-automaticos',
        icon: <SendIcon />,
        show: can('messaging:auto-config:view'),
      },
      {
        label: 'Configurações de boletos',
        path: '/admin/app/config-boletos',
        icon: <ReceiptLongIcon />,
        show: can('billing:slips-config:view'),
      },
      {
        label: 'Configurações de login',
        path: '/admin/app/config-login',
        icon: <LoginIcon />,
        show: can('security:login-config:view'),
      },
    ],
  },
]

export default function MainLayout() {
  const authState = useAuthCheck()
  const navigate = useNavigate()
  const location = useLocation()
  const [mobileOpen, setMobileOpen] = useState(false)
  const [openGroups, setOpenGroups] = useState<Record<string, boolean>>({
    clientes: true,
    contratos: true,
  })

  useEffect(() => {
    if (authState === 'PENDING_2FA') navigate('/admin/login-2fa', { replace: true })
    if (authState === 'NO_SESSION' || authState === 'FORBIDDEN')
      navigate('/admin', { replace: true })
  }, [authState, navigate])

  if (authState === 'loading') {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
        <CircularProgress />
      </Box>
    )
  }

  if (authState !== 'AUTH_OK') return <Navigate to="/admin" replace />

  function toggleGroup(id: string) {
    setOpenGroups(prev => ({ ...prev, [id]: !prev[id] }))
  }

  async function logout() {
    try {
      await apiClientes.post('/clientes-api/v1/auth/logout')
    } catch {}
    navigate('/admin')
  }

  const drawerContent = (
    <Box sx={{ overflow: 'auto' }}>
      <List dense>
        <ListItemButton onClick={() => navigate('/admin/app/home')}>
          <ListItemIcon>
            <HomeIcon />
          </ListItemIcon>
          <ListItemText primary="Home" />
        </ListItemButton>

        <Divider sx={{ my: 0.5 }} />

        {navGroups.map(group => (
          <Box key={group.id}>
            <ListItemButton onClick={() => toggleGroup(group.id)}>
              <ListItemIcon>{group.icon}</ListItemIcon>
              <ListItemText primary={group.label} />
              {openGroups[group.id] ? <ExpandLessIcon /> : <ExpandMoreIcon />}
            </ListItemButton>
            <Collapse in={!!openGroups[group.id]} timeout="auto" unmountOnExit>
              <List dense disablePadding>
                {group.items
                  .filter(item => item.show !== false)
                  .map(item => (
                    <ListItemButton
                      key={item.path}
                      sx={{ pl: 4 }}
                      selected={location.pathname === item.path}
                      onClick={() => {
                        navigate(item.path)
                        setMobileOpen(false)
                      }}
                    >
                      <ListItemIcon sx={{ minWidth: 36 }}>
                        {item.badge ? (
                          <Badge badgeContent={item.badge} color="error">
                            {item.icon}
                          </Badge>
                        ) : (
                          item.icon
                        )}
                      </ListItemIcon>
                      <ListItemText primary={item.label} />
                    </ListItemButton>
                  ))}
              </List>
            </Collapse>
            <Divider sx={{ my: 0.5 }} />
          </Box>
        ))}

        <ListItemButton onClick={logout}>
          <ListItemIcon>
            <LogoutIcon />
          </ListItemIcon>
          <ListItemText primary="Sair" />
        </ListItemButton>
      </List>
    </Box>
  )

  return (
    <Box sx={{ display: 'flex' }}>
      <CssBaseline />

      <AppBar position="fixed" sx={{ zIndex: theme => theme.zIndex.drawer + 1 }}>
        <Toolbar>
          <IconButton
            color="inherit"
            aria-label="Abrir menu"
            edge="start"
            onClick={() => setMobileOpen(prev => !prev)}
            sx={{ mr: 2, display: { lg: 'none' } }}
          >
            <MenuIcon />
          </IconButton>
          <Box sx={{ flex: 1, display: 'flex', justifyContent: 'center' }}>
            <Box
              component="img"
              src="/logo-arenco.png"
              alt="Arenco"
              onClick={() => navigate('/admin/app/home')}
              sx={{ height: 56, maxWidth: 140, cursor: 'pointer', objectFit: 'contain' }}
            />
          </Box>
        </Toolbar>
      </AppBar>

      {/* Mobile drawer */}
      <Drawer
        variant="temporary"
        open={mobileOpen}
        onClose={() => setMobileOpen(false)}
        ModalProps={{ keepMounted: true }}
        sx={{
          display: { xs: 'block', lg: 'none' },
          '& .MuiDrawer-paper': { width: DRAWER_WIDTH, boxSizing: 'border-box' },
        }}
      >
        <Toolbar />
        {drawerContent}
      </Drawer>

      {/* Desktop drawer */}
      <Drawer
        variant="permanent"
        sx={{
          display: { xs: 'none', lg: 'block' },
          '& .MuiDrawer-paper': { width: DRAWER_WIDTH, boxSizing: 'border-box' },
        }}
        open
      >
        <Toolbar />
        {drawerContent}
      </Drawer>

      <Box
        component="main"
        sx={{ flexGrow: 1, p: 3, width: { lg: `calc(100% - ${DRAWER_WIDTH}px)` } }}
      >
        <Toolbar />
        <Outlet />
      </Box>
    </Box>
  )
}
