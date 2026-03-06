import { ref } from 'vue';

export type SortDir = 'asc' | 'desc';
export type SortItem = { field: string; dir: SortDir };

type Options = {
    /** Chave para persistir no localStorage (opcional) */
    storageKey?: string;
    /** Persiste no localStorage? default: true */
    persist?: boolean;
};

export function useTriStateMultiSort(options: Options = {}) {
    const { storageKey, persist = true } = options;

    const sortState = ref<SortItem[]>([]);

    const orderIndexOf = (field: string) => sortState.value.findIndex(s => s.field === field);
    const dirOf = (field: string): SortDir | null => sortState.value.find(s => s.field === field)?.dir ?? null;

    // 1º clique asc, 2º desc, 3º remove (mesmo se for a última)
    function cycleSort(field: string) {
        const idx = orderIndexOf(field);
        if (idx === -1) {
            sortState.value.push({ field, dir: 'asc' });
        } else {
            const current = sortState.value[idx];
            if (current?.dir === 'asc') {
                sortState.value[idx] = { field, dir: 'desc' };
            } else {
                sortState.value.splice(idx, 1); // remove
            }
        }
        saveToStorage();
    }

    function removeSort(field: string) {
        const idx = orderIndexOf(field);
        if (idx > -1) {
            sortState.value.splice(idx, 1);
            saveToStorage();
        }
    }

    /** Limpa toda ordenação (fica sem sort) */
    function clearSort() {
        sortState.value = [];
        saveToStorage();
    }

    /** Monta ?sort=campo,asc&sort=outro,desc (Spring Data) */
    function buildSortQuery(): string {
        if (!sortState.value.length) return '';
        const parts = sortState.value.map(s => `sort=${encodeURIComponent(s.field)},${s.dir}`);
        return `&${parts.join('&')}`;
    }

    /** Inicializa a partir do storage (se houver) */
    function init() {
        if (storageKey && persist) {
            try {
                const raw = localStorage.getItem(storageKey);
                if (raw) {
                    const parsed = JSON.parse(raw) as SortItem[];
                    if (Array.isArray(parsed)) {
                        sortState.value = parsed.filter(s => s?.field && s?.dir);
                    }
                }
            } catch (e) {
                console.error(e)
            }
        }
    }

    function saveToStorage() {
        if (!storageKey || !persist) return;
        try {
            localStorage.setItem(storageKey, JSON.stringify(sortState.value));
        } catch (e) {
            console.error(e)
        }
    }

    return {
        sortState,
        dirOf,
        orderIndexOf,
        cycleSort,
        removeSort,
        clearSort,
        buildSortQuery,
        init
    };
}
