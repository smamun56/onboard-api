package com.hr.onboard.service.user.auth;

import com.hr.onboard.data.auth.VerificationPair;
import com.hr.onboard.entity.auth.User;
import com.hr.onboard.entity.enums.Role;
import com.hr.onboard.exception.AlreadyExist;
import com.hr.onboard.exception.InvalidOperation;
import com.hr.onboard.repository.UserRepository;
import com.hr.onboard.service.verification.VerificationService;
import com.hr.onboard.validation.validator.EmailValidator;
import com.hr.onboard.validation.validator.PasswordValidator;
import com.hr.onboard.validation.validator.UserNameValidator;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationServiceImpl implements RegistrationService{

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    VerificationService verificationService;

    EmailValidator emailValidator = EmailValidator.getInstance();
    PasswordValidator passwordValidator = PasswordValidator.getInstance();
    UserNameValidator userNameValidator = UserNameValidator.getInstance();


    @Override
    public User createUser(String username, String password, String email, Role role)
            throws AlreadyExist {
        username = userNameValidator.validate(username);
        password = passwordValidator.validate(password);
        email = emailValidator.validate(email);
        if (userRepository.getByUserName(username).isPresent()
                || userRepository.getByEmail(email).isPresent())
            throw new AlreadyExist("username or email is already taken !");

        User user = new User();
        user.setUserName(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRole(role);
        userRepository.save(user);

        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User registerUser(
            String username, String password, String email, VerificationPair verificationPair)
            throws AlreadyExist, InvalidOperation {
        verificationService.verify(verificationPair.getKey(), email, verificationPair.getCode());
        return createUser(username, password, email, Role.NORMAL);
    }
}
