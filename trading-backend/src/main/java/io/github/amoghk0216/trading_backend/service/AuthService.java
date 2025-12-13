package io.github.amoghk0216.trading_backend.service;

import io.github.amoghk0216.trading_backend.dto.UserDto;
import io.github.amoghk0216.trading_backend.exception.InvalidCredentialsException;
import io.github.amoghk0216.trading_backend.exception.RoleNotFoundException;
import io.github.amoghk0216.trading_backend.exception.UserAlreadyExistsException;
import io.github.amoghk0216.trading_backend.model.Role;
import io.github.amoghk0216.trading_backend.model.User;
import io.github.amoghk0216.trading_backend.repository.RoleRepository;
import io.github.amoghk0216.trading_backend.repository.UserRepository;
import io.github.amoghk0216.trading_backend.security.UserDetailsServiceImpl;
import io.github.amoghk0216.trading_backend.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    private RoleRepository roleRepository;

    public String login(UserDto userDto){
        logger.info("Login attempt for user: {}", userDto.email());

        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.email(), userDto.password())
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(userDto.email());
            String token = jwtUtil.generateToken(userDetails);

            logger.info("Login successful for user: {}", userDto.email());
            return token;

        } catch (BadCredentialsException e) {
            logger.warn("Failed login attempt for user: {} - Invalid credentials", userDto.email());
            throw new InvalidCredentialsException("Invalid email or password", e);
        } catch (AuthenticationException e) {
            logger.warn("Authentication failed for user: {}", userDto.email(), e);
            throw new InvalidCredentialsException("Authentication failed", e);
        } catch (Exception e) {
            logger.error("Unexpected error during login for user: {}", userDto.email(), e);
            throw new RuntimeException("An error occurred during login", e);
        }
    }

    public User register(UserDto userDto) {
        logger.info("Registration attempt for user: {}", userDto.email());

        // Validate input
        if (userDto.email() == null || userDto.email().isBlank()) {
            logger.warn("Registration failed: Email is required");
            throw new IllegalArgumentException("Email is required");
        }
        if (userDto.password() == null || userDto.password().isBlank()) {
            logger.warn("Registration failed: Password is required");
            throw new IllegalArgumentException("Password is required");
        }
        if (userDto.name() == null || userDto.name().isBlank()) {
            logger.warn("Registration failed: Name is required");
            throw new IllegalArgumentException("Name is required");
        }

        // Check if user already exists
        Optional<User> existingUser = userRepository.findByEmail(userDto.email());
        if (existingUser.isPresent()) {
            logger.warn("Registration failed: Email already exists - {}", userDto.email());
            throw new UserAlreadyExistsException("User with email " + userDto.email() + " already exists");
        }

        try {
            // Find default user role
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> {
                        logger.error("Default role 'ROLE_USER' not found in database");
                        return new RoleNotFoundException("Default user role not found. Please contact administrator.");
                    });

            // Create new user
            User user = User.builder()
                    .name(userDto.name())
                    .email(userDto.email())
                    .password(passwordEncoder.encode(userDto.password()))
                    .build();

            user.getRoles().add(userRole);

            User savedUser = userRepository.save(user);
            logger.info("User registered successfully: {}", savedUser.getEmail());

            return savedUser;

        } catch (RoleNotFoundException | UserAlreadyExistsException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during registration for user: {}", userDto.email(), e);
            throw new RuntimeException("An error occurred during registration", e);
        }
    }
}
