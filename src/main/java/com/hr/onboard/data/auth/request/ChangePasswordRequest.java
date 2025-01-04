package com.hr.onboard.data.auth.request;

import com.hr.onboard.validation.constraint.Password;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {
  @NotEmpty(message = "password cannot be empty !")
  String oldPassword;

  @Password
  String newPassword;
}
