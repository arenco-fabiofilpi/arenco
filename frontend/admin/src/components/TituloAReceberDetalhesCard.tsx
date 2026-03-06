import { Box, Card, CardContent, CardActions, Divider, Grid, IconButton, Tooltip, Typography, Button, CircularProgress } from '@mui/material'
import ContentCopyIcon from '@mui/icons-material/ContentCopy'
import PictureAsPdfIcon from '@mui/icons-material/PictureAsPdf'
import ImageIcon from '@mui/icons-material/Image'
import { useSnackbar } from 'notistack'
import { useMemo } from 'react'
import { FormatoBoleto, useBoletoDownloader } from '../hooks/useBoletoDownloader'

type Titulo = Record<string, unknown>

const FIELD_MAP = [
  { label: 'Empresa', key: 'empresa', type: 'string' },
  { label: 'Cliente', key: 'cliente', type: 'string' },
  { label: 'Nome Cliente', key: 'nomeClte', type: 'string' },
  { label: 'CNPJ/CPF Cliente', key: 'cnpjCpfDoCliente', type: 'string' },
  { label: 'Número Doc', key: 'numeDoc', type: 'string' },
  { label: 'Sequência', key: 'sequencia', type: 'string' },
  { label: 'Sequência Reparcela', key: 'sequenciaReparcela', type: 'string' },
  { label: 'Lote', key: 'lote', type: 'string' },
  { label: 'Quadra', key: 'quadra', type: 'string' },
  { label: 'Data Emissão', key: 'dtEmissao', type: 'string' },
  { label: 'Data Base', key: 'dataBase', type: 'string' },
  { label: 'Data Hoje', key: 'dataDeHoje', type: 'string' },
  { label: 'Data Reparcela', key: 'dataReparcela', type: 'string' },
  { label: 'Centro de Custo', key: 'ccusto', type: 'string' },
  { label: 'Nome Centro de Custo', key: 'nomeCcusto', type: 'string' },
  { label: 'Unidade de Custo', key: 'unidadeDeCusto', type: 'string' },
  { label: 'Fatura', key: 'fatura', type: 'string' },
  { label: 'Número Fatura', key: 'numeFatura', type: 'string' },
  { label: 'Vencimento', key: 'vencimento', type: 'string' },
  { label: 'Valor', key: 'valor', type: 'string' },
  { label: 'Tipo Doc', key: 'tipoDoc', type: 'string' },
  { label: 'Série', key: 'serie', type: 'string' },
  { label: 'Observação', key: 'observacao', type: 'string' },
  { label: 'Saldo', key: 'saldo', type: 'string' },
  { label: 'Receita', key: 'receita', type: 'string' },
  { label: 'Denominação', key: 'denominacao', type: 'string' },
  { label: 'Moeda', key: 'moeda', type: 'string' },
  { label: 'Descrição Moeda', key: 'descricaoMoeda', type: 'string' },
  { label: 'Quantidade Moeda', key: 'qtdeMoeda', type: 'number' },
  { label: 'Saldo Qtde Moeda', key: 'saldoQtdeMoeda', type: 'number' },
  { label: 'Valor Corrigido Vencimento', key: 'valorCorrigidoVencimento', type: 'string' },
  { label: 'Valor Corrigido Hoje', key: 'valorCorrigidoHoje', type: 'string' },
  { label: 'Valor Corrigido Data Ref', key: 'valorCorrigidoDataRef', type: 'string' },
  { label: 'Código Portador', key: 'codigoPortador', type: 'string' },
  { label: 'Tem Cobrança Boleto', key: 'temCobrancaBoleto', type: 'boolean' },
  { label: 'Situação Jurídica', key: 'indicacaoSituacaoJuridica', type: 'string' },
  { label: 'Parcelas Liberadas Após', key: 'parcelasLiberadaApos', type: 'string' },
  { label: 'Tipo Correção Contrato', key: 'tipoCorrecaoContrato', type: 'string' },
  { label: 'Taxa Juros Valor Presente', key: 'taxaJurosValorPresente', type: 'percent' },
  { label: 'Saldo Valor Presente', key: 'saldoValorPresente', type: 'string' },
  { label: 'Seguro Embutido', key: 'seguroEmbutidoParcela', type: 'boolean' },
  { label: 'Percentual Seguro', key: 'percentualSeguro', type: 'percent' },
  { label: 'Código Cobrança', key: 'codigoCobranca', type: 'string' },
  { label: 'Total Reparcela', key: 'totalReparcela', type: 'string' },
] as const

const numberFmt = new Intl.NumberFormat('pt-BR')

function formatValue(f: (typeof FIELD_MAP)[number], titulo: Titulo): string {
  const v = titulo[f.key]
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

export function TituloAReceberDetalhesCard({ titulo }: { titulo: Titulo | null }) {
  const { enqueueSnackbar } = useSnackbar()
  const { baixarBoleto, carregandoPdf, carregandoPng, errorMessage } = useBoletoDownloader()

  const visibleFields = useMemo(
    () =>
      titulo
        ? FIELD_MAP.filter(f => {
            const v = titulo[f.key]
            return v !== undefined && v !== null && String(v) !== ''
          })
        : [],
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
        <Typography variant="h6">Dados do Título</Typography>
        <Typography variant="caption" color="text.secondary">
          Informações principais e financeiras
        </Typography>
        <Divider sx={{ my: 2 }} />
        <Grid container spacing={2}>
          {visibleFields.map(f => {
            const text = formatValue(f, titulo)
            return (
              <Grid item xs={12} sm={6} lg={4} key={f.key}>
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
      <CardActions sx={{ justifyContent: 'flex-end', gap: 1 }}>
        {titulo.pdfId && (
          <Button
            variant="contained"
            startIcon={carregandoPdf ? <CircularProgress size={16} color="inherit" /> : <PictureAsPdfIcon />}
            disabled={carregandoPdf}
            onClick={() => baixarBoleto(String(titulo.pdfId), FormatoBoleto.PDF)}
          >
            Baixar Boleto PDF
          </Button>
        )}
        {titulo.pngId && (
          <Button
            variant="contained"
            startIcon={carregandoPng ? <CircularProgress size={16} color="inherit" /> : <ImageIcon />}
            disabled={carregandoPng}
            onClick={() => baixarBoleto(String(titulo.pngId), FormatoBoleto.PNG)}
          >
            Baixar Boleto PNG
          </Button>
        )}
      </CardActions>
      {errorMessage && (
        <Typography color="error" align="center" sx={{ pb: 1 }}>
          {errorMessage}
        </Typography>
      )}
    </Card>
  )
}
