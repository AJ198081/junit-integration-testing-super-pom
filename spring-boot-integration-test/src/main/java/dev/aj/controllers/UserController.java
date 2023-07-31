package dev.aj.controllers;

import dev.aj.domain.mappers.UserMapper;
import dev.aj.service.UserService;
import dev.aj.domain.dtos.UserDto;
import dev.aj.domain.dtos.UserDetailsDto;
import dev.aj.domain.dtos.UserRestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private static final UserMapper mapper = UserMapper.getInstance;

    @PostMapping
    public UserRestDto createUser(@RequestBody @Valid UserDetailsDto userDetails) {
        UserDto userDto = userService.createUser(mapper.userDetailsToUserDto(userDetails));
        return mapper.userDtoToUserRest(userDto);
    }

    @GetMapping
    public List<UserRestDto> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                      @RequestParam(value = "limit", defaultValue = "2") int limit) {
        List<UserDto> users = userService.getUsers(page, limit);
        return mapper.userDtosToUserRestDtos(users);
    }
}
