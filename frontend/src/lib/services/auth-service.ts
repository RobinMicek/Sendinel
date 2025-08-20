import type { UserCreateRequest } from "@/types/dtos/user";
import APIService from "./api-service";
import type { JwtResponse, LoginRequest, TotpRequest } from "@/types/dtos/auth";
import type { OobeStatusResponse } from "@/types/dtos/oobe";

export default class AuthService extends APIService {
    async login(loginRequest: LoginRequest) {
        const res = await this.api.post("/auth/login", loginRequest)
        return res.data as JwtResponse
    }

    async totpVerify(totpRequest: TotpRequest) {
        const res = await this.api.post("/auth/totp/verify", totpRequest)
        return res.data as JwtResponse
    }

    async oobeStatus() {
        const res = await this.api.get("/oobe")
        return res.data as OobeStatusResponse
    }

    async oobeCreateUser(userRequest: UserCreateRequest) {
        const res = await this.api.post("/oobe", userRequest)
        return res.data 
    }
}