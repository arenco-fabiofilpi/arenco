import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { Box, Typography, Button } from '@mui/material'
import FilterListIcon from '@mui/icons-material/FilterList'
import { apiClientes } from '../../api/axios'
import { ServerTable, type ColumnDef, type PaginationState } from '../../components/ServerTable'

const columns: ColumnDef[] = [
  { field: 'dateCreated', headerName: 'Data de Criação', sortable: false },
  { field: 'fileType', headerName: 'Tipo de Arquivo', sortable: false },
  { field: 'cliente', headerName: 'Cliente', sortable: false },
  { field: 'nomeClte', headerName: 'Nome Cliente', sortable: false },
  { field: 'numeDoc', headerName: 'Contrato', sortable: false },
  { field: 'sequencia', headerName: 'Sequência', sortable: false },
  { field: 'lote', headerName: 'Lote', sortable: false },
  { field: 'dtEmissao', headerName: 'Data Emissão', sortable: false },
  { field: 'dataBase', headerName: 'Data Base', sortable: false },
  { field: 'ccusto', headerName: 'Centro de Custos', sortable: false },
  { field: 'nomeCcusto', headerName: 'Nome Centro de Custos', sortable: false },
  { field: 'fatura', headerName: 'Fatura', sortable: false },
  { field: 'numeFatura', headerName: 'Número da Fatura', sortable: false },
  { field: 'vencimento', headerName: 'Vencimento', sortable: false },
  { field: 'valor', headerName: 'Valor', sortable: false },
  { field: 'tipoDoc', headerName: 'Tipo Documento', sortable: false },
  { field: 'serie', headerName: 'Série', sortable: false },
  { field: 'observacao', headerName: 'Observação', sortable: false },
]

function extrairFilename(contentDisposition?: string | null, fallback = 'arquivo') {
  if (!contentDisposition) return fallback
  const utf8 = /filename\*\s*=\s*UTF-8''([^;]+)/i.exec(contentDisposition)
  if (utf8?.[1]) return decodeURIComponent(utf8[1])
  const ascii = /filename\s*=\s*"([^"]+)"/i.exec(contentDisposition)
  if (ascii?.[1]) return ascii[1]
  const plain = /filename\s*=\s*([^;]+)/i.exec(contentDisposition)
  if (plain?.[1]) return plain[1].trim()
  return fallback
}

export default function BoletosPage() {
  const navigate = useNavigate()
  const [lista, setLista] = useState<Record<string, unknown>[]>([])
  const [loading, setLoading] = useState(false)
  const [pagination, setPagination] = useState<PaginationState>({ page: 1, rowsPerPage: 10, rowsNumber: 0 })

  useEffect(() => {
    const fetch = async () => {
      setLoading(true)
      try {
        const resp = await apiClientes.post(
          `/clientes-api/v1/admin/boletos/search?page=${pagination.page}&size=${pagination.rowsPerPage}`,
          {}
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

  async function baixar(row: Record<string, unknown>) {
    const boletoId = row.id
    const formato = row.fileType as string
    if (!boletoId) return
    try {
      const url = `/clientes-api/v1/admin/boletos/${encodeURIComponent(String(boletoId))}`
      const response = await apiClientes.get(url, { responseType: 'blob' })
      const contentType = (response.headers as Record<string, string>)['content-type'] || (formato === 'PDF' ? 'application/pdf' : 'image/png')
      const contentDisposition = (response.headers as Record<string, string>)['content-disposition']
      const filename = extrairFilename(contentDisposition, `boleto-${boletoId}.${formato}`)
      const blob = new Blob([response.data as BlobPart], { type: contentType })
      const href = URL.createObjectURL(blob)
      const a = document.createElement('a')
      a.href = href
      a.download = filename
      document.body.appendChild(a)
      a.click()
      a.remove()
      URL.revokeObjectURL(href)
    } catch (err) {
      console.error(`Erro ao baixar boleto ${formato}:`, err)
    }
  }

  return (
    <Box>
      <Typography variant="h4" sx={{ mb: 2 }}>Boletos</Typography>
      <Button variant="contained" startIcon={<FilterListIcon />} sx={{ mb: 2 }}>Filtros</Button>
      <ServerTable
        rows={lista}
        columns={columns}
        loading={loading}
        pagination={pagination}
        onPaginationChange={setPagination}
        onRowClick={baixar}
        noDataLabel="Nenhum boleto encontrado."
      />
    </Box>
  )
}
