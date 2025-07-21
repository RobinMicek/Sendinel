package cz.promtply.api.dto.email;

import com.fasterxml.jackson.databind.JsonNode;
import cz.promtply.api.dto.client.ClientBasicsResponseDto;
import cz.promtply.api.dto.sender.SenderBasicsResponseDto;
import cz.promtply.api.dto.template.TemplateBasicsResponseDto;
import cz.promtply.shared.enums.EmailPrioritiesEnum;
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
    private EmailPrioritiesEnum priority;
    private SenderBasicsResponseDto sentBy;
    private ClientBasicsResponseDto requestedBy;
    private Instant createdOn;
    private List<EmailStatusResponseDto> emailStatuses;
}
