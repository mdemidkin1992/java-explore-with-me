package ru.practicum.explorewithme.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.practicum.explorewithme.dto.StatDto;
import ru.practicum.explorewithme.dto.StatDtoWithHits;
import ru.practicum.explorewithme.util.CrudOperations;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class StatsControllerTest extends CrudOperations {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private static StatDto statDto;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static String expectedTimestampString;

    @BeforeEach
    void init() {
        statDto = StatDto.builder()
                .app("ewm-main-service")
                .uri("/events")
                .ip("121.0.0.1")
                .timestamp(LocalDateTime.now())
                .build();

        expectedTimestampString = FORMATTER.format(statDto.getTimestamp());
    }

    @Test
    @SneakyThrows
    void saveStats() {
        StatDto response = saveStatsToDb(statDto);
        assertNotNull(response);
        assertEquals(statDto.getApp(), response.getApp());
        assertEquals(statDto.getUri(), response.getUri());
        assertEquals(statDto.getIp(), response.getIp());
        assertEquals(FORMATTER.format(statDto.getTimestamp()), expectedTimestampString);
    }

    @Test
    @SneakyThrows
    void getStats() {
        StatDto response = saveStatsToDb(statDto);
        final int hits = 1;

        StatDtoWithHits expected = new StatDtoWithHits(
                response.getApp(),
                response.getUri(),
                hits
        );

        MvcResult result = mockMvc.perform(get("/stats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("start", "2020-01-01 10:00:00")
                        .param("end", "2035-01-01 10:00:00")
                        .param("uris", "/events")
                        .param("unique", "false"))
                .andExpect(status().isOk())
                .andReturn();

        List<StatDtoWithHits> dtos = List.of(objectMapper.readValue(
                result.getResponse().getContentAsString(),
                StatDtoWithHits[].class
        ));

        assertNotNull(dtos);
        assertEquals(1, dtos.size());
        assertEquals(expected.getApp(), dtos.get(0).getApp());
        assertEquals(expected.getUri(), dtos.get(0).getUri());
        assertEquals(expected.getHits(), dtos.get(0).getHits());
    }

}