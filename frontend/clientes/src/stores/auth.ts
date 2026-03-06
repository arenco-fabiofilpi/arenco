import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useAuthStore = defineStore('auth', () => {
    const pendingUsername = ref<string>('');

    function setPendingUsername(u: string) {
        pendingUsername.value = u?.trim();
    }
    function clearPendingUsername() {
        pendingUsername.value = '';
    }

    return { pendingUsername, setPendingUsername, clearPendingUsername };
});
