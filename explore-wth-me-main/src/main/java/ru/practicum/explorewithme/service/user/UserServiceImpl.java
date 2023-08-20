package ru.practicum.explorewithme.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.user.NewUserRequest;
import ru.practicum.explorewithme.dto.user.UserDto;
import ru.practicum.explorewithme.dto.user.mapper.UserMapper;
import ru.practicum.explorewithme.model.User;
import ru.practicum.explorewithme.repository.UserRepository;
import ru.practicum.explorewithme.util.exception.ClientErrorException;
import ru.practicum.explorewithme.util.exception.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsersByAdmin(List<Long> ids, int from, int size) {
        Pageable page = PageRequest.of(from / size, size);
        List<User> response;

        if (ids == null || ids.isEmpty()) {
            response = userRepository.findAll(page).toList();
        } else {
            response = userRepository.findAllByIdIn(ids, page);
        }

        return response.stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto addNewUserByAdmin(NewUserRequest userRequest) {
        User request = UserMapper.mapFromUserDto(userRequest);
        checkIfUserWithNameExists(userRequest.getName());
        User response = userRepository.save(request);
        return UserMapper.mapToUserDto(response);
    }

    @Override
    @Transactional
    public void deleteUserByAdmin(long userId) {
        Optional<User> mayBeUser = userRepository.findById(userId);
        if (mayBeUser.isEmpty()) {
            throw new EntityNotFoundException("User with id " + userId + " not found");
        }
        userRepository.deleteById(userId);
    }

    private void checkIfUserWithNameExists(String name) {
        if (userRepository.existsUserByName(name)) {
            throw new ClientErrorException("User with name " + name + " already exists");
        }
    }
}