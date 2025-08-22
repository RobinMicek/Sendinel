import type { PageLoad } from './$types';
import AppSettingsService from '@/services/app-settings-service';

export const load: PageLoad = async () => {
    const appSettingsService = new AppSettingsService();
    
    try {
        const response = await appSettingsService.getSettings();
        return { appSettingsData: response };

    } catch (e) {
        console.error("Failed to fetch app settings", e);
        return { appSettingsData: null };
    }
};
