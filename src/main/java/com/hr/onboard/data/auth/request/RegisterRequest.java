package com.hr.onboard.data.auth.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hr.onboard.data.auth.VerificationPair;
import com.hr.onboard.validation.constraint.Email;
import com.hr.onboard.validation.constraint.Password;
import com.hr.onboard.validation.constraint.Username;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
  @Schema(description = "name of the user")
  @Username
  private String username;

  @Schema(description = "email of the user")
  @Email
  private String email;

  @Schema(description = "password of the user")
  @Password
  private String password;

  @Schema(description = "verification for the Registration")
  @Valid
  @NotNull(message = "verification cannot be empty !")
  @JsonProperty("verification")
  private VerificationPair verification;
}
