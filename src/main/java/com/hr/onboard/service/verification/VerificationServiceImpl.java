package com.hr.onboard.service.verification;

import com.hr.onboard.data.auth.VerificationPair;
import com.hr.onboard.exception.InvalidOperation;
import com.hr.onboard.exception.ValidationError;
import com.hr.onboard.model.auth.VerificationCode;
import com.hr.onboard.repository.verification.VerificationCodeRepository;
import com.hr.onboard.repository.verification.VerifyTokenRepository;
import com.hr.onboard.service.email.EmailService;
import com.hr.onboard.validation.validator.EmailValidator;
import com.hr.onboard.validation.validator.UUIDValidator;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Calendar;
import java.util.UUID;

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
        email = emailValidator.validate(email);

        Calendar exp = Calendar.getInstance();
        exp.add(Calendar.SECOND, 300);
        VerificationCode emailVerification = new VerificationCode();
        emailVerification.setEmail(email);
        emailVerification.setExpireAt(exp.toInstant());
        verificationRepository.save(emailVerification);

        emailService.sendSimpleEmail(
                emailVerification.getEmail(),
                "Verification",
                "your verification code is " + emailVerification.getCode());
        return new VerificationPair(emailVerification.getId().toString(), emailVerification.getCode());
    }

    @Override
    public void verify(String key, String email, String code)
            throws InvalidOperation {
        UUID keyId = uuidValidator.validate(key);
        email = emailValidator.validate(email);
        if (code == null)
            throw new ValidationError("code cannot be null !");

        if (verificationRepository.deleteByIdAndEmailAndCodeAndExpireAtGreaterThan(
                keyId, email, code, Instant.now())
                == 0) {
            throw new InvalidOperation("verification fail !");
        }

    }

    // run by job
    @Transactional
    @Override
    public void deleteExpiredVerificationCodes() {
        logger.info("delete expired verification codes");
        verificationRepository.deleteByExpireAtLessThan(Instant.now());
    }

    // run by job
    @Transactional
    @Override
    public void deleteExpiredVerifyTokens() {
        logger.info("delete expired verification tokens");
        verifyTokenRepository.deleteByExpireAtLessThan(Instant.now());
    }
}
