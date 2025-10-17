export interface JwtToken {
    totp: boolean
    sub: string // UUID
    iat: number
    exp: number
}