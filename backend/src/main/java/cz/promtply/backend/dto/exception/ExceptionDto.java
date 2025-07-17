package cz.promtply.backend.dto.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatusCode;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionDto {
    private int statusCode;
    private String reason;

    // Map the statusCode.value to just statusCode
    public void setStatusCode(HttpStatusCode statusCode) {
        this.statusCode = statusCode.value();
    }
}
