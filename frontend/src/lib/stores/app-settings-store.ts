import { writable } from "svelte/store";
import { getLocalStorageItem, removeLocalStorageItem, setLocalStorageItem } from "@/utils/local-storage-util";
import type { AppSettingsResponse } from "@/types/dtos/app-settings";
import { APP_SETTINGS_CACHE_EXPIRATION } from "@/config";

export const appSettingsStore = (() => {
    const key = "app-settings";

    // Load value with expiry check
    function load(): AppSettingsResponse | null {
        const data = getLocalStorageItem<{ value: AppSettingsResponse; timestamp: number } | null>(key, null);
        if (!data) return null;

        const now = Date.now();
        if (now - data.timestamp > APP_SETTINGS_CACHE_EXPIRATION) {
            removeLocalStorageItem(key);
            return null;
        }
        return data.value;
    }

    const { subscribe, set } = writable<AppSettingsResponse | null>(load());

    return {
        subscribe,
        set: (value: AppSettingsResponse | null) => {
            set(value);
            if (value) {
                setLocalStorageItem(key, { value, timestamp: Date.now() });
            } else {
                removeLocalStorageItem(key);
            }
        },
        remove: () => {
            set(null);
            removeLocalStorageItem(key);
        },
        get: () => {
            let value: AppSettingsResponse | null = null;
            subscribe(v => (value = v))();
            return value;
        }
    };
})();
