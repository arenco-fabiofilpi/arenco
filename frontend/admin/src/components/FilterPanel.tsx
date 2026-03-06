import {
  Box,
  Card,
  CardContent,
  Typography,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  TextField,
  Button,
  Chip,
  Stack,
  RadioGroup,
  FormControlLabel,
  Radio,
  Collapse,
} from '@mui/material'

export interface FilterItem {
  key: string
  value: string
  operator: string
}

interface FilterPanelProps {
  open: boolean
  fieldOptions: string[]
  operatorOptions: readonly string[]
  filters: FilterItem[]
  joinOperator: 'AND' | 'OR'
  currentFilter: FilterItem
  onCurrentFilterChange: (f: FilterItem) => void
  onAddFilter: () => void
  onRemoveFilter: (index: number) => void
  onJoinOperatorChange: (op: 'AND' | 'OR') => void
  onSearch: () => void
  onClear: () => void
}

export function FilterPanel({
  open,
  fieldOptions,
  operatorOptions,
  filters,
  joinOperator,
  currentFilter,
  onCurrentFilterChange,
  onAddFilter,
  onRemoveFilter,
  onJoinOperatorChange,
  onSearch,
  onClear,
}: FilterPanelProps) {
  return (
    <Collapse in={open}>
      <Card variant="outlined" sx={{ mb: 2 }}>
        <CardContent>
          <Typography variant="h6" gutterBottom>
            Filtros
          </Typography>
          <Box sx={{ display: 'flex', gap: 2, flexWrap: 'wrap', alignItems: 'flex-end', mb: 2 }}>
            <FormControl size="small" sx={{ minWidth: 160 }}>
              <InputLabel>Campo</InputLabel>
              <Select
                value={currentFilter.key}
                label="Campo"
                onChange={e => onCurrentFilterChange({ ...currentFilter, key: e.target.value })}
              >
                {fieldOptions.map(opt => (
                  <MenuItem key={opt} value={opt}>
                    {opt}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>

            <FormControl size="small" sx={{ minWidth: 140 }}>
              <InputLabel>Operador</InputLabel>
              <Select
                value={currentFilter.operator}
                label="Operador"
                onChange={e => onCurrentFilterChange({ ...currentFilter, operator: e.target.value })}
              >
                {operatorOptions.map(opt => (
                  <MenuItem key={opt} value={opt}>
                    {opt}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>

            <TextField
              size="small"
              label="Valor"
              value={currentFilter.value}
              onChange={e => onCurrentFilterChange({ ...currentFilter, value: e.target.value })}
              sx={{ minWidth: 180 }}
            />

            <Button variant="contained" onClick={onAddFilter}>
              Adicionar
            </Button>
          </Box>

          <Typography variant="subtitle2" gutterBottom>
            Filtros Aplicados
          </Typography>
          {filters.length === 0 ? (
            <Typography variant="body2" color="text.secondary">
              Nenhum filtro aplicado
            </Typography>
          ) : (
            <Stack direction="row" flexWrap="wrap" gap={1} sx={{ mb: 1 }}>
              {filters.map((f, i) => (
                <Chip
                  key={i}
                  label={`${f.key} ${f.operator} "${f.value}"`}
                  onDelete={() => onRemoveFilter(i)}
                  size="small"
                />
              ))}
            </Stack>
          )}

          <Box sx={{ mt: 1 }}>
            <Typography variant="caption" color="text.secondary">
              Combinar filtros por:
            </Typography>
            <RadioGroup
              row
              value={joinOperator}
              onChange={e => onJoinOperatorChange(e.target.value as 'AND' | 'OR')}
            >
              <FormControlLabel value="AND" control={<Radio size="small" />} label="E" />
              <FormControlLabel value="OR" control={<Radio size="small" />} label="OU" />
            </RadioGroup>
          </Box>

          <Box sx={{ display: 'flex', justifyContent: 'flex-end', gap: 1, mt: 1 }}>
            <Button onClick={onClear} color="secondary">
              Limpar
            </Button>
            <Button variant="contained" onClick={onSearch}>
              Buscar
            </Button>
          </Box>
        </CardContent>
      </Card>
    </Collapse>
  )
}
