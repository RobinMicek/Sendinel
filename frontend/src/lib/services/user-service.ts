import APIService from "./api-service";
import type { UserResponse } from "@/types/dtos/user";
import type { PageResponse } from "@/types/dtos/page";

export default class UserService extends APIService {
    async me() {
        const res = await this.api.get("/user/me", {headers: this.getHeaders()})
        return res.data as UserResponse
    }

    async getAll(pageNumber: number = 0, pageSize: number = this.getPreferedDatatablePageSize(), sortBy: string = "", sortOrder: "asc" | "desc" | "" = "") {
        const res = await this.api.get(`/user?page=${pageNumber}&size=${pageSize}&sort=${sortBy},${sortOrder}`, {headers: this.getHeaders()})
        return res.data as PageResponse<UserResponse>
    }
}