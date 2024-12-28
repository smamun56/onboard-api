package com.hr.onboard.data.admin.request;

import com.hr.onboard.validation.constraint.Role;
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
public class ChangeUserRoleRequest {
    @Schema(description = "id of target user")
    @UUID(message = "invalid user id !")
    @NotEmpty(message = "user id can not empty !")
    private String id;

    @Schema(description = "the role that target user want to change to")
    @Role
    private String role;
}
