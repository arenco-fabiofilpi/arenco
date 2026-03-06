import { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { Box, Button, CircularProgress, Alert } from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import { apiClientes } from '../../api/axios'
import { ContratoDetalhesCard } from '../../components/ContratoDetalhesCard'

export default function ContratoDetalhesPage() {
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()
  const [contrato, setContrato] = useState<Record<string, unknown> | null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    if (!id) { setLoading(false); return }
    apiClientes.get(`/clientes-api/v1/admin/contratos?id=${id}`)
      .then(resp => setContrato(resp.data))
      .catch(err => console.error('Erro ao carregar contrato:', err))
      .finally(() => setLoading(false))
  }, [id])

  return (
    <Box>
      <Button startIcon={<ArrowBackIcon />} onClick={() => navigate(-1)} sx={{ mb: 2 }}>Voltar</Button>
      {loading && <CircularProgress />}
      {!loading && !contrato && <Alert severity="warning">Contrato não encontrado.</Alert>}
      {!loading && contrato && <ContratoDetalhesCard contrato={contrato} />}
    </Box>
  )
}
