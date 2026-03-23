package br.com.fiap.wtccrm.auth.controller;

import br.com.fiap.wtccrm.auth.dto.LoginRequestDTO;
import br.com.fiap.wtccrm.auth.dto.LoginResponseDTO;
import br.com.fiap.wtccrm.auth.dto.RegisterRequestDTO;
import br.com.fiap.wtccrm.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@Valid @RequestBody LoginRequestDTO request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public LoginResponseDTO register(@Valid @RequestBody RegisterRequestDTO request) {
        return authService.register(request);
    }

    @PutMapping("/users/{id}/fcm-token")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateFcm(@PathVariable String id, @RequestBody String token) {
        authService.updateFcmToken(id, token);
    }
}
