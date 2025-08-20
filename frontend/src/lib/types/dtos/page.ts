export interface PageResponse<T> {
  content: T[];
  pageNumber: number; // 1-indexed
  pageSize: number;
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
  empty: boolean;
}
