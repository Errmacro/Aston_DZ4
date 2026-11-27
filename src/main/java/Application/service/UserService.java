package Application.service;

import Application.dto.UserCreateDto;
import Application.dto.UserResponseDto;
import Application.dto.UserUpdateDto;
import Application.entity.User;
import Application.events.EventType;
import Application.events.UserEvent;
import Application.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import Application.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, KafkaTemplate<String, UserEvent> kafkaTemplate) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.kafkaTemplate = kafkaTemplate;
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
        UserEvent event = new UserEvent();
        event.setType(EventType.CREATE);
        event.setEmail(saved.getEmail());
        kafkaTemplate.send("user-notifications", saved.getEmail(), event);
        return userMapper.toDto(saved);
    }

    public UserResponseDto updateUserData(Long id, UserUpdateDto dto) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Пользователь с ID " + id + " не найден"));
        userMapper.updateEntity(existing, dto);
        return userMapper.toDto(userRepository.save(existing));
    }

    public void removeUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Пользователь с ID " + id + " не найден"));
        userRepository.deleteById(id);
        UserEvent event = new UserEvent();
        event.setType(EventType.DELETE);
        event.setEmail(user.getEmail());
        kafkaTemplate.send("user-notifications", user.getEmail(), event);
    }
}
