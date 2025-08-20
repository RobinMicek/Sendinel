import type { UserBasicsResponse } from "./user";

export interface TemplateBasicsResponse {
  id: string; // UUID
  name: string;
  description: string;
}

export interface TemplateRequest {
  name: string;
  description: string;
  subject: string;
  schema: string; // originally JsonNode in Java, converted to string
  textRaw: string;
  htmlRaw: string;
  replyTo: string;
}

export interface TemplateResponse {
  id: string; // UUID
  name: string;
  description: string;
  subject: string;
  schema: string;
  textRaw: string;
  htmlRaw: string;
  replyTo: string;
  createdBy: UserBasicsResponse;
  updatedBy: UserBasicsResponse;
  createdOn: string; // Instant
  updatedOn: string; // Instant
  deletedOn: string | null; // Instant
}

export interface TemplateExportRequest {
  ids: string[]; // UUIDs
  overwriteExisting: boolean;
}
