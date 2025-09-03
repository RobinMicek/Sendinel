import type { SenderTypesEnum } from "../enums/sender-types-enum";
import type { UserBasicsResponse } from "./user";

export interface SenderBasicsResponse {
  id: string; // UUID
  name: string;
  description: string;
  type: SenderTypesEnum;
}

export interface SenderRequest {
  name: string;
  type: SenderTypesEnum;
  description: string;
  configuration: Record<string, unknown>;
}

export interface SenderResponse {
  id: string; // UUID
  name: string;
  type: SenderTypesEnum;
  description: string;
  configuration: Record<string, unknown>; // JsonNode
  createdBy: UserBasicsResponse;
  updatedBy: UserBasicsResponse;
  createdOn: string; // Instant
  updatedOn: string; // Instant
  deletedOn: string | null; // Instant
}
