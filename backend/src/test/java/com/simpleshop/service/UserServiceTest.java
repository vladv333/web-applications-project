package com.simpleshop.service;

import com.simpleshop.dto.LoginRequest;
import com.simpleshop.dto.RegisterRequest;
import com.simpleshop.entity.Cart;
import com.simpleshop.entity.User;
import com.simpleshop.repository.CartRepository;
import com.simpleshop.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService service;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "vlad", "vlad@gmail.com", "$2a$10$hashedpassword");
    }

    @Test
    void register_validRequest_createsUserAndCart() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("vlad");
        request.setEmail("vlad@gmail.com");
        request.setPassword("123456");

        when(userRepository.existsByEmail("vlad@gmail.com")).thenReturn(false);
        when(userRepository.existsByUsername("vlad")).thenReturn(false);
        when(passwordEncoder.encode("123456")).thenReturn("$2a$10$hashedpassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(cartRepository.save(any(Cart.class))).thenReturn(new Cart());

        User result = service.register(request);

        assertThat(result.getUsername()).isEqualTo("vlad");
        verify(userRepository).save(any(User.class));
        verify(cartRepository).save(any(Cart.class)); // cart must be created too
    }

    @Test
    void register_emailAlreadyTaken_throwsException() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("vlad@gmail.com");
        request.setUsername("vlad");

        when(userRepository.existsByEmail("vlad@gmail.com")).thenReturn(true);

        assertThatThrownBy(() -> service.register(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Email already in use");
    }

    @Test
    void register_usernameTaken_throwsException() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("new@gmail.com");
        request.setUsername("vlad");

        when(userRepository.existsByEmail("new@gmail.com")).thenReturn(false);
        when(userRepository.existsByUsername("vlad")).thenReturn(true);

        assertThatThrownBy(() -> service.register(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Username already taken");
    }

    @Test
    void login_validCredentials_returnsUser() {
        LoginRequest request = new LoginRequest();
        request.setEmail("vlad@gmail.com");
        request.setPassword("123456");

        when(userRepository.findByEmail("vlad@gmail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("123456", user.getPassword())).thenReturn(true);

        User result = service.login(request);

        assertThat(result.getEmail()).isEqualTo("vlad@gmail.com");
    }

    @Test
    void login_userNotFound_throwsException() {
        LoginRequest request = new LoginRequest();
        request.setEmail("unknown@gmail.com");
        request.setPassword("123456");

        when(userRepository.findByEmail("unknown@gmail.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.login(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void login_wrongPassword_throwsException() {
        LoginRequest request = new LoginRequest();
        request.setEmail("vlad@gmail.com");
        request.setPassword("wrongpassword");

        when(userRepository.findByEmail("vlad@gmail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", user.getPassword())).thenReturn(false);

        assertThatThrownBy(() -> service.login(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Invalid password");
    }
}