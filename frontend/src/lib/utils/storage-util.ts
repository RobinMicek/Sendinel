import {UnauthenticatedError} from "@/exceptions/unauthenticated-error";
import type {UserResponseDto} from "@/backend-sdk";

export function getToken(): string {
    const authToken = localStorage.getItem("authToken");

    if (authToken != null) {
        return "Bearer " + authToken;
    }

    throw new UnauthenticatedError("Missing JWT token");
}

export function setToken(jwtToken: string): void {
    localStorage.setItem("authToken", jwtToken);
}

export function getUser(): UserResponseDto {
    const userInfo = localStorage.getItem("userInfo")

    if (userInfo != null)  {
        return JSON.parse(userInfo);
    }

    throw new UnauthenticatedError("User info is missing");
}

export function setUser(userInfo: UserResponseDto): void {
    return localStorage.setItem("userInfo", JSON.stringify(userInfo));
}

export function removeUserInfo(): void {
    localStorage.removeItem("authToken");
    localStorage.removeItem("userInfo")
}