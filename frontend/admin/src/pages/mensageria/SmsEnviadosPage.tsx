import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { Box, Typography, Button } from '@mui/material'
import FilterListIcon from '@mui/icons-material/FilterList'
import { apiClientes } from '../../api/axios'
import { ServerTable, type ColumnDef, type PaginationState } from '../../components/ServerTable'

const columns: ColumnDef[] = [
  { field: 'deliveredTo', headerName: 'Destinatário', sortable: false },
  { field: 'message', headerName: 'Mensagem', sortable: false },
  { field: 'dateCreated', headerName: 'Data de Criação', sortable: false },
  { field: 'status', headerName: 'Status', sortable: false },
]

export default function SmsEnviadosPage() {
  const navigate = useNavigate()
  const [lista, setLista] = useState<Record<string, unknown>[]>([])
  const [loading, setLoading] = useState(false)
  const [pagination, setPagination] = useState<PaginationState>({ page: 1, rowsPerPage: 10, rowsNumber: 0 })

  useEffect(() => {
    const fetch = async () => {
      setLoading(true)
      try {
        const resp = await apiClientes.get(
          `/clientes-api/v1/sms-sent?page=${pagination.page}&size=${pagination.rowsPerPage}`
        )
        setLista(resp.data.content ?? [])
        setPagination(p => ({ ...p, rowsNumber: resp.data.totalElements ?? 0 }))
      } catch (error: unknown) {
        if ((error as { response?: { status: number } }).response?.status === 403) navigate('/admin')
      } finally {
        setLoading(false)
      }
    }
    fetch()
  }, [pagination.page, pagination.rowsPerPage])

  return (
    <Box>
      <Typography variant="h4" sx={{ mb: 2 }}>SMS Enviados</Typography>
      <Button variant="contained" startIcon={<FilterListIcon />} sx={{ mb: 2 }}>Filtros</Button>
      <ServerTable
        rows={lista}
        columns={columns}
        loading={loading}
        pagination={pagination}
        onPaginationChange={setPagination}
        noDataLabel="Nenhum SMS encontrado."
      />
    </Box>
  )
}
