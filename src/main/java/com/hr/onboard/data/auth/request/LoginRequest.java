package com.hr.onboard.data.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @Schema(description = "username of the user")
    @NotEmpty(message = "username can't be empty !")
    private String username;

    @Schema(description = "password of the user")
    @NotEmpty(message = "password can't be empty !")
    private String password;
}
