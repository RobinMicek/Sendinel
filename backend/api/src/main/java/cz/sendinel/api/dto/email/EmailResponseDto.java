package cz.sendinel.api.dto.email;

import com.fasterxml.jackson.databind.JsonNode;
import cz.sendinel.api.dto.client.ClientBasicsResponseDto;
import cz.sendinel.api.dto.sender.SenderBasicsResponseDto;
import cz.sendinel.api.dto.template.TemplateBasicsResponseDto;
import cz.sendinel.shared.enums.EmailPrioritiesEnum;
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
    private TemplateBasicsResponseDto template;
    private JsonNode templateVariables;
    private EmailPrioritiesEnum priority;
    private SenderBasicsResponseDto sentBy;
    private ClientBasicsResponseDto requestedBy;
    private Instant createdOn;
    private List<EmailStatusResponseDto> emailStatuses;
}
