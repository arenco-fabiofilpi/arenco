import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { Box, Typography, Button } from '@mui/material'
import FilterListIcon from '@mui/icons-material/FilterList'
import { apiClientes } from '../../api/axios'
import { ServerTable, type ColumnDef, type PaginationState } from '../../components/ServerTable'

const columns: ColumnDef[] = [
  { field: 'date', headerName: 'Data de Criação', sortable: false },
  { field: 'from', headerName: 'Remetente', sortable: false },
  { field: 'deliveredTo', headerName: 'Destinatário', sortable: false },
  { field: 'subject', headerName: 'Assunto', sortable: false },
  { field: 'messageId', headerName: 'ID da Mensagem', sortable: false },
  { field: 'templateId', headerName: 'ID do Template', sortable: false },
  { field: 'statusCode', headerName: 'Status', sortable: false },
]

export default function EmailsEnviadosPage() {
  const navigate = useNavigate()
  const [lista, setLista] = useState<Record<string, unknown>[]>([])
  const [loading, setLoading] = useState(false)
  const [pagination, setPagination] = useState<PaginationState>({ page: 1, rowsPerPage: 10, rowsNumber: 0 })

  useEffect(() => {
    const fetch = async () => {
      setLoading(true)
      try {
        const resp = await apiClientes.get(
          `/clientes-api/v1/emails-sent?page=${pagination.page}&size=${pagination.rowsPerPage}`
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
      <Typography variant="h4" sx={{ mb: 2 }}>E-mails Enviados</Typography>
      <Button variant="contained" startIcon={<FilterListIcon />} sx={{ mb: 2 }}>Filtros</Button>
      <ServerTable
        rows={lista}
        columns={columns}
        loading={loading}
        pagination={pagination}
        onPaginationChange={setPagination}
        noDataLabel="Nenhum e-mail encontrado."
      />
    </Box>
  )
}
