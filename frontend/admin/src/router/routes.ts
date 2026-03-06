// src/router/routes.ts
import type { RouteRecordRaw } from 'vue-router';

import MainLayout from 'layouts/MainLayout.vue';

// Páginas públicas
import LoginPage from 'src/pages/login/LoginPage.vue';

// Páginas privadas
import HomePage from 'src/pages/misc/HomePage.vue';
import WorkInProgress from 'src/pages/misc/WorkInProgress.vue';
import MinimalLayout from 'src/layouts/MinimalLayout.vue';

const routes: RouteRecordRaw[] = [
  // PÚBLICAS (com layout mínimo)
  {
    path: '/admin',
    component: MinimalLayout,
    children: [
      { path: '', component: LoginPage, name: 'Login' },
      { path: 'login-2fa', component: () => import('src/pages/login/Login2FAPage.vue'), name: 'Login2FA' },
      { path: 'esqueci-minha-senha', component: () => import('src/pages/forgot-password/ForgotPasswordPage.vue'), name: 'EsqueciMinhaSenha' },
    ],
  },

  //
  // PRIVADAS (Layout Principal) — prefixo /app
  //
  {
    path: '/admin/app',
    component: MainLayout,
    children: [
      { path: '', redirect: { name: 'HomePage' } },

      { path: 'home', component: HomePage, name: 'HomePage', meta: { requiresAuth: true } },

      { path: 'clientes', component: () => import('src/pages/clientes/ClientesPage.vue'), name: 'Clientes', meta: { requiresAuth: true } },
      { path: 'enderecos', component: () => import('src/pages/clientes/EnderecosPage.vue'), name: 'Enderecos', meta: { requiresAuth: true } },
      { path: 'contatos', component: () => import('src/pages/clientes/ContatosPage.vue'), name: 'Contatos', meta: { requiresAuth: true } },
      { path: 'preferencias-contatos', component: () => import('src/pages/clientes/ContactPreferencesPage.vue'), name: 'PreferenciasContatos', meta: { requiresAuth: true } },

      { path: 'contratos', component: () => import('src/pages/contratos/ContratosPage.vue'), name: 'Contratos', meta: { requiresAuth: true } },
      { path: 'contratos/:id', component: () => import('src/pages/contratos/ContratoDetalhesPage.vue'), name: 'ContratoDetalhes', props: true, meta: { requiresAuth: true } },

      { path: 'a-receber', component: () => import('src/pages/contratos/TitulosAReceberPage.vue'), name: 'ATitulosReceber', meta: { requiresAuth: true } },
      { path: 'a-receber/:id', component: () => import('src/pages/contratos/TitulosAReceberDetalhesPage.vue'), name: 'ATituloReceberDetalhes', props: true, meta: { requiresAuth: true } },

      { path: 'recebidos', component: () => import('src/pages/contratos/TitulosRecebidosPage.vue'), name: 'TitulosRecebidos', meta: { requiresAuth: true } },
      { path: 'recebidos/:id', component: () => import('src/pages/contratos/TitulosRecebidosDetalhesPage.vue'), name: 'TituloRecebidoDetalhes', props: true, meta: { requiresAuth: true } },
      { path: 'boletos', component: () => import('src/pages/contratos/BoletosPage.vue'), name: 'Boletos', props: true, meta: { requiresAuth: true } },

      { path: 'emails-enviados', component: () => import('src/pages/mensageria/EmailsEnviadosPage.vue'), name: 'EmailsEnviados', meta: { requiresAuth: true } },
      { path: 'templates-email', component: WorkInProgress, name: 'TemplatesEmail', meta: { requiresAuth: true } },
      { path: 'sms-enviados', component: () => import('src/pages/mensageria/SmsEnviadosPage.vue'), name: 'SmsEnviados', meta: { requiresAuth: true } },

      { path: 'sessoes-ativas', component: () => import('src/pages/seguranca/SessoesAtivasPage.vue'), name: 'SessoesAtivas', meta: { requiresAuth: true } },
      { path: 'atividades-suspeitas', component: WorkInProgress, name: 'AtividadesSuspeitas', meta: { requiresAuth: true } },
      { path: 'ips-bloqueados', component: WorkInProgress, name: 'IpsBloqueados', meta: { requiresAuth: true } },
      { path: 'usuarios-bloqueados', component: WorkInProgress, name: 'UsuariosBloqueados', meta: { requiresAuth: true } },

      { path: 'administradores', component: () => import('src/pages/configuracoes/AdministradoresPage.vue'), name: 'Administradores', meta: { requiresAuth: true } },
      { path: 'config-envios-automaticos', component: () => import('src/pages/configuracoes/MessengerSettingsPage.vue'), name: 'ConfigEnviosAutomaticos', meta: { requiresAuth: true } },
      { path: 'config-boletos', component: () => import('src/pages/configuracoes/ConfiguracoesBoletosPage.vue'), name: 'ConfigBoletos', meta: { requiresAuth: true } },
      { path: 'config-boletos/:id', component: () => import('src/pages/configuracoes/ConfiguracoesBoletosDetalhesPage.vue'), name: 'DetalhesConfigBoletos', meta: { requiresAuth: true } },
      { path: 'config-login', component: WorkInProgress, name: 'ConfigLogin', meta: { requiresAuth: true } },

      { path: 'sincronizacoes/bases', component: () => import('src/pages/sincronizacoes/SincronizacaoBasesPage.vue'), name: 'SincronizacaoBasesPage', meta: { requiresAuth: true } },
      { path: 'sincronizacoes/boletos', component: () => import('src/pages/sincronizacoes/SincronizacaoBoletosPage.vue'), name: 'SincronizacaoBoletosPage', meta: { requiresAuth: true } },
    ],
  },

  //
  // 404
  //
  { path: '/:catchAll(.*)*', component: () => import('src/pages/misc/NotFound.vue'), name: 'NotFound' },
];

export default routes;
