import type { PageResponse } from "@/types/dtos/page";
import APIService from "./api-service";
import type { SenderBasicsResponse, SenderRequest, SenderResponse } from "@/types/dtos/sender";

export default class SenderService extends APIService {
    async getAll(pageNumber: number = 0, pageSize: number = this.getPreferedDatatablePageSize(), sortBy: string = "", sortOrder: "asc" | "desc" | "" = "", searchString: string = "") {
        const searchQuery = `name=like=*${searchString}*,description=like=*${searchString}*`

        const res = await this.api.get(`/sender?page=${pageNumber}&size=${pageSize}&sort=${sortBy},${sortOrder}&search=${encodeURIComponent(searchQuery)}`, {headers: this.getHeaders()})
        return res.data as PageResponse<SenderResponse>
    }

    async getAllBasics() {
        const res = await this.api.get(`/sender/list`, {headers: this.getHeaders()})
        return res.data as SenderBasicsResponse[]
    }

    async get(id: string) {
        const res = await this.api.get(`/sender/${id}`, {headers: this.getHeaders()})
        return res.data as SenderResponse
    }

    async create(senderCreateRequest: SenderRequest) {
        const res = await this.api.post("/sender", senderCreateRequest, {headers: this.getHeaders()})
        return res.data as SenderResponse
    }

    async update(id:string, senderUpdateRequest: SenderRequest) {
        const res = await this.api.put(`/sender/${id}`, senderUpdateRequest, {headers: this.getHeaders()})
        return res.data as SenderRequest
    }

    async delete(id: string) {
        const res = await this.api.delete(`/sender/${id}`, {headers: this.getHeaders()})
    }
}