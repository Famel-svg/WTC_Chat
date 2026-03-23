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

/**
 * Endpoints públicos e de sessão: login, registro e atualização do token FCM do usuário.
 * O JWT é emitido pelo {@link br.com.fiap.wtccrm.auth.service.AuthService} e validado pelo filtro JWT.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Autentica por e-mail/senha e retorna token + dados do usuário.
     */
    @PostMapping("/login")
    public LoginResponseDTO login(@Valid @RequestBody LoginRequestDTO request) {
        return authService.login(request);
    }

    /**
     * Cria operador ou cliente (papel OPERATOR ou CLIENT) e devolve sessão com JWT.
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public LoginResponseDTO register(@Valid @RequestBody RegisterRequestDTO request) {
        return authService.register(request);
    }

    /**
     * Registra o token FCM do dispositivo para push notifications (usado pelo app Android).
     */
    @PutMapping("/users/{id}/fcm-token")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateFcm(@PathVariable String id, @RequestBody String token) {
        authService.updateFcmToken(id, token);
    }
}
