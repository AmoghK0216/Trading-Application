package io.github.amoghk0216.trading_backend.service;

import io.github.amoghk0216.trading_backend.dto.UserDto;
import io.github.amoghk0216.trading_backend.model.Role;
import io.github.amoghk0216.trading_backend.model.User;
import io.github.amoghk0216.trading_backend.repository.RoleRepository;
import io.github.amoghk0216.trading_backend.repository.UserRepository;
import io.github.amoghk0216.trading_backend.security.UserDetailsServiceImpl;
import io.github.amoghk0216.trading_backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

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
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.email(), userDto.password()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(userDto.email());

        return jwtUtil.generateToken(userDetails);
    }

    public User register(UserDto userDto) throws Exception{
        User user = User.builder()
                .name(userDto.name())
                .email(userDto.email())
                .password(passwordEncoder.encode(userDto.password()))
                .build();

        Role userRole = roleRepository.findByName("ROLE_USER").orElseThrow(() -> new Exception("Default role not found"));
        user.getRoles().add(userRole);

        Optional<User> check = userRepository.findByEmail(userDto.email());

        if(check.isPresent())
            throw new Exception("email already exists");

        return userRepository.save(user);
    }
}
