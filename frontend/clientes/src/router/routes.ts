// src/router/routes.ts
import type { RouteRecordRaw } from 'vue-router'

// Layouts
import MinimalLayout from 'src/layouts/MinimalLayout.vue'
import MainLayout from 'src/layouts/MainLayout.vue'

// Páginas públicas
import LoginPage from 'src/pages/login/LoginPage.vue'
import AuthenticationPage from 'src/pages/login/AuthenticationPage.vue'

const routes: RouteRecordRaw[] = [
  //
  // PÚBLICAS (layout mínimo) — /clientes
  //
  {
    path: '/clientes',
    component: MinimalLayout,
    children: [
      { path: '', component: LoginPage, name: 'login' },
      {
        path: 'authentication',
        component: AuthenticationPage,
        name: 'authentication'
      }
    ]
  },

  //
  // PRIVADAS (MainLayout) — /clientes/app
  //
  {
    path: '/clientes/app',
    component: MainLayout,
    meta: { requiresAuth: true },
    children: [
      // Home logada
      { path: '', component: () => import('src/pages/HomePage.vue'), name: 'home' },

      // Demais páginas (referenciadas pelo menu e pelos botões)
      { path: 'contratos', component: () => import('src/pages/ContratosPage.vue'), name: 'contratos' },
      {
        path: 'contratos/:id',
        component: () => import('src/pages/ContratoDetalhePage.vue'),
        name: 'contrato-detalhe',
        props: true
      },
      { path: 'boletos', component: () => import('src/pages/BoletosPage.vue'), name: 'boletos' },
      { path: 'pagamentos', component: () => import('src/pages/PagamentosPage.vue'), name: 'pagamentos' },
      {
        path: 'dados-cadastrais',
        component: () => import('src/pages/DadosCadastraisPage.vue'),
        name: 'dados-cadastrais'
      },
      { path: 'suporte', component: () => import('src/pages/SuportePage.vue'), name: 'suporte' }
    ]
  },

  //
  // 404
  //
  {
    path: '/:catchAll(.*)*',
    component: () => import('src/pages/misc/NotFound.vue'),
    name: 'not-found'
  }
]

export default routes
