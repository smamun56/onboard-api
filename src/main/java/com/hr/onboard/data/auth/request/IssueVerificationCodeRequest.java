package com.hr.onboard.data.auth.request;

import com.hr.onboard.validation.constraint.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueVerificationCodeRequest {
  @Email
  private String email;
}
