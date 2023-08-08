package ru.practicum.explorewithme.service.event;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.StatsClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class StatsServiceClient {

    private final StatsClient statsClient;

    protected int getHits(@NonNull ResponseEntity<Object> responseEntity) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> responseList
                = objectMapper.convertValue(responseEntity.getBody(), new TypeReference<>() {});

        if (!responseList.isEmpty()) {
            Map<String, Object> firstResponseMap = responseList.get(0);
            if (firstResponseMap.containsKey("hits")) {
                Object hitsValue = firstResponseMap.get("hits");
                if (hitsValue instanceof Integer) {
                    return (int) hitsValue;
                }
            }
        }

        return 0;
    }

    protected ResponseEntity<Object> getStatistics(long id) {
        LocalDateTime start = LocalDateTime.of(2020, 1, 1, 10, 0,0);
        LocalDateTime end = LocalDateTime.of(2035, 1, 1, 10, 0,0);
        return statsClient.getStats(
                start,
                end,
                List.of("/events/" + id),
                true
        );
    }

    protected void sendHit(String app, HttpServletRequest request) {
        statsClient.sendHit(app, request);
    }
}
