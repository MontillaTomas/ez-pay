
package com.example.ez_pay.Services;

import com.example.ez_pay.DTOs.AuthResponseDTO;
import com.example.ez_pay.DTOs.LoginRequestDTO;
import com.example.ez_pay.DTOs.UserDTO;
import com.example.ez_pay.Exceptions.AuthenticationException;
import com.example.ez_pay.Exceptions.ResourceNotFoundException;
import com.example.ez_pay.Mappers.UserMapper;
import com.example.ez_pay.Models.*;
import com.example.ez_pay.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    public UserEntity register(UserDTO request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Error: El nombre de usuario ya está en uso.");
        }
        Role requestedRole = validateRole(request.getRol());

        UserEntity userEntity = userMapper.toEntity(request);
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        userEntity.setRol(requestedRole);
        return userRepository.save(userEntity);
    }

    public AuthResponseDTO login(LoginRequestDTO request) {
        // buscar al usuario
        UserEntity user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AuthenticationException("Usuario o contraseña incorrectos"));

        // Autenticar al usuario
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // 3. Crear el UserDetails (puedes usar tu UserDetailsServiceImpl o crearlo aquí)
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRol().name()))
        );

        // 4. Generar el token
        String token = jwtService.generateToken(userDetails);

        // 5. Devolver el token
        return new AuthResponseDTO(token);
    }

    private static Role validateRole(String requestedRole) {
        if (requestedRole == null || requestedRole.toString().isEmpty()) {
            throw new ResourceNotFoundException("Error: Debe especificar un rol para el usuario.");
        }
        try{
            return Role.fromText(requestedRole.toString());
        } catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Error: el rol ingresado '" + requestedRole.toString() + "' no es válido.");
        }
    }

}
