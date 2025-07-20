package cz.promtply.backend.dto.email;

import com.fasterxml.jackson.databind.JsonNode;
import cz.promtply.backend.dto.client.ClientBasicsResponseDto;
import cz.promtply.backend.dto.sender.SenderBasicsResponseDto;
import cz.promtply.backend.dto.template.TemplateBasicsResponseDto;
import cz.promtply.backend.enums.EmailPriorities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailResponseDto {
    private UUID id;
    private String toAddress;
    private String trackCode;
    private TemplateBasicsResponseDto template;
    private JsonNode templateVariables;
    private EmailPriorities priority;
    private SenderBasicsResponseDto sentBy;
    private ClientBasicsResponseDto requestedBy;
    private Instant createdOn;
    private List<EmailStatusResponseDto> emailStatuses;
}
