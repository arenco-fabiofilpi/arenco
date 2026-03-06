import { boot } from 'quasar/wrappers'
import axios from 'axios'

const apiClientes = axios.create({
  baseURL: import.meta.env.VITE_URL_API_CLIENTES,
  withCredentials: true, // IMPORTANTE
})

export default boot(({ app }) => {
  // Disponível globalmente via this.$apiClientes etc.
  app.config.globalProperties.$apiClientes = apiClientes
})

export { apiClientes }
