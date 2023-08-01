package ru.practicum.explorewithme;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.explorewithme.dto.StatDto;

@Service
public class StatsClient extends BaseClient {
    private static final String API_PREFIX = "/hit";

    @Autowired
    public StatsClient(@Value("${explore-with-me-stats-service.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

//    public ResponseEntity<Object> getEvents() {
//        return get("");
//    }
//
//    public ResponseEntity<Object> getEvent(long eventId) {
//        return get("/" + eventId);
//    }

    public ResponseEntity<Object> sendHit(StatDto statDto) {
        return get("", statDto);
    }
}
