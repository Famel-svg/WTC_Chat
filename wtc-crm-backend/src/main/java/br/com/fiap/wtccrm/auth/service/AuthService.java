package br.com.fiap.wtccrm.auth.service;

import br.com.fiap.wtccrm.auth.dto.LoginRequestDTO;
import br.com.fiap.wtccrm.auth.dto.LoginResponseDTO;
import br.com.fiap.wtccrm.auth.dto.RegisterRequestDTO;
import br.com.fiap.wtccrm.auth.model.UserAccount;
import br.com.fiap.wtccrm.auth.security.JwtUtil;
import br.com.fiap.wtccrm.exception.BusinessException;
import java.time.Instant;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Regras de autenticação: validação de credenciais, hash BCrypt e emissão de JWT.
 * <p>
 * Implementação atual usa armazenamento em memória para desenvolvimento; em produção,
 * substituir por leitura/escrita na coleção Firestore {@code users}.
 */
@Service
public class AuthService {

    private final Map<String, UserAccount> usersById = new ConcurrentHashMap<>();
    private final Map<String, String> idByEmail = new ConcurrentHashMap<>();
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        seedOperator();
    }

    /**
     * Localiza usuário por e-mail, confere senha e gera token HS256 com {@code userId} e {@code role}.
     */
    public LoginResponseDTO login(LoginRequestDTO request) {
        String email = request.email().toLowerCase(Locale.ROOT);
        String userId = idByEmail.get(email);
        if (userId == null) {
            throw new BusinessException("Invalid credentials");
        }
        UserAccount user = usersById.get(userId);
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new BusinessException("Invalid credentials");
        }
        String token = jwtUtil.generateToken(user.getId(), user.getRole());
        return new LoginResponseDTO(token, user.getId(), user.getRole(), user.getName());
    }

    /**
     * Cadastra novo usuário com papel OPERATOR ou CLIENT e retorna JWT da mesma forma que o login.
     */
    public LoginResponseDTO register(RegisterRequestDTO request) {
        String normalizedRole = request.role().toUpperCase(Locale.ROOT);
        if (!normalizedRole.equals("OPERATOR") && !normalizedRole.equals("CLIENT")) {
            throw new BusinessException("Role must be OPERATOR or CLIENT");
        }
        String email = request.email().toLowerCase(Locale.ROOT);
        if (idByEmail.containsKey(email)) {
            throw new BusinessException("E-mail already exists");
        }
        UserAccount user = new UserAccount();
        user.setId(UUID.randomUUID().toString());
        user.setName(request.name());
        user.setEmail(email);
        user.setRole(normalizedRole);
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setCreatedAt(Instant.now());
        usersById.put(user.getId(), user);
        idByEmail.put(email, user.getId());

        String token = jwtUtil.generateToken(user.getId(), user.getRole());
        return new LoginResponseDTO(token, user.getId(), user.getRole(), user.getName());
    }

    /**
     * Persiste o token FCM para envio de notificações push ao dispositivo do usuário.
     */
    public void updateFcmToken(String userId, String token) {
        UserAccount user = usersById.get(userId);
        if (user == null) {
            throw new BusinessException("User not found");
        }
        user.setFcmToken(token);
    }

    /**
     * Garante um operador padrão para testes locais (credenciais no README).
     */
    private void seedOperator() {
        RegisterRequestDTO seed = new RegisterRequestDTO("WTC Operator", "operator@wtc.com", "123456", "OPERATOR");
        register(seed);
    }
}
