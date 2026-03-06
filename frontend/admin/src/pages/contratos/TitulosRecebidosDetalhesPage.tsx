import { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { Box, Button, CircularProgress, Alert } from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import { apiClientes } from '../../api/axios'
import { TituloRecebidoDetalhesCard } from '../../components/TituloRecebidoDetalhesCard'

export default function TitulosRecebidosDetalhesPage() {
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()
  const [titulo, setTitulo] = useState<Record<string, unknown> | null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    if (!id) { setLoading(false); return }
    apiClientes.get(`/clientes-api/v1/admin/recebidos?id=${id}`)
      .then(resp => setTitulo(resp.data))
      .catch(err => console.error('Erro ao carregar título:', err))
      .finally(() => setLoading(false))
  }, [id])

  return (
    <Box>
      <Button startIcon={<ArrowBackIcon />} onClick={() => navigate(-1)} sx={{ mb: 2 }}>Voltar</Button>
      {loading && <CircularProgress />}
      {!loading && !titulo && <Alert severity="warning">Título não encontrado.</Alert>}
      {!loading && titulo && <TituloRecebidoDetalhesCard titulo={titulo} />}
    </Box>
  )
}
