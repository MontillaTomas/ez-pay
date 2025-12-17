package com.example.ez_pay.Config;

import com.example.ez_pay.Services.JwtService;
import com.example.ez_pay.Services.Impl.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // 1. Si no hay cabecera o no empieza con "Bearer ", pasamos al siguiente filtro
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extraemos el token (después de "Bearer ")
        jwt = authHeader.substring(7);

        try {
            // 3. Extraemos el username del token
            username = jwtService.extractUsername(jwt);

            // 4. Si el username existe Y el usuario no está autenticado
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // 5. Cargamos los detalles del usuario desde la BD
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                // 6. Validamos el token
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    // 7. Creamos el token de autenticación
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    // 8. Establecemos la autenticación en el SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Manejar excepciones de token inválido (ej. expirado, firma incorrecta)
            // Aquí no hacemos nada, solo dejamos que la petición continúe sin autenticación
        }

        // 9. Pasamos al siguiente filtro
        filterChain.doFilter(request, response);
    }
}