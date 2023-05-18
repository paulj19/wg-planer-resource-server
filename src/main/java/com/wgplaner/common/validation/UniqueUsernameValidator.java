package com.wgplaner.common.validation;


import com.wgplaner.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class UniqueUsernameValidator
        implements ConstraintValidator<UniqueUsername, String> {

    private final UserRepository userRepository;

    @Override
    public void initialize(UniqueUsername constraintAnnotation) {
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context){
        return userRepository.findByUsername(username) == null;
    }
}