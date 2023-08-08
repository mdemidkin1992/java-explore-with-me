package ru.practicum.explorewithme.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.service.compilation.CompilationService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/compilations")
public class CompilationsController {

    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getCompilations(
            @RequestParam(required = false, name = "pinned") Boolean pinned,
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        log.info("GET | Get Compilations");
        List<CompilationDto> response = compilationService.getCompilations(pinned, from, size);
        log.info("Get Compilations: {}", response);
        return response;
    }

    @GetMapping(path = "/{compId}")
    public CompilationDto getCompilation(
            @PathVariable(name = "compId") long compId
    ) {
        log.info("GET | Get Compilation");
        CompilationDto response = compilationService.getCompilation(compId);
        log.info("Get Compilation: {}", response);
        return response;
    }

}
