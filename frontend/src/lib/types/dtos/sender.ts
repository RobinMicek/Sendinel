import type { SenderConfigurationFieldType, SenderTypesEnum } from "../enums/sender-types-enum";
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
  configuration: Record<string, SenderConfigurationFieldType>;
}

export interface SenderResponse {
  id: string; // UUID
  name: string;
  type: SenderTypesEnum;
  description: string;
  configuration: Record<string, SenderConfigurationFieldType>; // JsonNode
  createdBy: UserBasicsResponse;
  updatedBy: UserBasicsResponse;
  createdOn: string; // Instant
  updatedOn: string; // Instant
  deletedOn: string | null; // Instant
}
