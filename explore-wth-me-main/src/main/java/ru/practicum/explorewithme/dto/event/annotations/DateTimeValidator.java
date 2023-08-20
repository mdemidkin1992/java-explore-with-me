package ru.practicum.explorewithme.dto.event.annotations;

import ru.practicum.explorewithme.dto.event.NewEventDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class DateTimeValidator implements ConstraintValidator<DateTime, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        if (!(value instanceof NewEventDto)) {
            throw new IllegalArgumentException("Invalid validation type");
        }

        NewEventDto entity = (NewEventDto) value;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime eventDate = entity.getEventDate();

        return eventDate != null && eventDate.minusHours(2L).isAfter(now);
    }

}
