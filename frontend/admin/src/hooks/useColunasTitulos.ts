import type { ColumnDef } from '../components/ServerTable'

export function getColunasTitulosAReceber(): ColumnDef[] {
  return [
    { field: 'vencimento', headerName: 'Vencimento' },
    { field: 'numeDoc', headerName: 'Contrato' },
    { field: 'sequencia', headerName: 'Sequência' },
    { field: 'cliente', headerName: 'Cliente' },
    { field: 'nomeClte', headerName: 'Nome do Cliente' },
    { field: 'empresa', headerName: 'Empresa' },
    { field: 'valor', headerName: 'Valor' },
  ]
}

export function getColunasTitulosRecebidos(): ColumnDef[] {
  return [
    { field: 'numeDoc', headerName: 'Contrato' },
    { field: 'sequencia', headerName: 'Sequência' },
    { field: 'cliente', headerName: 'Cliente' },
    { field: 'nomeClte', headerName: 'Nome do Cliente' },
    { field: 'empresa', headerName: 'Empresa' },
    { field: 'valor', headerName: 'Valor' },
    { field: 'dtDeposito', headerName: 'Data Depósito' },
    { field: 'numeDeposito', headerName: 'Número Depósito' },
  ]
}
