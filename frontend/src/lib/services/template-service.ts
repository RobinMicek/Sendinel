import type { PageResponse } from "@/types/dtos/page";
import APIService from "./api-service";
import type { TemplateBasicsResponse, TemplateExportRequest, TemplateImportRequest, TemplateRequest, TemplateResponse } from "@/types/dtos/template";
import type { BlobFileDownload as OctetStreamFileDownload } from "@/types/dtos/file";
import { EXPORT_FILE_EXTENSION } from "@/config";
import type { AxiosProgressEvent } from "axios";

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

    async export(templateExportRequest: TemplateExportRequest) {
        const res = await this.api.post(`/template/export`, templateExportRequest, {headers: this.getHeaders(), responseType: "blob"})

        const file: OctetStreamFileDownload = {
            blob: res.data as Blob & { type: "application/octet-stream" },
            filename: "templates" + EXPORT_FILE_EXTENSION // Fallback filename
        }

        // Try to extract actual filename
        const disposition = res.headers["content-disposition"];
        if (disposition && disposition.includes("filename")) {
            const match = disposition.match(/filename\*?=(?:UTF-8'')?([^;]+)/);
            if (match?.[1]) {
                file.filename = decodeURIComponent(match[1]);
            }
        }

        return file
    }

    async import(templateImportRequest: TemplateImportRequest, file: File, onProgress?: (percent: number) => void) {
        const formData = new FormData()

        // Append JSON data as a blob
        formData.append("templateImportRequestDto",new Blob([JSON.stringify(templateImportRequest)], { type: "application/json" }))

        // Append the file
        formData.append("file", file)

        const res = await this.api.post(`/template/import?overwriteExisting=${templateImportRequest.overwriteExisting}`,
            formData,
            {
                headers: {...this.getHeaders(), "Content-Type": "multipart/form-data"},
                onUploadProgress: (progressEvent: AxiosProgressEvent) => {
                    if (progressEvent.total) {
                        const percent = Math.round((progressEvent.loaded * 100) / progressEvent.total);
                        onProgress?.(percent);
                    }
                },
            },
            
        )
    }
}