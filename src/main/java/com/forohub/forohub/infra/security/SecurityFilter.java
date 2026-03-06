package com.forohub.forohub.infra.security;

import com.forohub.forohub.domain.usuario.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    public SecurityFilter(TokenService tokenService, UsuarioRepository usuarioRepository) {
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            var token = recuperarToken(request);

            if (token != null) {
                var loginUsuario = tokenService.getSubject(token);
                var usuario = usuarioRepository.findByLogin(loginUsuario);

                if (usuario.isPresent()) {
                    var authentication = new UsernamePasswordAuthenticationToken(
                            usuario.get(),
                            null,
                            usuario.get().getAuthorities()
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    escribirRespuestaNoAutorizado(response, "Usuario del token no existe");
                    return;
                }
            }
        } catch (RuntimeException exception) {
            escribirRespuestaNoAutorizado(response, "Token JWT invalido o expirado");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }
        return authorizationHeader.replace("Bearer ", "");
    }

    private void escribirRespuestaNoAutorizado(HttpServletResponse response, String mensaje) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("{\"error\":\"" + mensaje + "\"}");
    }
}
