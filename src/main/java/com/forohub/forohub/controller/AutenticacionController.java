package com.forohub.forohub.controller;

import com.forohub.forohub.domain.usuario.DatosAutenticacionUsuario;
import com.forohub.forohub.domain.usuario.DatosTokenJWT;
import com.forohub.forohub.domain.usuario.Usuario;
import com.forohub.forohub.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AutenticacionController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AutenticacionController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<DatosTokenJWT> autenticar(@RequestBody @Valid DatosAutenticacionUsuario datos) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(datos.login(), datos.clave());
        var auth = authenticationManager.authenticate(authenticationToken);
        var tokenJWT = tokenService.generarToken((Usuario) auth.getPrincipal());
        return ResponseEntity.ok(new DatosTokenJWT(tokenJWT));
    }
}
