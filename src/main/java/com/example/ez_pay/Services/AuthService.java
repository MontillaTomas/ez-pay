package com.example.ez_pay.Services;

import com.example.ez_pay.DTOs.AuthResponseDTO;
import com.example.ez_pay.DTOs.LoginRequestDTO;
import com.example.ez_pay.DTOs.UserDTO;
import com.example.ez_pay.Models.Role;
import com.example.ez_pay.Models.UserEntity;
import com.example.ez_pay.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserEntity register(UserDTO request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Error: El nombre de usuario ya está en uso.");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Error: El email ya está en uso.");
        }

        Role requestedRole = request.getRol();

        if (requestedRole == null || (requestedRole != Role.EMPLEADO && requestedRole != Role.EMPRESA)) {
            throw new RuntimeException("Error: El rol especificado no es válido. Debe ser EMPLEADO o EMPRESA.");
        }

        UserEntity user = new UserEntity();
        user.setFirstname(request.getFirstname());
        user.setLastName(request.getLastName());
        user.setBirth(request.getBirth());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRol(requestedRole);

        return userRepository.save(user);
    }

    public AuthResponseDTO login(LoginRequestDTO request) {
        // 1. Autenticar al usuario
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // 2. Si la autenticación es exitosa, buscar al usuario
        UserEntity user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 3. Crear el UserDetails (puedes usar tu UserDetailsServiceImpl o crearlo aquí)
        // Usaremos tu UserDetailsServiceImpl para cargar los detalles (incluyendo roles)
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
}
