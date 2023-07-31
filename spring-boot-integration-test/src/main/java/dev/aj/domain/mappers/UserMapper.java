package dev.aj.domain.mappers;

import dev.aj.domain.dtos.UserDetailsDto;
import dev.aj.domain.dtos.UserDto;
import dev.aj.domain.dtos.UserRestDto;
import dev.aj.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper()
public interface UserMapper {

    UserMapper getInstance = Mappers.getMapper(UserMapper.class);

    UserDto userDetailsToUserDto(UserDetailsDto userDetailsDto);

    UserDetailsDto userDtoToUserDetailsDto(UserDto userDto);

    UserRestDto userDtoToUserRest(UserDto userDto);

    UserDto userRestToUserDto(UserRestDto userRestDto);

    User userDtoToUser(UserDto userDto);

    UserDto userToUserDto(User user);


    List<UserRestDto> userDtosToUserRestDtos(List<UserDto> users);
}
