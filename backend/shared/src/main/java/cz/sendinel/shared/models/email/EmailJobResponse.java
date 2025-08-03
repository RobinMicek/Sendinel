package cz.sendinel.shared.models.email;

import cz.sendinel.shared.enums.EmailStatusesEnum;
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
public class EmailJobResponse implements Serializable {
    private UUID emailId;
    private EmailStatusesEnum status;
    private String note = null; // Not required
}
