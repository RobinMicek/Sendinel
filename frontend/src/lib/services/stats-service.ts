import APIService from "./api-service";
import type { StatsRequest, StatsResponse } from "@/types/dtos/stats";

export default class StatsService extends APIService {
    async get(statsRequest: StatsRequest) {
        const res = await this.api.post("/stats", statsRequest, {headers: this.getHeaders()})
        return res.data as StatsResponse
    }
}