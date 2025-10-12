package mate.academy.taskmanagementapp.mapper;

import mate.academy.taskmanagementapp.config.MapConfig;
import mate.academy.taskmanagementapp.dto.*;
import mate.academy.taskmanagementapp.model.User;
import org.mapstruct.*;

@Mapper(config = MapConfig.class)
public interface UserMapper {
    User toEntity(UserRegistrationDto userRegistrationDto);

    UserResponseDto toDto(User user);

    void updateUserFromDto(UserUpdateDto dto, @MappingTarget User user);

}
