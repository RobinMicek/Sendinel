import type { UserBasicsResponse } from "./user";
import type { SenderBasicsResponse } from "./sender";

export interface ClientRequest {
  name: string;
  description: string;
  senderId?: string; // UUID as string
}

export interface ClientResponse {
  id: string; // UUID
  name: string;
  description: string;
  sender: SenderBasicsResponse;
  createdBy: UserBasicsResponse;
  updatedBy: UserBasicsResponse;
  createdOn: string; // ISO timestamp
  updatedOn: string; // ISO timestamp
  deletedOn: string | null; // nullable ISO timestamp
}

export interface ClientBasicsResponse {
  id: string; // UUID
  name: string;
  description: string;
}
