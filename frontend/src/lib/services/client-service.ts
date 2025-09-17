import APIService from "./api-service";
import type { PageResponse } from "@/types/dtos/page";
import type { ClientRequest, ClientResponse } from "@/types/dtos/client";
import type { ClientTokenRequest, ClientTokenResponse, ClientTokenValueResponse } from "@/types/dtos/client-token";

export default class ClientService extends APIService {
    async getAll(pageNumber: number = 0, pageSize: number = this.getPreferedDatatablePageSize(), sortBy: string = "", sortOrder: "asc" | "desc" | "" = "", searchString: string = "") {
        const searchQuery = searchString == "" ? "" : `name=like=*${searchString}*,description=like=*${searchString}*`

        const res = await this.api.get(`/client?page=${pageNumber}&size=${pageSize}&sort=${sortBy},${sortOrder}&search=${encodeURIComponent(searchQuery)}`, {headers: this.getHeaders()})
        return res.data as PageResponse<ClientResponse>
    }

    async get(id: string) {
        const res = await this.api.get(`/client/${id}`, {headers: this.getHeaders()})
        return res.data as ClientResponse
    }

    async create(clientCreateRequest: ClientRequest) {
        const res = await this.api.post("/client", clientCreateRequest, {headers: this.getHeaders()})
        return res.data as ClientResponse
    }

    async update(id:string, clientUpdateRequest: ClientRequest) {
        const res = await this.api.put(`/client/${id}`, clientUpdateRequest, {headers: this.getHeaders()})
        return res.data as ClientResponse
    }

    async delete(id: string) {
        const res = await this.api.delete(`/client/${id}`, {headers: this.getHeaders()})
    }

    async getAllTokens(id: string) {
        const res = await this.api.get(`/client/${id}/token`, {headers: this.getHeaders()})
        return res.data as ClientTokenResponse[]
    }

    async createToken(id: string, clientTokenCreateRequest: ClientTokenRequest) {
        const res = await this.api.post(`/client/${id}/token`, clientTokenCreateRequest, {headers: this.getHeaders()})
        return res.data as ClientTokenValueResponse
    }

    async deleteToken(clientId: string, tokenId: string) {
        const res = await this.api.delete(`/client/${clientId}/token/${tokenId}`, {headers: this.getHeaders()})
    }
}