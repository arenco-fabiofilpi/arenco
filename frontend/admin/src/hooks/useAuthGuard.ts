import { useState, useEffect } from 'react'
import { apiClientes } from '../api/axios'

export type AuthState = 'loading' | 'AUTH_OK' | 'PENDING_2FA' | 'NO_SESSION' | 'FORBIDDEN'

export function useAuthCheck(): AuthState {
  const [authState, setAuthState] = useState<AuthState>('loading')

  useEffect(() => {
    apiClientes
      .get('/clientes-api/v1/auth', {
        validateStatus: s => (s >= 200 && s < 300) || s === 401 || s === 403 || s === 206,
        headers: { 'Cache-Control': 'no-cache' },
      })
      .then(resp => {
        if (resp.status === 200) setAuthState('AUTH_OK')
        else if (resp.status === 206) setAuthState('PENDING_2FA')
        else if (resp.status === 403) setAuthState('FORBIDDEN')
        else setAuthState('NO_SESSION')
      })
      .catch(() => setAuthState('NO_SESSION'))
  }, [])

  return authState
}
