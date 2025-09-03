import APIService from "./api-service";
import type { SenderBasicsResponse } from "@/types/dtos/sender";

export default class SenderService extends APIService {
    async getAllBasics() {
        const res = await this.api.get(`/sender/list`, {headers: this.getHeaders()})
        return res.data as SenderBasicsResponse[]
    }
}