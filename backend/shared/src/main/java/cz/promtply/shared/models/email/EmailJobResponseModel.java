package cz.promtply.shared.models.email;

import cz.promtply.shared.enums.EmailStatusesEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailJobResponseModel {
    private UUID emailId;
    private EmailStatusesEnum status;
}
