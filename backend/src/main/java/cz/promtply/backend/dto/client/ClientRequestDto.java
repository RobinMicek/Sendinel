package cz.promtply.backend.dto.client;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
@NotNull
public class ClientRequestDto {
    @NotBlank
    private String name;

    @NotNull
    private String description;

    private UUID senderId;
}
