package com.hr.onboard.data.auth.request;

import com.hr.onboard.validation.constraint.Email;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForgetPasswordRequest {
  @Schema(description = "email of the user")
  @Email
  private String email;
}
