package cz.promtply.backend.models.email;

import com.fasterxml.jackson.databind.JsonNode;
import cz.promtply.backend.enums.EmailPrioritiesEnum;
import cz.promtply.backend.enums.SenderTypesEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmailJobRequestModel {
    private UUID emailId;
    private String toAddress;
    private String replyTo;
    private EmailPrioritiesEnum priority;
    private String subject;
    private String renderedTextBody;
    private String renderedHtmlBody;
    private SenderTypesEnum senderType;
    private JsonNode senderConfiguration;
}
