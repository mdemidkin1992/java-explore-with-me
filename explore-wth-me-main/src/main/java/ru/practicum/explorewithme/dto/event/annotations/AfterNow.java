package ru.practicum.explorewithme.dto.event.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AfterNowValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AfterNow {
    String message() default "Date and time must be after current date and time";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

