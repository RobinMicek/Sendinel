package cz.sendinel.shared.models.email;

import com.fasterxml.jackson.databind.JsonNode;
import cz.sendinel.shared.enums.EmailPrioritiesEnum;
import cz.sendinel.shared.enums.SenderTypesEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmailJobRequest implements Serializable {
    private UUID emailId;
    private String toAddress;
    private String replyTo;
    private EmailPrioritiesEnum priority;
    private String subject;
    private String renderedTextBody;
    private String renderedHtmlBody;
    private SenderTypesEnum senderType;
    private JsonNode senderConfiguration;

    private int retryCount = 0;
}
