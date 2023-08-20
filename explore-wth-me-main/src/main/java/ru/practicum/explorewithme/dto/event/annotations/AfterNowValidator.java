package ru.practicum.explorewithme.dto.event.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class AfterNowValidator implements ConstraintValidator<AfterNow, LocalDateTime> {

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        return value == null || value.isAfter(LocalDateTime.now());
    }
}

