export interface PdfFileDownload {
    blob: Blob & { type: "application/pdf" },
    filename: string
}

export interface BlobFileDownload {
    blob: Blob & { type: "application/octet-stream" },
    filename: string
}