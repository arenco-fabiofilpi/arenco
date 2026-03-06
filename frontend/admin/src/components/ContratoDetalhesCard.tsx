import { Box, Card, CardContent, Divider, Grid, IconButton, Tooltip, Typography } from '@mui/material'
import ContentCopyIcon from '@mui/icons-material/ContentCopy'
import { useSnackbar } from 'notistack'
import { useMemo } from 'react'

type Contrato = Record<string, unknown>

type Field =
  | { label: string; key: string; type?: 'currency' | 'number' | 'percent' | 'boolean' }
  | { label: string; keys: string[]; type?: 'currency' | 'number' | 'percent' | 'boolean' }

const FIELD_MAP: Field[] = [
  { label: 'Número Contrato', key: 'numeContrato' },
  { label: 'Empresa', key: 'empresa' },
  { label: 'Nome da Empresa', key: 'nomeEmpresa' },
  { label: 'CNPJ Empresa', key: 'cnpjEmpresa' },
  { label: 'Cidade Empresa', key: 'cidadeEmpresa' },
  { label: 'Endereço Empresa', key: 'enderecoCompletoEmpresa' },
  { label: 'Cliente', key: 'cliente' },
  { label: 'Nome Cliente', key: 'nomeCliente' },
  { label: 'CPF/CIC Cliente', key: 'cic' },
  { label: 'RG Cliente', key: 'rgCliente' },
  { label: 'Data Nascimento Cliente', key: 'clienteNascimento' },
  { label: 'Endereço Cliente', key: 'enderecoCompletoCliente' },
  { label: 'Cidade Cliente', key: 'cidade' },
  { label: 'Estado Cliente', key: 'estado' },
  { label: 'CEP Cliente', key: 'cepCompleto' },
  { label: 'Telefones Cliente', key: 'telefones' },
  { label: 'Contrato Original', key: 'numeContratoOriginal' },
  { label: 'Contrato Anterior', key: 'numeContratoAnterior' },
  { label: 'Número Contrato Matriz', key: 'numeroContratoMatriz' },
  { label: 'Número Contrato Interno', key: 'numeroContratoInterno' },
  { label: 'Observação Contrato', key: 'observacaoContrato' },
  { label: 'Valor Contrato', key: 'valorContrato' },
  { label: 'Valor Contrato Extenso', key: 'valorContratoExtenso' },
  { label: 'Receita', key: 'receita' },
  { label: 'Denominação', key: 'denominacao' },
  { label: 'Total Saldo em Aberto', key: 'totalSaldoEmAberto' },
  { label: 'Centro de Custo', key: 'ccusto' },
  { label: 'Nome Centro de Custo', key: 'nomeCcusto' },
  { label: 'Unidade de Custo', key: 'unidadeDeCusto' },
  { label: 'Unidade de Custo Original', key: 'unidadeDeCustoOriginal' },
  { label: 'Quadra Original', key: 'quadraOriginal' },
  { label: 'Lote Original', key: 'loteOriginal' },
  { label: 'Data Base', key: 'dataBase' },
  { label: 'Data Emissão', key: 'dtEmissao' },
  { label: 'Data Inclusão', key: 'dataInclusao' },
  { label: 'Data Rescisão', key: 'dataRescisao' },
  { label: 'Data Entrega Unidade', key: 'dataEntregaUnidade' },
  { label: 'Nome Corretor', key: 'nomeCorretor' },
  { label: 'Nome Vendedor', key: 'nomeVendedor' },
  { label: 'Nome Cônjuge', key: 'conjugueNome' },
  { label: 'CPF Cônjuge', key: 'cpfConjugue' },
  { label: 'Descrição', key: 'descricao' },
  { label: 'Tipo Documento', key: 'tipoDoc' },
  { label: 'Tipo Unidade', key: 'tipoUnidade' },
  { label: 'Unidade/Quadra/Lote', key: 'unidadeQuadraLote' },
  { label: 'Usuário Inclusão', key: 'usuarioInclusao' },
  { label: 'Observação Cadastro Cliente', key: 'observacaoCadastroCliente' },
  { label: 'ID', key: 'id' },
]

const numberFmt = new Intl.NumberFormat('pt-BR')

function firstNonEmpty(...vals: unknown[]) {
  for (const v of vals) {
    if (v !== undefined && v !== null && String(v) !== '') return v
  }
  return undefined
}

function getRawValue(f: Field, contrato: Contrato) {
  if ('key' in f) return contrato[f.key]
  return firstNonEmpty(...f.keys.map(k => contrato[k]))
}

function formatValue(f: Field, contrato: Contrato): string {
  const v = getRawValue(f, contrato)
  switch (f.type) {
    case 'number':
      return isFinite(+(v as number)) ? numberFmt.format(+(v as number)) : v != null ? String(v) : ''
    case 'percent':
      return isFinite(+(v as number))
        ? `${numberFmt.format(+(v as number))}%`
        : v != null
          ? String(v)
          : ''
    case 'boolean':
      return v ? 'Sim' : 'Não'
    default:
      return v != null ? String(v) : ''
  }
}

export function ContratoDetalhesCard({ contrato }: { contrato: Contrato | null }) {
  const { enqueueSnackbar } = useSnackbar()

  const visibleFields = useMemo(
    () => (contrato ? FIELD_MAP.filter(f => getRawValue(f, contrato) !== undefined) : []),
    [contrato],
  )

  function copy(text: string) {
    navigator.clipboard.writeText(text)
    enqueueSnackbar('Copiado!', { variant: 'success', autoHideDuration: 1200 })
  }

  if (!contrato) return null

  return (
    <Card variant="outlined">
      <CardContent>
        <Typography variant="h6">Detalhes do Contrato</Typography>
        <Typography variant="caption" color="text.secondary">
          Informações principais e financeiras
        </Typography>
        <Divider sx={{ my: 2 }} />
        <Grid container spacing={2}>
          {visibleFields.map(f => {
            const text = formatValue(f, contrato)
            return (
              <Grid item xs={12} sm={6} lg={4} key={f.label}>
                <Box sx={{ bgcolor: 'grey.50', borderRadius: 2, p: 1 }}>
                  <Typography variant="caption" color="text.secondary" display="block">
                    {f.label}
                  </Typography>
                  <Box sx={{ display: 'flex', alignItems: 'center' }}>
                    <Typography
                      variant="body2"
                      fontWeight={500}
                      sx={{ flex: 1, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}
                    >
                      {text}
                    </Typography>
                    <Tooltip title="Clicar para copiar">
                      <IconButton size="small" onClick={() => copy(text)}>
                        <ContentCopyIcon fontSize="inherit" />
                      </IconButton>
                    </Tooltip>
                  </Box>
                </Box>
              </Grid>
            )
          })}
        </Grid>
      </CardContent>
    </Card>
  )
}
