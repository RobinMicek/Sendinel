package cz.promtply.backend.models.email;

import cz.promtply.backend.entity.EmailStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailJobResponseModel {
    private UUID emailId;
    private EmailStatus status;
}
