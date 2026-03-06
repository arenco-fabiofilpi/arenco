// Configuration for your app
// https://v2.quasar.dev/quasar-cli-vite/quasar-config-file

import { defineConfig } from '#q-app/wrappers'

export default defineConfig((/* ctx */) => {
  return {
    // Boot files
    boot: ['axios', 'pinia'],

    // CSS Files
    css: ['app.scss'],

    // Quasar Extras
    extras: ['roboto-font', 'material-icons'],

    // Build configuration
    build: {
      publicPath: '/clientes/',  // 👈 caminho base
      env: {
        VITE_VER_API: 'v1',
        VITE_URL_API_AUTENTICACAO: process.env.VITE_URL_API_AUTENTICACAO,
        VITE_URL_API_CLIENTES: process.env.VITE_URL_API_CLIENTES,
        VITE_URL_API_CONTRATOS: process.env.VITE_URL_API_CONTRATOS
      },
      target: {
        browser: ['es2022', 'firefox115', 'chrome115', 'safari14'],
        node: 'node20',
      },

      vueRouterMode: 'history', // Router mode

      typescript: {
        strict: true,
        vueShim: true,
      },

      vitePlugins: [
        [
          'vite-plugin-checker',
          {
            vueTsc: true,
            eslint: false, // Desativa o ESLint no plugin
          },
        ],
      ],
    },

    // Dev server configuration
    devServer: {
      open: true,
    },

    // Quasar framework configuration
    framework: {
      config: {},
      plugins: ['Dialog', 'Notify'],
    },

    // Animations
    animations: [],

    // PWA Configuration
    pwa: {
      workboxMode: 'GenerateSW',
    },

    // SSR Configuration
    ssr: {
      prodPort: 3000,
      middlewares: ['render'],
      pwa: false,
    },

    // Cordova configuration
    cordova: {},

    // Capacitor configuration
    capacitor: {
      hideSplashscreen: true,
    },

    // Electron configuration
    electron: {
      inspectPort: 5858,
      bundler: 'packager',
      packager: {},
      builder: {
        appId: 'quasar-project',
      },
    },

    // BEX configuration
    bex: {
      extraScripts: [],
    },
  }
})
