import APIService from "./api-service";
import type { UserResponse } from "@/types/dtos/user";


export default class UserService extends APIService {
    async me() {
        const res = await this.api.get("/user/me", {headers: this.getHeaders()})
        return res.data as UserResponse
    }
}