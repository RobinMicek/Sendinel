package cz.promptly.api.dto.client.token;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientTokenRequestDto {
    @NotBlank
    private String name;

    @NotNull
    private String description;

    private Date expiration;
}
