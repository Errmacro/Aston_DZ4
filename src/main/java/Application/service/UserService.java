package Application.service;

import Application.dto.UserDto;
import Application.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Application.repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private User toEntity(UserDto dto) {
        User entity = new User();
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setAge(dto.getAge());
        return entity; }

    private UserDto toDto(User entity) {
        UserDto dto = new UserDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setAge(entity.getAge()); return dto; }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(this::toDto).toList();
    }

    public UserDto addUser(UserDto userDto) {
        if (userDto == null || userDto.getName() == null || userDto.getName().isBlank()) {
            throw new IllegalArgumentException("Некорректные данные пользователя");
        }
        User saved = userRepository.save(toEntity(userDto));
        return toDto(saved);
    }

    public UserDto getUser(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID не может быть null");
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Пользователь с ID " + id + " не найден"));
        return toDto(user);
    }

    public UserDto updateUserData(Long id, UserDto userDto){
        if (id == null || userDto.getName() == null || userDto.getName().isBlank()) {
            throw new IllegalArgumentException("Некорректные данные пользователя");
        }
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Пользователь с ID " + id + " не найден"));
        existing.setName(userDto.getName());
        existing.setEmail(userDto.getEmail());
        existing.setAge(userDto.getAge());
        User updated = userRepository.save(existing);
        return toDto(updated);
    }

    public void removeUser(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Пользователя с таким id не существует");
        }
        userRepository.deleteById(id);
    }
}
