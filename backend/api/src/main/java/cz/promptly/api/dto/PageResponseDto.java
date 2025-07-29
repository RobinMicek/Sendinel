package cz.promptly.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResponseDto<T> {
    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
    private boolean empty;

    // Since config/PaginationConfig uses OneIndexedParameters = true,
    // we need to increment each page number by one
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber + 1;
    }
}
