import type { UserCreateRequest, UserTotpCreateResponse, UserTotpStatusResponse } from "@/types/dtos/user";
import APIService from "./api-service";
import type { JwtResponse, LoginRequest, TotpRequest } from "@/types/dtos/auth";
import type { OobeStatusResponse } from "@/types/dtos/oobe";

export default class AuthService extends APIService {
    async login(loginRequest: LoginRequest) {
        const res = await this.api.post("/auth/login", loginRequest)
        return res.data as JwtResponse
    }

    async totpStatus() {
        const res = await this.api.get("/auth/totp", {headers: this.getHeaders()})
        return res.data as UserTotpStatusResponse
    }

    async totpVerify(totpRequest: TotpRequest) {
        const res = await this.api.post("/auth/totp/verify", totpRequest, {headers: this.getHeaders()})
        return res.data as JwtResponse
    }

    async totpCreate() {
        const res = await this.api.post("/auth/totp", {}, {headers: this.getHeaders()})
        return res.data as UserTotpCreateResponse
    }

    async totpActivate(totpRequest: TotpRequest) {
        const res = await this.api.post("/auth/totp/activate", totpRequest, {headers: this.getHeaders()})
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