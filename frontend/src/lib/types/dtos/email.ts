import type { EmailPrioritiesEnum } from "../enums/email-priorities-enum";
import type { EmailStatusesEnum } from "../enums/email-statuses-enum";
import type { ClientBasicsResponse } from "./client";
import type { SenderBasicsResponse } from "./sender";
import type { TemplateBasicsResponse } from "./template";

export interface EmailRequest {
  toAddress: string;
  template: string; // UUID
  templateVariables: Record<string, unknown>; // JsonNode
  priority?: EmailPrioritiesEnum;
}

export interface EmailResponse {
  id: string; // UUID
  toAddress: string;
  template: TemplateBasicsResponse;
  templateVariables: Record<string, unknown>;
  priority: EmailPrioritiesEnum;
  sentBy: SenderBasicsResponse;
  requestedBy: ClientBasicsResponse;
  createdOn: string; // Instant
  emailStatuses: EmailStatusResponse[];
}

export interface EmailStatusResponse {
  id: string; // UUID
  status: EmailStatusesEnum;
  note: string;
  createdOn: string; // Instant
}
