import type { ClientBasicsResponse } from "./client";
import type { SenderBasicsResponse } from "./sender";
import type { TemplateBasicsResponse } from "./template";

export interface EmailRequest {
  toAddress: string;
  template: string; // UUID
  templateVariables: Record<string, unknown>; // JsonNode
  priority?: string;
}

export interface EmailResponse {
  id: string; // UUID
  toAddress: string;
  template: TemplateBasicsResponse;
  templateVariables: Record<string, unknown>;
  priority: string;
  sentBy: SenderBasicsResponse;
  requestedBy: ClientBasicsResponse;
  createdOn: string; // Instant
  emailStatuses: EmailStatusResponse[];
}

export interface EmailStatusResponse {
  id: string; // UUID
  status: string;
  note: string;
  createdOn: string; // Instant
}
