package cz.promtply.backend.dto.template;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateRequestDto {
    @NotBlank
    private String name;

    @NotNull
    private String description;

    @NotNull
    private String subject;

    @NotNull
    private String schema;

    @NotNull
    private String textRaw;

    @NotNull
    private String htmlRaw;

    @NotNull
    private String markdownRaw;

    @NotNull
    private boolean preferMarkdown;

    @Email
    @NotNull
    private String replyTo;

    // Accept JsonNode for schema and convert to string
    public void setSchema(JsonNode schemaNode) {
        this.schema = schemaNode.toString();
    }
}
