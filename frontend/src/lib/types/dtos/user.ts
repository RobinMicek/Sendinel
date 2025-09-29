import type { UserRolesEnum } from "../enums/user-roles-enum";

// Basic user info
export interface UserBasicsResponse {
  id: string; // UUID
  firstname: string;
  lastname: string;
}

// Full user response
export interface UserResponse {
  id: string; // UUID
  firstname: string;
  lastname: string;
  email: string;
  role: UserRolesEnum;
  createdBy: UserBasicsResponse;
  updatedBy: UserBasicsResponse;
  createdOn: string; // Instant
  updatedOn: string; // Instant
  deletedOn: string | null; // Instant
}

// Request DTOs
export interface UserCreateRequest {
  firstname: string;
  lastname: string;
  email: string;
  role: UserRolesEnum;
  password: string;
}

export interface UserUpdateRequest {
  firstname: string;
  lastname: string;
  email: string;
  role: UserRolesEnum;
}

export interface UserChangePasswordRequest {
  password: string;
}

// TOTP-related DTOs
export interface UserTotpStatusResponse {
  exists: boolean;
  activated: boolean;
}

export interface UserTotpCreateResponse {
  secret: string;
  qrBase64: string;
}

