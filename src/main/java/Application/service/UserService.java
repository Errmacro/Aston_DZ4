package Application.service;

import Application.dto.UserCreateDto;
import Application.dto.UserResponseDto;
import Application.dto.UserUpdateDto;
import Application.entity.User;
import Application.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Application.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Пользователь с ID " + id + " не найден"));
        return userMapper.toDto(user);
    }


    public UserResponseDto addUser(UserCreateDto dto) {
        User saved = userRepository.save(userMapper.toEntity(dto));
        return userMapper.toDto(saved);
    }

    public UserResponseDto updateUserData(Long id, UserUpdateDto dto) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Пользователь с ID " + id + " не найден"));
        userMapper.updateEntity(existing, dto);
        return userMapper.toDto(userRepository.save(existing));
    }

    public void removeUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException("Пользователь с ID " + id + " не найден");
        }
        userRepository.deleteById(id);
    }
}
