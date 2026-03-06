import { Box, Card, CardContent, Divider, Grid, IconButton, Tooltip, Typography } from '@mui/material'
import ContentCopyIcon from '@mui/icons-material/ContentCopy'
import { useSnackbar } from 'notistack'
import { useMemo } from 'react'

type Titulo = Record<string, unknown>

type Field =
  | { label: string; key: string; type?: 'currency' | 'number' | 'percent' | 'boolean' }
  | { label: string; keys: string[]; type?: 'currency' | 'number' | 'percent' | 'boolean' }

const FIELD_MAP: Field[] = [
  { label: 'Empresa', key: 'empresa' },
  { label: 'Cliente', key: 'cliente' },
  { label: 'Nome Cliente', key: 'nomeClte' },
  { label: 'Centro de Custo', keys: ['ccusto', 'centroCusto'] },
  { label: 'Nome Centro de Custo', key: 'nomeCcusto' },
  { label: 'Número Doc', key: 'numeDoc' },
  { label: 'Sequência', key: 'sequencia' },
  { label: 'Sequência Reparcela', key: 'sequenciaReparcela' },
  { label: 'Lote', key: 'lote' },
  { label: 'Lote Loteamento', key: 'loteLoteamento' },
  { label: 'Quadra', key: 'quadra' },
  { label: 'Data Base', key: 'dataBase' },
  { label: 'Data Emissão', key: 'dtEmissao' },
  { label: 'Data Depósito', key: 'dtDeposito' },
  { label: 'Data Gravação', key: 'dataGravacao' },
  { label: 'Data Reparcela', key: 'dataReparcela' },
  { label: 'Vencimento', key: 'vencimento' },
  { label: 'Valor', key: 'valor', type: 'currency' },
  { label: 'Valor Pago', key: 'valorPago', type: 'currency' },
  { label: 'Valor Depósito', key: 'valorDeposito', type: 'currency' },
  { label: 'Valor Juros', key: 'valorJuros', type: 'currency' },
  { label: 'Valor Desconto', key: 'valorDesc', type: 'currency' },
  { label: 'Valor Multa', key: 'valorMulta', type: 'currency' },
  { label: 'Valor VM', key: 'valorVm', type: 'currency' },
  { label: 'Valor Seguro', key: 'valorSeguro', type: 'currency' },
  { label: 'Tipo Doc', key: 'tipoDoc' },
  { label: 'Série', key: 'serie' },
  { label: 'Moeda', key: 'moeda' },
  { label: 'Fatura', key: 'fatura' },
  { label: 'Número Fatura', key: 'numeFatura' },
  { label: 'Número Depósito', key: 'numeDeposito' },
  { label: 'Denominação', key: 'denominacao' },
  { label: 'Receita', key: 'receita' },
  { label: 'Tipo Depósito', key: 'tipoDeposito' },
  { label: 'Descrição Tipo Depósito', key: 'descricaoTipoDeposito' },
  { label: 'Tipo Baixa', key: 'tipoBaixa' },
  { label: 'Descrição Tipo Baixa', key: 'descricaoTipoBaixa' },
  { label: 'Dias Atraso', key: 'diasAtraso', type: 'number' },
  { label: 'Banco', key: 'banco' },
  { label: 'Código Banco', key: 'codigoBanco' },
  { label: 'Nome Banco', key: 'nomeBanco' },
  { label: 'Agência', key: 'agencia' },
  { label: 'Conta', key: 'conta' },
  { label: 'Unidade de Custo', key: 'unidadeDeCusto' },
  { label: 'Quadra Original', key: 'quadraOriginal' },
  { label: 'Lote Original', key: 'loteOriginal' },
  { label: 'Unidade Custo Original', key: 'unidadeDeCustoOriginal' },
  { label: 'Total Reparcela', key: 'totalReparcela', type: 'currency' },
  { label: 'Observação', key: 'observacao' },
  { label: 'Observação Contrato', key: 'observacaoContrato' },
]

const brCurrency = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' })
const numberFmt = new Intl.NumberFormat('pt-BR')

function firstNonEmpty(...vals: unknown[]) {
  for (const v of vals) {
    if (v !== undefined && v !== null && String(v) !== '') return v
  }
  return undefined
}

function getRawValue(f: Field, titulo: Titulo) {
  if ('key' in f) return titulo[f.key]
  return firstNonEmpty(...f.keys.map(k => titulo[k]))
}

function formatValue(f: Field, titulo: Titulo): string {
  const v = getRawValue(f, titulo)
  switch (f.type) {
    case 'currency':
      return isFinite(+(v as number)) ? brCurrency.format(+(v as number)) : v != null ? String(v) : ''
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

export function TituloRecebidoDetalhesCard({ titulo }: { titulo: Titulo | null }) {
  const { enqueueSnackbar } = useSnackbar()

  const visibleFields = useMemo(
    () => (titulo ? FIELD_MAP.filter(f => getRawValue(f, titulo) !== undefined) : []),
    [titulo],
  )

  function copy(text: string) {
    navigator.clipboard.writeText(text)
    enqueueSnackbar('Copiado!', { variant: 'success', autoHideDuration: 1200 })
  }

  if (!titulo) return null

  return (
    <Card variant="outlined">
      <CardContent>
        <Typography variant="h6">Detalhes do Título Recebido</Typography>
        <Typography variant="caption" color="text.secondary">
          Informações principais e financeiras
        </Typography>
        <Divider sx={{ my: 2 }} />
        <Grid container spacing={2}>
          {visibleFields.map(f => {
            const text = formatValue(f, titulo)
            const label = f.label
            return (
              <Grid item xs={12} sm={6} lg={4} key={label}>
                <Box sx={{ bgcolor: 'grey.50', borderRadius: 2, p: 1 }}>
                  <Typography variant="caption" color="text.secondary" display="block">
                    {label}
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
