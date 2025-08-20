export interface JwtResponse {
  jwtToken: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface TotpRequest {
  code: string; // must be 6 digits
}
