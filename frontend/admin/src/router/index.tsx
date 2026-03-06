import { lazy, Suspense } from 'react'
import { Routes, Route, Navigate } from 'react-router-dom'
import { Box, CircularProgress } from '@mui/material'
import MainLayout from '../layouts/MainLayout'
import MinimalLayout from '../layouts/MinimalLayout'
import LoginPage from '../pages/login/LoginPage'
import HomePage from '../pages/misc/HomePage'
import WorkInProgress from '../pages/misc/WorkInProgress'
import NotFound from '../pages/misc/NotFound'

const Login2FAPage = lazy(() => import('../pages/login/Login2FAPage'))
const ForgotPasswordPage = lazy(() => import('../pages/forgot-password/ForgotPasswordPage'))
const ClientesPage = lazy(() => import('../pages/clientes/ClientesPage'))
const EnderecosPage = lazy(() => import('../pages/clientes/EnderecosPage'))
const ContatosPage = lazy(() => import('../pages/clientes/ContatosPage'))
const ContactPreferencesPage = lazy(() => import('../pages/clientes/ContactPreferencesPage'))
const ContratosPage = lazy(() => import('../pages/contratos/ContratosPage'))
const ContratoDetalhesPage = lazy(() => import('../pages/contratos/ContratoDetalhesPage'))
const TitulosAReceberPage = lazy(() => import('../pages/contratos/TitulosAReceberPage'))
const TitulosAReceberDetalhesPage = lazy(
  () => import('../pages/contratos/TitulosAReceberDetalhesPage'),
)
const TitulosRecebidosPage = lazy(() => import('../pages/contratos/TitulosRecebidosPage'))
const TitulosRecebidosDetalhesPage = lazy(
  () => import('../pages/contratos/TitulosRecebidosDetalhesPage'),
)
const BoletosPage = lazy(() => import('../pages/contratos/BoletosPage'))
const EmailsEnviadosPage = lazy(() => import('../pages/mensageria/EmailsEnviadosPage'))
const SmsEnviadosPage = lazy(() => import('../pages/mensageria/SmsEnviadosPage'))
const SessoesAtivasPage = lazy(() => import('../pages/seguranca/SessoesAtivasPage'))
const AdministradoresPage = lazy(() => import('../pages/configuracoes/AdministradoresPage'))
const MessengerSettingsPage = lazy(() => import('../pages/configuracoes/MessengerSettingsPage'))
const ConfiguracoesBoletosPage = lazy(
  () => import('../pages/configuracoes/ConfiguracoesBoletosPage'),
)
const ConfiguracoesBoletosDetalhesPage = lazy(
  () => import('../pages/configuracoes/ConfiguracoesBoletosDetalhesPage'),
)
const SincronizacaoBasesPage = lazy(
  () => import('../pages/sincronizacoes/SincronizacaoBasesPage'),
)
const SincronizacaoBoletosPage = lazy(
  () => import('../pages/sincronizacoes/SincronizacaoBoletosPage'),
)

function Loading() {
  return (
    <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '50vh' }}>
      <CircularProgress />
    </Box>
  )
}

export function AppRouter() {
  return (
    <Suspense fallback={<Loading />}>
      <Routes>
        <Route path="/" element={<Navigate to="/admin" replace />} />

        <Route path="/admin" element={<MinimalLayout />}>
          <Route index element={<LoginPage />} />
          <Route path="login-2fa" element={<Login2FAPage />} />
          <Route path="esqueci-minha-senha" element={<ForgotPasswordPage />} />
        </Route>

        <Route path="/admin/app" element={<MainLayout />}>
          <Route index element={<Navigate to="home" replace />} />
          <Route path="home" element={<HomePage />} />

          <Route path="clientes" element={<ClientesPage />} />
          <Route path="enderecos" element={<EnderecosPage />} />
          <Route path="contatos" element={<ContatosPage />} />
          <Route path="preferencias-contatos" element={<ContactPreferencesPage />} />

          <Route path="contratos" element={<ContratosPage />} />
          <Route path="contratos/:id" element={<ContratoDetalhesPage />} />

          <Route path="a-receber" element={<TitulosAReceberPage />} />
          <Route path="a-receber/:id" element={<TitulosAReceberDetalhesPage />} />

          <Route path="recebidos" element={<TitulosRecebidosPage />} />
          <Route path="recebidos/:id" element={<TitulosRecebidosDetalhesPage />} />

          <Route path="boletos" element={<BoletosPage />} />

          <Route path="emails-enviados" element={<EmailsEnviadosPage />} />
          <Route path="templates-email" element={<WorkInProgress />} />
          <Route path="sms-enviados" element={<SmsEnviadosPage />} />

          <Route path="sessoes-ativas" element={<SessoesAtivasPage />} />
          <Route path="atividades-suspeitas" element={<WorkInProgress />} />
          <Route path="ips-bloqueados" element={<WorkInProgress />} />
          <Route path="usuarios-bloqueados" element={<WorkInProgress />} />

          <Route path="administradores" element={<AdministradoresPage />} />
          <Route path="config-envios-automaticos" element={<MessengerSettingsPage />} />
          <Route path="config-boletos" element={<ConfiguracoesBoletosPage />} />
          <Route path="config-boletos/:id" element={<ConfiguracoesBoletosDetalhesPage />} />
          <Route path="config-login" element={<WorkInProgress />} />

          <Route path="sincronizacoes/bases" element={<SincronizacaoBasesPage />} />
          <Route path="sincronizacoes/boletos" element={<SincronizacaoBoletosPage />} />
        </Route>

        <Route path="*" element={<NotFound />} />
      </Routes>
    </Suspense>
  )
}
