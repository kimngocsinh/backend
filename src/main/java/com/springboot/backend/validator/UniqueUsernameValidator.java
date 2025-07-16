package com.springboot.backend.validator;

import com.springboot.backend.annotation.UniqueUsername;
import com.springboot.backend.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    private final UserRepository userRepository;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        return username != null && !userRepository.existsByUsername(username);
    }
}
