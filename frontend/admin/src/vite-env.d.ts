/// <reference types="vite/client" />

interface ImportMetaEnv {
  readonly VITE_URL_API_CLIENTES: string
}

interface ImportMeta {
  readonly env: ImportMetaEnv
}
