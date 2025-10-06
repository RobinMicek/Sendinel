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
  schema: object;
  textRaw: string;
  htmlRaw: string;
  replyTo: string;
  tags: TemplateTagRequest[];
}

export interface TemplateResponse {
  id: string; // UUID
  name: string;
  description: string;
  subject: string;
  schema: object;
  textRaw: string;
  htmlRaw: string;
  replyTo: string;
  tags: TemplateTagResponse[];
  createdBy: UserBasicsResponse;
  updatedBy: UserBasicsResponse;
  createdOn: string; // Instant
  updatedOn: string; // Instant
  deletedOn: string | null; // Instant
}

export interface TemplateTagRequest {
  name: string;
}

export interface TemplateTagResponse {
  id: string; // UUID
  name: string;
  createdOn: string; // Instant
  createdBy: UserBasicsResponse;
}

export interface TemplateExportRequest {
  ids: string[]; // UUIDs
}

export interface TemplateImportRequest {
  overwriteExisting: boolean;
}
