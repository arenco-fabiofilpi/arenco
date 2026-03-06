import { useState, useCallback, useRef } from 'react'

export type SortDir = 'asc' | 'desc'
export type SortItem = { field: string; dir: SortDir }

interface Options {
  storageKey?: string
  persist?: boolean
}

export function useTriStateMultiSort(options: Options = {}) {
  const { storageKey, persist = true } = options

  const [sortState, setSortState] = useState<SortItem[]>(() => {
    if (storageKey && persist) {
      try {
        const raw = localStorage.getItem(storageKey)
        if (raw) {
          const parsed = JSON.parse(raw) as SortItem[]
          if (Array.isArray(parsed)) return parsed.filter(s => s?.field && s?.dir)
        }
      } catch {}
    }
    return []
  })

  const sortStateRef = useRef(sortState)
  sortStateRef.current = sortState

  const saveToStorage = useCallback(
    (state: SortItem[]) => {
      if (!storageKey || !persist) return
      try {
        localStorage.setItem(storageKey, JSON.stringify(state))
      } catch {}
    },
    [storageKey, persist],
  )

  const dirOf = useCallback(
    (field: string): SortDir | null =>
      sortStateRef.current.find(s => s.field === field)?.dir ?? null,
    [],
  )

  const cycleSort = useCallback(
    (field: string) => {
      setSortState(prev => {
        const idx = prev.findIndex(s => s.field === field)
        let next: SortItem[]
        if (idx === -1) {
          next = [...prev, { field, dir: 'asc' }]
        } else if (prev[idx]?.dir === 'asc') {
          next = [...prev]
          next[idx] = { field, dir: 'desc' }
        } else {
          next = prev.filter((_, i) => i !== idx)
        }
        saveToStorage(next)
        return next
      })
    },
    [saveToStorage],
  )

  const clearSort = useCallback(() => {
    setSortState([])
    saveToStorage([])
  }, [saveToStorage])

  const buildSortQuery = useCallback((): string => {
    const state = sortStateRef.current
    if (!state.length) return ''
    return '&' + state.map(s => `sort=${encodeURIComponent(s.field)},${s.dir}`).join('&')
  }, [])

  return { sortState, dirOf, cycleSort, clearSort, buildSortQuery }
}
