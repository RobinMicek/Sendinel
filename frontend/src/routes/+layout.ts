import { redirect } from '@sveltejs/kit';
import { oobeStatusStore, tokenStore, userStore } from '@/stores/store-factory';
import { appSettingsStore } from '@/stores/store-factory.js';
import AuthService from '@/services/auth-service';
import AppSettingsService from '@/services/app-settings-service';
import { browser } from '$app/environment';
import { isJwtExpired } from '@/utils/jwt-util.js';

const authService = new AuthService();
const appSettingsService = new AppSettingsService();

export async function load({ url }) {
    const path = url.pathname;

    if (!browser) {
        // On server: do nothing (let client-side takeover)
        return {};
    }

    // 1. Ensure OOBE status is cached
    if (oobeStatusStore.get() == null) {
        try {
            const response = await authService.oobeStatus();
            oobeStatusStore.set(response.oobeStatus);
        } catch {
            console.error("Failed to get oobe status");
        }
    }
    
    // 2. If OOBE not completed → force /auth/oobe (but avoid loop)
    if (oobeStatusStore.get() && path !== "/auth/oobe") {
        throw redirect(307, "/auth/oobe");
    }
    
    // 3. If missing token/user or token expired → force /auth (but avoid loop)
    const token = tokenStore.get();
    const user = userStore.get();
    if ((token == null || user == null || isJwtExpired(token)) && !path.startsWith("/auth")) {
        throw redirect(307, "/auth");
    }
    
    // 4. Ensure app settings are cached - needs to be after successful auth
    if (appSettingsStore.get() == null) {
        try {
            const response = await appSettingsService.getSettings();
            appSettingsStore.set(response);
        } catch {
            console.error("Failed to get app settings");
        }
    }
    
    // 5. Redirect / → /dashboard
    if (path === "/") {
        throw redirect(307, "/dashboard");
    }
    
    return {};
}
