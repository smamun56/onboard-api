package com.hr.onboard.data.auth.request;

import com.hr.onboard.validation.constraint.Password;
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
public class ResetPasswordRequest {
  @Schema(description = "token after password reset link")
  @NotEmpty(message = "token can not be empty !")
  String token;

  @Password
  String newPassword;
}
