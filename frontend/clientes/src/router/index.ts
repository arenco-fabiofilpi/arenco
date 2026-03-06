// src/router/index.ts
import type { RouteLocationNormalized } from 'vue-router';
import { createRouter, createWebHistory } from 'vue-router';
import routes from './routes';
import { apiClientes } from 'src/boot/axios';

const router = createRouter({
  history: createWebHistory(), // ajuste a base se precisar
  routes,
});

// Estados possíveis da verificação de auth
type AuthState = 'AUTH_OK' | 'NO_SESSION' | 'FORBIDDEN';

// Rotas públicas por NAME
const PUBLIC_ROUTE_NAMES = new Set<string>([
  'login',
  'authentication',
]);

// destino padrão para quem está autenticado
const APP_HOME = { name: 'home' };

// memo por navegação
let inflightAuthCheck: Promise<AuthState> | null = null;

async function checkAuth(): Promise<AuthState> {
  try {
    const resp = await apiClientes.get('/clientes-api/v1/customer/auth', {
      withCredentials: true,
      validateStatus: s => (s >= 200 && s < 300) || s === 401 || s === 403 || s === 206,
      headers: { 'Cache-Control': 'no-cache' },
    });

    if (resp.status === 200) return 'AUTH_OK';
    if (resp.status === 403) return 'FORBIDDEN';
    return 'NO_SESSION';
  } catch {
    return 'NO_SESSION';
  }
}

function isPublicRoute(to: RouteLocationNormalized): boolean {
  // prioridade ao meta.public
  if (to.matched.some(r => r.meta?.public === true)) return true;
  // fallback por nome
  return !!(to.name && PUBLIC_ROUTE_NAMES.has(String(to.name)));
}

router.beforeEach(async (to) => {
  // Você pode otimizar: se a rota for pública e você não
  // precisar redirecionar logados -> públicas, poderia pular o checkAuth.
  // Aqui mantemos a verificação para suportar esse redirecionamento.
  if (!inflightAuthCheck) inflightAuthCheck = checkAuth();
  const auth = await inflightAuthCheck;
  inflightAuthCheck = null;

  if (isPublicRoute(to)) {
    // se autenticado, manda para a home logada
    if (auth === 'AUTH_OK') return APP_HOME;

    return true;
  }

  // Rotas privadas (exigem sessão)
  if (auth === 'AUTH_OK') return true;

  if (auth === 'FORBIDDEN') {
    // opcional: crie rota "Forbidden" dedicada
    return { name: 'login', query: { from: to.fullPath } };
  }

  // NO_SESSION
  return { name: 'login', query: { redirect: to.fullPath } };
});

export default router;
