import type { UserBasicsResponse } from "./user";

export interface SenderBasicsResponse {
  id: string; // UUID
  name: string;
  description: string;
  type: string;
}

export interface SenderRequest {
  name: string;
  type: string;
  description: string;
  configuration: Record<string, unknown>;
}

export interface SenderResponse {
  id: string; // UUID
  name: string;
  type: string;
  configuration: Record<string, unknown>; // JsonNode
  createdBy: UserBasicsResponse;
  updatedBy: UserBasicsResponse;
  createdOn: string; // Instant
  updatedOn: string; // Instant
  deletedOn: string | null; // Instant
}
