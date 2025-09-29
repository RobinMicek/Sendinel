import { writable, get as getStoreValue } from "svelte/store";
import { getLocalStorageItem, removeLocalStorageItem, setLocalStorageItem } from "@/utils/local-storage-util";
import type { UserResponse } from "@/types/dtos/user";
import type { AppSettingsResponse } from "@/types/dtos/app-settings";
import { APP_LATEST_RELEASE_CACHE_EXPIRATION, APP_SETTINGS_CACHE_EXPIRATION, DEFAULT_DATATABLE_PAGE_SIZE, LAST_VISITED_PAGE_CACHE_EXPIRATION } from "@/config";

/**
 * Create a persistent Svelte store backed by localStorage.
 *
 * @param key - localStorage key
 * @param initial - default value if nothing is stored
 * @param maxAgeMs - optional maximum age in ms. If exceeded, the stored value is invalidated.
 */
function persistentStore<T>(key: string, initial: T | null, maxAgeMs?: number) {
    // Loader with expiration check
    function load(): T | null {
        if (maxAgeMs) {
            const data = getLocalStorageItem<{ value: T; timestamp: number } | null>(key, null);
            if (!data) return initial;

            const now = Date.now();
            if (now - data.timestamp > maxAgeMs) {
                removeLocalStorageItem(key);
                return initial;
            }
            return data.value;
        } else {
            return getLocalStorageItem<T | null>(key, initial);
        }
    }

    const store = writable<T | null>(load());

    return {
        subscribe: store.subscribe,
        set: (value: T | null) => {
            store.set(value);
            if (value !== null) {
                if (maxAgeMs) {
                    setLocalStorageItem(key, { value, timestamp: Date.now() });
                } else {
                    setLocalStorageItem(key, value);
                }
            } else {
                removeLocalStorageItem(key);
            }
        },
        remove: () => {
            store.set(null);
            removeLocalStorageItem(key);
        },
        get: (): T | null => {
            // Check in-memory first
            let value = getStoreValue(store);

            if (value === null) {
                value = load();
                if (value !== null) {
                    store.set(value); // rehydrate memory
                }
            }
            return value;
        }
    };
}

// Token store
export const tokenStore = persistentStore<string>("token", null);

// User store
export const userStore = persistentStore<UserResponse>("user", null);

// OOBE status store
export const oobeStatusStore = persistentStore<boolean>("oobe", null);

// App settings store
export const appSettingsStore = persistentStore<AppSettingsResponse>("app-settings", null, APP_SETTINGS_CACHE_EXPIRATION);

// Latest release store
export const latestReleaseStore = persistentStore<LatestRelease>("latest-release", null, APP_LATEST_RELEASE_CACHE_EXPIRATION);

// Prefered datatable page size store
export const preferedDatatablePageSizeStore = persistentStore<number>("prefered-datatable-page-size", DEFAULT_DATATABLE_PAGE_SIZE);

// Last visited page store
export const lastVisitedPageStore = persistentStore<String>("last-visited-page", null, LAST_VISITED_PAGE_CACHE_EXPIRATION);

// Clear all
export function clearAuthInfo() {
    tokenStore.remove();
    userStore.remove();
}
