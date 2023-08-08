package ru.practicum.explorewithme.service.user;

import ru.practicum.explorewithme.dto.user.NewUserRequest;
import ru.practicum.explorewithme.dto.user.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getUsersByAdmin(List<Long> ids, int from, int size);

    UserDto addNewUserByAdmin(NewUserRequest userRequest);

    void deleteUserByAdmin(long userId);
}
