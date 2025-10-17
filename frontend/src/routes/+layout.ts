import { redirect } from '@sveltejs/kit';
import { lastVisitedPageStore, oobeStatusStore, tokenStore, userStore } from '@/stores/store-factory';
import { appSettingsStore } from '@/stores/store-factory.js';
import AuthService from '@/services/auth-service';
import AppSettingsService from '@/services/app-settings-service';
import { getJwtPayload, isJwtExpired } from '@/utils/jwt-util.js';

export const ssr = false

const authService = new AuthService();
const appSettingsService = new AppSettingsService();

export async function load({ url }) {
    const path = url.pathname;

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

    // 4. If token is present but TOTP is not verified -> force /auth/totp
    const jwtPayload = getJwtPayload(token!)
    if (jwtPayload && jwtPayload.totp === false && !path.startsWith("/auth/totp")) {
        throw redirect(307, "/auth/totp");
    }

    
    // 5. Ensure app settings are cached - needs to be after successful auth
    if (appSettingsStore.get() == null) {
        try {
            const response = await appSettingsService.getSettings();
            appSettingsStore.set(response);
        } catch {
            console.error("Failed to get app settings");
        }
    }

    // 6. Redirect / → last visited page or /dashboard
    if (path === "/") {
        if (lastVisitedPageStore.get() != null) {
            throw redirect(307, lastVisitedPageStore.get() as string)
        } else {
            throw redirect(307, "/dashboard");
        }
    }
    
    return {};
}