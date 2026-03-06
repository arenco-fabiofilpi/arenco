import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { Box, Typography, Button } from '@mui/material'
import FilterListIcon from '@mui/icons-material/FilterList'
import { apiClientes } from '../../api/axios'
import { ServerTable, type ColumnDef, type PaginationState } from '../../components/ServerTable'

const columns: ColumnDef[] = [
  { field: 'userFullName', headerName: 'Nome do Cliente', sortable: false },
  { field: 'ownerIdErp', headerName: 'ID ERP do Cliente', sortable: false },
  { field: 'value', headerName: 'Contato', sortable: false },
  { field: 'contactType', headerName: 'Tipo de Contato', sortable: false },
  { field: 'active', headerName: 'Ativo?', sortable: false, renderCell: row => row.active ? 'Sim' : 'Não' },
]

export default function ContactPreferencesPage() {
  const navigate = useNavigate()
  const [preferencias, setPreferencias] = useState<Record<string, unknown>[]>([])
  const [loading, setLoading] = useState(false)
  const [pagination, setPagination] = useState<PaginationState>({ page: 1, rowsPerPage: 10, rowsNumber: 0 })

  useEffect(() => {
    const fetch = async () => {
      setLoading(true)
      try {
        const resp = await apiClientes.get(
          `/clientes-api/v1/customers-management/contact-preferences/search?page=${pagination.page}&size=${pagination.rowsPerPage}`
        )
        setPreferencias(resp.data.content ?? [])
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
      <Typography variant="h4" sx={{ mb: 0.5 }}>Preferências de Contato</Typography>
      <Typography variant="caption" color="text.secondary" sx={{ mb: 2, display: 'block' }}>
        Importante para processo de Esqueci Minha Senha
      </Typography>
      <Button variant="contained" startIcon={<FilterListIcon />} sx={{ mb: 2 }}>Filtros</Button>
      <ServerTable
        rows={preferencias}
        columns={columns}
        loading={loading}
        pagination={pagination}
        onPaginationChange={setPagination}
        noDataLabel="Nenhuma preferência de contato encontrada."
      />
    </Box>
  )
}
