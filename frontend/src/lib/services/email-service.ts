import APIService from "./api-service";
import type { PageResponse } from "@/types/dtos/page";
import type { EmailResponse } from "@/types/dtos/email";
import type { PdfFileDownload } from "@/types/dtos/file";

export default class EmailService extends APIService {
    async getAll(pageNumber: number = 0, pageSize: number = this.getPreferedDatatablePageSize(), sortBy: string = "", sortOrder: "asc" | "desc" | "" = "") {
        const res = await this.api.get(`/email?page=${pageNumber}&size=${pageSize}&sort=${sortBy},${sortOrder}`, {headers: this.getHeaders()})
        return res.data as PageResponse<EmailResponse>
    }

    async get(id: string) {
        const res = await this.api.get(`/email/${id}`, {headers: this.getHeaders()})
        return res.data as EmailResponse
    }

    async exportToPdf(id: string) {
        const res = await this.api.get(`/email/${id}/render`, {headers: this.getHeaders(), responseType: "blob"})

        const file: PdfFileDownload = {
            blob: res.data as Blob & { type: "application/pdf" },
            filename: id + ".pdf" // Fallback filename
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
}