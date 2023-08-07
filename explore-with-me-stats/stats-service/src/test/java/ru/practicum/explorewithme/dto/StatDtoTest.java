package ru.practicum.explorewithme.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class StatDtoTest {

    @Autowired
    private JacksonTester<StatDto> jsonTester;

    @Test
    public void testSerializeStatDto() throws IOException {
        LocalDateTime timestamp = LocalDateTime.of(2023, 8, 31, 12, 30, 15);

        StatDto statDto = StatDto.builder()
                .app("ewm-main-service")
                .uri("/events")
                .ip("121.0.0.1")
                .timestamp(timestamp)
                .build();

        JsonContent<StatDto> jsonContent = jsonTester.write(statDto);
        assertThat(jsonContent).extractingJsonPathStringValue("$.app").isEqualTo("ewm-main-service");
        assertThat(jsonContent).extractingJsonPathStringValue("$.uri").isEqualTo("/events");
        assertThat(jsonContent).extractingJsonPathStringValue("$.ip").isEqualTo("121.0.0.1");
        assertThat(jsonContent).extractingJsonPathStringValue("$.timestamp")
                .isEqualTo("2023-08-31 12:30:15");
    }

    @Test
    public void testDeserializeStatDto() throws IOException {
        String json = "{\"app\":\"another-app\",\"uri\":\"/another/uri\",\"ip\":\"192.168.0.1\",\"timestamp\":\"2023-08-31 18:45:30\"}";

        StatDto statDto = jsonTester.parse(json).getObject();

        assertThat(statDto.getApp()).isEqualTo("another-app");
        assertThat(statDto.getUri()).isEqualTo("/another/uri");
        assertThat(statDto.getIp()).isEqualTo("192.168.0.1");
        assertThat(statDto.getTimestamp()).isEqualTo(LocalDateTime.of(2023, 8, 31, 18, 45, 30));
    }

}