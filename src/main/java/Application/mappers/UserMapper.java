package Application.mappers;

import Application.dto.UserCreateDto;
import Application.dto.UserResponseDto;
import Application.dto.UserUpdateDto;
import Application.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toEntity(UserCreateDto dto) {
        User entity = new User();
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setAge(dto.getAge());
        return entity;
    }

    public void updateEntity(User entity, UserUpdateDto dto) {
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setAge(dto.getAge());
    }

    public UserResponseDto toDto(User entity) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setAge(entity.getAge());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }


}
