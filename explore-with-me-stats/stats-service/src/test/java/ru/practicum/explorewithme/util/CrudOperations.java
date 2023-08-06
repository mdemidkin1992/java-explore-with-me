package ru.practicum.explorewithme.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.practicum.explorewithme.dto.StatDto;

import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CrudOperations {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public StatDto saveStatsToDb(StatDto statDto) throws Exception {

        MvcResult result = mockMvc.perform(post("/hit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.app").value(statDto.getApp()))
                .andExpect(jsonPath("$.uri").value(statDto.getUri()))
                .andExpect(jsonPath("$.ip").value(statDto.getIp()))
                .andExpect(jsonPath("$.timestamp").value(FORMATTER.format(statDto.getTimestamp())))
                .andReturn();

        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                StatDto.class
        );
    }

}
