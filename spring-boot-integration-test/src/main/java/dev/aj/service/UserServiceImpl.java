package dev.aj.service;

import dev.aj.domain.dtos.UserDto;
import dev.aj.domain.mappers.UserMapper;
import dev.aj.domain.model.User;
import dev.aj.exceptions.UsersServiceException;
import dev.aj.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service(value = "userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper mapper = UserMapper.getInstance;

    @Override
    public UserDto createUser(UserDto userDto) {

        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new UsersServiceException("Record already exists");
        }

        User user = mapper.userDtoToUser(userDto);
        user.setUserId(UUID.randomUUID().toString());
        user.setEncryptedPassword(passwordEncoder.encode(userDto.getPassword()));

        User saveduser = userRepository.save(user);

        return mapper.userToUserDto(saveduser);
    }

    @Override
    public List<UserDto> getUsers(int page, int limit) {
        return mapper.usersToUserDtos(userRepository.findAll());
    }

    @Override
    public UserDto getUser(String email) {

        User user = userRepository.findByUserId(email).orElseThrow(() -> new UsersServiceException("Unable to find user with email " + email));
        return mapper.userToUserDto(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsersServiceException("Unable to find user with name " + username));

        return new UserDetails() {

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public String getPassword() {
                return user.getEncryptedPassword();
            }

            @Override
            public String getUsername() {
                return user.getUserId();
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };
    }
}
