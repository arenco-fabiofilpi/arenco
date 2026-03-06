import axios from 'axios'

const apiClientes = axios.create({
  baseURL: import.meta.env.VITE_URL_API_CLIENTES,
  withCredentials: true,
})

export { apiClientes }
