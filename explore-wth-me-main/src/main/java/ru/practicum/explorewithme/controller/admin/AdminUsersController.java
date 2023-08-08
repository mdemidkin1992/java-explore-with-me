package ru.practicum.explorewithme.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.user.NewUserRequest;
import ru.practicum.explorewithme.dto.user.UserDto;
import ru.practicum.explorewithme.service.user.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
public class AdminUsersController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsersByAdmin(
            @RequestParam(required = false) List<Long> ids,
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        log.info("GET Admin | Get Users");
        List<UserDto> response = userService.getUsersByAdmin(ids, from, size);
        log.info("Get Users: {}", response);
        return response;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addNewUserByAdmin(
            @RequestBody @Valid NewUserRequest userRequest
    ) {
        log.info("POST Admin | New User Request: {}", userRequest);
        UserDto response = userService.addNewUserByAdmin(userRequest);
        log.info("New User added: {}", response);
        return response;
    }

    @DeleteMapping(path = "/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserByAdmin(
            @PathVariable(name = "userId") long userId
    ) {
        log.info("DELETE Admin | UserId: {}", userId);
        userService.deleteUserByAdmin(userId);
        log.info("User {} deleted", userId);
    }

}
