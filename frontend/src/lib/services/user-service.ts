import APIService from "./api-service";
import type { UserCreateRequest, UserResponse, UserUpdateRequest } from "@/types/dtos/user";
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

    async get(id: string) {
        const res = await this.api.get(`/user/${id}`, {headers: this.getHeaders()})
        return res.data as UserResponse
    }

    async create(userCreateRequest: UserCreateRequest) {
        const res = await this.api.post("/user", userCreateRequest, {headers: this.getHeaders()})
        return res.data as UserResponse
    }

    async update(id:string, userUpdateRequest: UserUpdateRequest) {
        const res = await this.api.put(`/user/${id}`, userUpdateRequest, {headers: this.getHeaders()})
        return res.data as UserResponse
    }

    async delete(id: string) {
        const res = await this.api.delete(`/user/${id}`, {headers: this.getHeaders()})
    }
}