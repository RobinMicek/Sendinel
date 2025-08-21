import type { AppSettingsResponse } from "@/types/dtos/app-settings";
import APIService from "./api-service";

export default class AppSettingsService extends APIService {
    async getSettings() {
        const res = await this.api.get("/app-settings", {headers: this.getHeaders()})
        return res.data as AppSettingsResponse
    }
}