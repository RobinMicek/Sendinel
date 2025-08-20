import { writable } from "svelte/store";
import { UnauthenticatedError } from "@/exceptions/unauthenticated-error";
import type { UserResponse } from "@/types/dtos/user";

// Token store
export const tokenStore = (() => {
    const { subscribe, set } = writable<string | null>(null);
    return {
        subscribe,
        set,
        remove: () => set(null),
        get: () => {
            let value: string | null = null;
            tokenStore.subscribe((v: string | null) => { value = v; })();
            if (value) return value;
            return null
        }
    };
})();

// User store
export const userStore = (() => {
    const { subscribe, set } = writable<UserResponse | null>(null);
    return {
        subscribe,
        set,
        remove: () => set(null),
        get: () => {
            let value: UserResponse | null = null;
            userStore.subscribe((v: UserResponse | null) => { value = v; })();
            if (value) return value;
            return null
        }
    };
})();

// Helper for clearing all auth info
export function clearAuthInfo() {
    tokenStore.remove();
    userStore.remove();
}
