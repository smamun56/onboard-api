package com.hr.onboard.data.admin.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserIdRequest {
    @Schema(description = "id of target user")
    @UUID(message = "invalid user id !")
    @NotEmpty(message = "user id cannot be empty !")
    private String id;
}
