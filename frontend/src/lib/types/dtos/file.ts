export interface PdfFileDownload {
    blob: Blob & { type: "application/pdf" },
    filename: string
}