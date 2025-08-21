import { writable } from "svelte/store";
import type { UserResponse } from "@/types/dtos/user";
import { getLocalStorageItem, removeLocalStorageItem, setLocalStorageItem } from "@/utils/local-storage-util";

// Token store with localStorage
export const tokenStore = (() => {
    const key = "token";
    const { subscribe, set } = writable<string | null>(getLocalStorageItem<string | null>(key, null));

    return {
        subscribe,
        set: (value: string | null) => {
            set(value);
            if (value) setLocalStorageItem(key, value);
            else removeLocalStorageItem(key);
        },
        remove: () => {
            set(null);
            removeLocalStorageItem(key);
        },
        get: () => {
            let value: string | null = null;
            subscribe(v => (value = v))();
            return value;
        }
    };
})();

// User store with localStorage
export const userStore = (() => {
    const key = "user";
    const { subscribe, set } = writable<UserResponse | null>(getLocalStorageItem<UserResponse | null>(key, null));

    return {
        subscribe,
        set: (value: UserResponse | null) => {
            set(value);
            if (value) setLocalStorageItem(key, value);
            else removeLocalStorageItem(key);
        },
        remove: () => {
            set(null);
            removeLocalStorageItem(key);
        },
        get: () => {
            let value: UserResponse | null = null;
            subscribe(v => (value = v))();
            return value;
        }
    };
})();

// Oobe status store with localStorage
export const oobeStatusStore = (() => {
    const key = "oobe";
    const { subscribe, set } = writable<boolean | null>(getLocalStorageItem<boolean | null>(key, null));

    return {
        subscribe,
        set: (value: boolean | null) => {
            set(value);
            if (value !== null) setLocalStorageItem(key, value);
            else removeLocalStorageItem(key);
        },
        remove: () => {
            set(null);
            removeLocalStorageItem(key);
        },
        get: () => {
            let value: boolean | null = null;
            subscribe(v => (value = v))();
            return value;
        }
    };
})();

// Helper for clearing all auth info
export function clearAuthInfo() {
    tokenStore.remove();
    userStore.remove();
}
