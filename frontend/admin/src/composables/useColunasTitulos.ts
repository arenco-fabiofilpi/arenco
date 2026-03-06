export function useColunasTitulosAReceber() {
  return [
    { name: 'vencimento', align: 'left' as const, label: 'Vencimento', field: 'vencimento' },
    { name: 'numeDoc', align: 'left' as const, label: 'Contrato', field: 'numeDoc' },
    { name: 'sequencia', align: 'left' as const, label: 'Sequência', field: 'sequencia' },
    { name: 'cliente', align: 'left' as const, label: 'Cliente', field: 'cliente' },
    { name: 'nomeClte', align: 'left' as const, label: 'Nome do Cliente', field: 'nomeClte' },
    { name: 'empresa', align: 'left' as const, label: 'Empresa', field: 'empresa' },
    { name: 'valor', align: 'left' as const, label: 'Valor', field: 'valor' },
  ]
}

export function useColunasTitulosRecebidos() {
  return [
    { name: 'numeDoc', align: 'left' as const, label: 'Contrato', field: 'numeDoc' },
    { name: 'sequencia', align: 'left' as const, label: 'Sequência', field: 'sequencia' },
    { name: 'cliente', align: 'left' as const, label: 'Cliente', field: 'cliente' },
    { name: 'nomeClte', align: 'left' as const, label: 'Nome do Cliente', field: 'nomeClte' },
    { name: 'empresa', align: 'left' as const, label: 'Empresa', field: 'empresa' },
    { name: 'valor', align: 'left' as const, label: 'Valor', field: 'valor' },
    { name: 'dataDeposito', align: 'left' as const, label: 'Data Depósito', field: 'dtDeposito' },
    {
      name: 'numeDeposito',
      align: 'left' as const,
      label: 'Número Depósito',
      field: 'numeDeposito',
    },
  ]
}
