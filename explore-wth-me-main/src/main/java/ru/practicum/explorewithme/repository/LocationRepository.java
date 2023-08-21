package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explorewithme.model.Location;
import ru.practicum.explorewithme.model.enums.LocationStatus;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {

    List<Location> findByStatusAndNameNotNull(LocationStatus status, Pageable pageable);

    @Query("SELECT l FROM Location l WHERE FUNCTION('distance', :lat, :lon, l.lat, l.lon) <= l.rad")
    List<Location> findLocationsWithinRadius(
            @Param("lat") Double lat,
            @Param("lon") Double lon
    );

    Location findByLatAndLon(Double lat, Double lon);

    boolean existsByLatAndLon(Double lat, Double lon);

}
