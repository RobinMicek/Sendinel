export interface StatsRequest {
    startDate: Date;
    endDate: Date;
}

export interface StatsResponse {
    totalSent: number;
    totalSentDaily: Record<string, number>; // string keys representing dates
    openRate: number;
    bouncedRate: number;
    failedRate: number;
    uniqueRecipients: number;
}
