package dev.aj.service;

import dev.aj.domain.dtos.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto user);

    List<UserDto> getUsers(int page, int limit);

    UserDto getUser(String email);

}
