import { ref } from 'vue'
import { apiClientes } from 'src/boot/axios'
import type { AxiosError } from 'axios'

export enum FormatoBoleto {
  PDF = 'pdf',
  PNG = 'png',
}

function extrairFilename(contentDisposition?: string | null, fallback = 'arquivo') {
  if (!contentDisposition) return fallback
  // tenta filename*=UTF-8''nome.ext (RFC 5987)
  const utf8 = /filename\*\s*=\s*UTF-8''([^;]+)/i.exec(contentDisposition)
  if (utf8?.[1]) return decodeURIComponent(utf8[1])

  // tenta filename="nome.ext"
  const ascii = /filename\s*=\s*"([^"]+)"/i.exec(contentDisposition)
  if (ascii?.[1]) return ascii[1]

  // tenta filename=nome.ext
  const plain = /filename\s*=\s*([^;]+)/i.exec(contentDisposition)
  if (plain?.[1]) return plain[1].trim()

  return fallback
}

export function useBoletoDownloader() {
  const carregandoPdf = ref(false)
  const carregandoPng = ref(false)
  const errorMessage = ref('');

  async function baixarBoleto(boletoId: string, formato: FormatoBoleto) {
    if (!boletoId) return

    const carregandoRef = formato === FormatoBoleto.PDF ? carregandoPdf : carregandoPng
    carregandoRef.value = true

    try {
      const url = `/clientes-api/v1/admin/boletos/${encodeURIComponent(boletoId)}`
      const response = await apiClientes.get(url, {
        responseType: 'blob', // <- importante!
      })

      const contentType = response.headers['content-type'] || (formato === FormatoBoleto.PDF ? 'application/pdf' : 'image/png')
      const contentDisposition = response.headers['content-disposition'] as string | undefined

      const nomeFallback = `boleto-${boletoId}.${formato}`
      const filename = extrairFilename(contentDisposition, nomeFallback)

      const blob = new Blob([response.data], { type: contentType })
      const href = URL.createObjectURL(blob)

      const a = document.createElement('a')
      a.href = href
      a.download = filename
      // se quiser abrir em nova aba quando for inline:
      // a.target = '_blank'
      document.body.appendChild(a)
      a.click()
      a.remove()
      URL.revokeObjectURL(href)
    } catch (err) {
      const error = err as AxiosError<{ message: string }>;
      if (error.status == 404) {
        errorMessage.value = 'Boleto não encontrado';
      }
      console.error(`Erro ao baixar boleto ${formato}:`, error.message);
    } finally {
      carregandoRef.value = false
    }
  }

  return {
    baixarBoleto,
    carregandoPdf,
    carregandoPng,
    errorMessage
  }
}
