import type { PageResponse } from "@/types/dtos/page";
import APIService from "./api-service";
import type { TemplateBasicsResponse, TemplateRequest, TemplateResponse } from "@/types/dtos/template";

export default class TemplateService extends APIService {
    async getAll(pageNumber: number = 0, pageSize: number = this.getPreferedDatatablePageSize(), sortBy: string = "", sortOrder: "asc" | "desc" | "" = "") {
        const res = await this.api.get(`/template?page=${pageNumber}&size=${pageSize}&sort=${sortBy},${sortOrder}`, {headers: this.getHeaders()})
        return res.data as PageResponse<TemplateResponse>
    }

    async getAllBasics() {
        const res = await this.api.get(`/template/list`, {headers: this.getHeaders()})
        return res.data as TemplateBasicsResponse[]
    }

    async get(id: string) {
        const res = await this.api.get(`/template/${id}`, {headers: this.getHeaders()})
        return res.data as TemplateResponse
    }

    async create(templateCreateRequest: TemplateRequest) {
        const res = await this.api.post("/template", templateCreateRequest, {headers: this.getHeaders()})
        return res.data as TemplateResponse
    }

    async update(id:string, templateUpdateRequest: TemplateRequest) {
        const res = await this.api.put(`/template/${id}`, templateUpdateRequest, {headers: this.getHeaders()})
        return res.data as TemplateResponse
    }

    async delete(id: string) {
        const res = await this.api.delete(`/template/${id}`, {headers: this.getHeaders()})
    }
}