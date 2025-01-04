package com.hr.onboard.service.verification;

import com.hr.onboard.data.auth.VerificationPair;
import com.hr.onboard.exception.InvalidOperation;
import com.hr.onboard.repository.verification.VerificationCodeRepository;
import com.hr.onboard.repository.verification.VerifyTokenRepository;
import com.hr.onboard.service.email.EmailService;
import com.hr.onboard.validation.validator.EmailValidator;
import com.hr.onboard.validation.validator.UUIDValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerificationServiceImpl implements VerificationService {
    @Autowired VerificationCodeRepository verificationRepository;
    @Autowired VerifyTokenRepository verifyTokenRepository;
    @Autowired
    EmailService emailService;
    EmailValidator emailValidator = EmailValidator.getInstance();
    UUIDValidator uuidValidator = UUIDValidator.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(VerificationService.class);

    @Override
    public VerificationPair issueVerificationCode(String email) {
        return null;
    }

    @Override
    public void verify(String key, String email, String code) throws InvalidOperation {

    }

    @Override
    public void deleteExpiredVerificationCodes() {

    }

    @Override
    public void deleteExpiredVerifyTokens() {

    }
}
