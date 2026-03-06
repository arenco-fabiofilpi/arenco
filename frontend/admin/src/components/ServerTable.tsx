import {
  Table,
  TableHead,
  TableBody,
  TableRow,
  TableCell,
  TablePagination,
  TableContainer,
  Paper,
  LinearProgress,
  Box,
  Typography,
  Checkbox,
} from '@mui/material'
import ArrowUpwardIcon from '@mui/icons-material/ArrowUpward'
import ArrowDownwardIcon from '@mui/icons-material/ArrowDownward'
import type { SortDir } from '../hooks/useTriStateMultiSort'

export interface ColumnDef {
  field: string
  headerName: string
  sortable?: boolean
  align?: 'left' | 'center' | 'right'
  width?: number | string
  renderCell?: (row: Record<string, unknown>) => React.ReactNode
}

export interface PaginationState {
  page: number
  rowsPerPage: number
  rowsNumber: number
}

interface ServerTableProps {
  rows: Record<string, unknown>[]
  columns: ColumnDef[]
  rowKey?: string
  loading?: boolean
  pagination: PaginationState
  onPaginationChange: (p: PaginationState) => void
  dirOf?: (field: string) => SortDir | null
  onSort?: (field: string) => void
  onRowClick?: (row: Record<string, unknown>) => void
  selection?: boolean
  selectedIds?: Set<unknown>
  onToggleSelect?: (id: unknown) => void
  allPageSelected?: boolean
  somePageSelected?: boolean
  onToggleSelectAllPage?: (val: boolean) => void
  noDataLabel?: string
  rowsPerPageOptions?: number[]
}

export function ServerTable({
  rows,
  columns,
  rowKey = 'id',
  loading = false,
  pagination,
  onPaginationChange,
  dirOf,
  onSort,
  onRowClick,
  selection,
  selectedIds,
  onToggleSelect,
  allPageSelected,
  somePageSelected,
  onToggleSelectAllPage,
  noDataLabel = 'Nenhum dado encontrado.',
  rowsPerPageOptions = [10, 20, 50],
}: ServerTableProps) {
  return (
    <TableContainer component={Paper} variant="outlined">
      {loading && <LinearProgress />}
      <Table size="small">
        <TableHead>
          <TableRow sx={{ bgcolor: 'grey.50' }}>
            {selection && (
              <TableCell padding="checkbox">
                <Checkbox
                  indeterminate={somePageSelected && !allPageSelected}
                  checked={!!allPageSelected}
                  onChange={e => onToggleSelectAllPage?.(e.target.checked)}
                />
              </TableCell>
            )}
            {columns.map(col => {
              const isSortable = col.sortable !== false && !!onSort
              const dir = dirOf?.(col.field) ?? null
              return (
                <TableCell
                  key={col.field}
                  align={col.align ?? 'left'}
                  width={col.width}
                  onClick={isSortable ? () => onSort(col.field) : undefined}
                  sx={{
                    cursor: isSortable ? 'pointer' : 'default',
                    whiteSpace: 'nowrap',
                    fontWeight: 600,
                    userSelect: 'none',
                  }}
                >
                  <Box sx={{ display: 'inline-flex', alignItems: 'center', gap: 0.5 }}>
                    {col.headerName}
                    {isSortable && dir === 'asc' && <ArrowUpwardIcon sx={{ fontSize: 16 }} />}
                    {isSortable && dir === 'desc' && <ArrowDownwardIcon sx={{ fontSize: 16 }} />}
                    {isSortable && !dir && <Box sx={{ width: 16, display: 'inline-block' }} />}
                  </Box>
                </TableCell>
              )
            })}
          </TableRow>
        </TableHead>
        <TableBody>
          {rows.length === 0 && !loading ? (
            <TableRow>
              <TableCell colSpan={columns.length + (selection ? 1 : 0)} align="center" sx={{ py: 4 }}>
                <Typography color="text.secondary">{noDataLabel}</Typography>
              </TableCell>
            </TableRow>
          ) : (
            rows.map((row, i) => {
              const id = row[rowKey] ?? i
              const isSelected = selectedIds?.has(id) ?? false
              return (
                <TableRow
                  key={String(id)}
                  hover={!!onRowClick}
                  selected={isSelected}
                  onClick={onRowClick ? () => onRowClick(row) : undefined}
                  sx={{ cursor: onRowClick ? 'pointer' : 'default' }}
                >
                  {selection && (
                    <TableCell padding="checkbox">
                      <Checkbox
                        checked={isSelected}
                        onClick={e => e.stopPropagation()}
                        onChange={() => onToggleSelect?.(id)}
                      />
                    </TableCell>
                  )}
                  {columns.map(col => (
                    <TableCell key={col.field} align={col.align ?? 'left'}>
                      {col.renderCell ? col.renderCell(row) : String(row[col.field] ?? '')}
                    </TableCell>
                  ))}
                </TableRow>
              )
            })
          )}
        </TableBody>
      </Table>
      <TablePagination
        component="div"
        count={pagination.rowsNumber}
        page={pagination.page - 1}
        rowsPerPage={pagination.rowsPerPage}
        rowsPerPageOptions={rowsPerPageOptions}
        onPageChange={(_, page) => onPaginationChange({ ...pagination, page: page + 1 })}
        onRowsPerPageChange={e =>
          onPaginationChange({ ...pagination, rowsPerPage: parseInt(e.target.value), page: 1 })
        }
        labelRowsPerPage="Linhas por página:"
        labelDisplayedRows={({ from, to, count }) => `${from}–${to} de ${count}`}
      />
    </TableContainer>
  )
}
