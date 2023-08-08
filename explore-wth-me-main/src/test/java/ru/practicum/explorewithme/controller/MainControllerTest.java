package ru.practicum.explorewithme.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.explorewithme.StatsClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.Mockito.*;

@WebMvcTest(EventsController.class)
public class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatsClient statsClient;

    private static final LocalDateTime START = LocalDateTime.of(2020, 1, 1, 9, 0, 0);
    private static final LocalDateTime END = LocalDateTime.of(2035, 1, 1, 9, 0, 0);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetEvents() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("start", FORMATTER.format(START))
                        .param("end", FORMATTER.format(END)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(""))
                .andDo(MockMvcResultHandlers.print());

        verify(statsClient).sendHit(any(), any());
    }

    @Test
    public void testGetEventById() throws Exception {
        long eventId = 123;

        mockMvc.perform(MockMvcRequestBuilders.get("/events/" + eventId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(""))
                .andDo(MockMvcResultHandlers.print());

        verify(statsClient).sendHit(any(), any());
    }

}
