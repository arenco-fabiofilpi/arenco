import { BrowserRouter } from 'react-router-dom'
import { ThemeProvider, createTheme, CssBaseline } from '@mui/material'
import { SnackbarProvider } from 'notistack'
import { AppRouter } from './router'

const theme = createTheme({
  palette: {
    primary: { main: '#1565C0' },
    secondary: { main: '#6a1b9a' },
  },
  typography: {
    fontFamily: 'Roboto, sans-serif',
  },
})

export default function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <SnackbarProvider maxSnack={3} anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}>
        <BrowserRouter>
          <AppRouter />
        </BrowserRouter>
      </SnackbarProvider>
    </ThemeProvider>
  )
}
