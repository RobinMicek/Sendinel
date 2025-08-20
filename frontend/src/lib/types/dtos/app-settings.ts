import type { UserBasicsResponse } from "./user";

export interface AppSettingsRequest {
  trackOpenedEmails: boolean;
  allowTemplateImports: boolean;
  displayNewVersionAlert: boolean;
  useGravatar: boolean;
}

export interface AppSettingsResponse {
  trackOpenedEmails: boolean;
  allowTemplateImports: boolean;
  displayNewVersionAlert: boolean;
  useGravatar: boolean;
  updatedBy: UserBasicsResponse;
  updatedOn: string; // ISO-8601 datetime (Instant)
}
