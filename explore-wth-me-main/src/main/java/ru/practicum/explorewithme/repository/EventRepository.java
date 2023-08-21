package ru.practicum.explorewithme.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.model.enums.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByInitiatorId(long userId, Pageable pageable);

    List<Event> findByInitiator_IdInAndStateInAndCategory_IdInAndEventDateGreaterThanEqualAndEventDateLessThanEqual(
            @Nullable List<Long> users,
            @Nullable List<EventState> states,
            @Nullable List<Long> categories,
            @Nullable LocalDateTime rangeStart,
            @Nullable LocalDateTime rangeEnd,
            Pageable pageable
    );

    List<Event> findByInitiator_IdInAndCategory_IdIn(
            @Nullable List<Long> users,
            @Nullable List<Long> categories,
            Pageable pageable
    );

    List<Event> findByAnnotationContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndCategory_IdInAndPaidAndEventDateGreaterThanEqualAndEventDateLessThanEqual(
            String annotation, String description, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable
    );


    List<Event> findAllByCategoryId(long catId);

    List<Event> findAllByLocationId(long locationId);

    @Query("SELECT e FROM Event e " +
            "WHERE FUNCTION('distance', :lat, :lon, e.location.lat, e.location.lon) " +
            "<= e.location.rad " +
            "ORDER BY e.eventDate DESC ")
    List<Event> findEventsWithLocationRadius(Double lat, Double lon, Pageable pageable);
}
