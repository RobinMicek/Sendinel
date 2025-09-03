import type { PageResponse } from "@/types/dtos/page";
import APIService from "./api-service";
import type { SenderBasicsResponse, SenderResponse } from "@/types/dtos/sender";

export default class SenderService extends APIService {
    async getAll(pageNumber: number = 0, pageSize: number = this.getPreferedDatatablePageSize(), sortBy: string = "", sortOrder: "asc" | "desc" | "" = "") {
        const res = await this.api.get(`/sender?page=${pageNumber}&size=${pageSize}&sort=${sortBy},${sortOrder}`, {headers: this.getHeaders()})
        return res.data as PageResponse<SenderResponse>
    }

    async getAllBasics() {
        const res = await this.api.get(`/sender/list`, {headers: this.getHeaders()})
        return res.data as SenderBasicsResponse[]
    }
}