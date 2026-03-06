import { useState, useEffect, useCallback } from 'react'
import { useNavigate } from 'react-router-dom'
import { Box, Typography, Button } from '@mui/material'
import FilterListIcon from '@mui/icons-material/FilterList'
import { useSnackbar } from 'notistack'
import { apiClientes } from '../../api/axios'
import { ServerTable, type ColumnDef, type PaginationState } from '../../components/ServerTable'
import { useTriStateMultiSort } from '../../hooks/useTriStateMultiSort'

const columns: ColumnDef[] = [
  { field: 'userFullName', headerName: 'Nome do Cliente', sortable: false },
  { field: 'streetName', headerName: 'Nome da Rua' },
  { field: 'streetNumber', headerName: 'Número' },
  { field: 'cep', headerName: 'CEP' },
  { field: 'district', headerName: 'Bairro' },
  { field: 'city', headerName: 'Cidade' },
  { field: 'state', headerName: 'Estado' },
  { field: 'country', headerName: 'País' },
]

export default function EnderecosPage() {
  const navigate = useNavigate()
  const { enqueueSnackbar } = useSnackbar()
  const [enderecos, setEnderecos] = useState<Record<string, unknown>[]>([])
  const [loading, setLoading] = useState(false)
  const [pagination, setPagination] = useState<PaginationState>({ page: 1, rowsPerPage: 10, rowsNumber: 0 })
  const { dirOf, cycleSort, buildSortQuery } = useTriStateMultiSort({ storageKey: 'enderecos:list:sort', persist: true })

  const fetchEnderecos = useCallback(async (pag: PaginationState) => {
    setLoading(true)
    try {
      const url = `/clientes-api/v1/customers-management/addresses/search?page=${pag.page}&size=${pag.rowsPerPage}${buildSortQuery()}`
      const resp = await apiClientes.get(url)
      setEnderecos(resp.data.content ?? [])
      setPagination(p => ({ ...p, rowsNumber: resp.data.totalElements ?? 0 }))
    } catch (error: unknown) {
      if ((error as { response?: { status: number } }).response?.status === 403) navigate('/admin')
      else enqueueSnackbar('Erro ao buscar endereços.', { variant: 'error' })
    } finally {
      setLoading(false)
    }
  }, [buildSortQuery, navigate, enqueueSnackbar])

  useEffect(() => { fetchEnderecos(pagination) }, [pagination.page, pagination.rowsPerPage])

  function handleSort(field: string) {
    cycleSort(field)
    fetchEnderecos({ ...pagination, page: 1 })
  }

  return (
    <Box>
      <Typography variant="h4" sx={{ mb: 2 }}>Endereços</Typography>
      <Button variant="contained" startIcon={<FilterListIcon />} sx={{ mb: 2 }}>Filtros</Button>
      <ServerTable
        rows={enderecos}
        columns={columns}
        loading={loading}
        pagination={pagination}
        onPaginationChange={setPagination}
        dirOf={dirOf}
        onSort={handleSort}
        noDataLabel="Nenhum endereço encontrado."
        rowsPerPageOptions={[10, 20, 50]}
      />
    </Box>
  )
}
