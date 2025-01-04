package com.hr.onboard.data.auth;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VerificationPair {

    private String key;

    private String code;

    public VerificationPair (String key, String verificationCode) {
        this.key = key;
        this.code = verificationCode;
    }
}
