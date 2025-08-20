import type { UserBasicsResponse } from "./user";

export interface ClientTokenRequest {
  name: string;
  description: string;
  expiration?: string; // Date -> ISO string
}

export interface ClientTokenResponse {
  id: string; // UUID
  name: string;
  description: string;
  expiration: string | null; // Date -> ISO string
  isExpired: boolean;
  createdBy: UserBasicsResponse;
  deletedBy: UserBasicsResponse;
  lastUsedOn: string | null; // Instant
  createdOn: string; // Instant
  deletedOn: string | null; // Instant
}

export interface ClientTokenValueResponse {
  token: string;
}
