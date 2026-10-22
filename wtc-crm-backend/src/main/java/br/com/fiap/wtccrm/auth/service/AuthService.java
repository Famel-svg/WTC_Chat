package br.com.fiap.wtccrm.auth.service;

import br.com.fiap.wtccrm.auth.dto.LoginRequestDTO;
import br.com.fiap.wtccrm.auth.dto.LoginResponseDTO;
import br.com.fiap.wtccrm.auth.dto.RegisterRequestDTO;
import br.com.fiap.wtccrm.auth.security.JwtUtil;
import br.com.fiap.wtccrm.exception.BusinessException;
import br.com.fiap.wtccrm.persistence.document.UserDocument;
import br.com.fiap.wtccrm.persistence.repository.UserRepository;
import java.time.Instant;
import java.util.Locale;
import java.util.UUID;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Autenticação com persistência em MongoDB (coleção {@code users}).
 */
@Service
public class AuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        String email = request.email().toLowerCase(Locale.ROOT);
        UserDocument user = userRepository
                .findByEmailIgnoreCase(email)
                .orElseThrow(() -> new BusinessException("Invalid credentials"));
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new BusinessException("Invalid credentials");
        }
        String token = jwtUtil.generateToken(user.getId(), user.getRole());
        return new LoginResponseDTO(token, user.getId(), user.getRole(), user.getName());
    }

    public LoginResponseDTO register(RegisterRequestDTO request) {
        String normalizedRole = request.role().toUpperCase(Locale.ROOT);
        if (!normalizedRole.equals("OPERATOR") && !normalizedRole.equals("CLIENT")) {
            throw new BusinessException("Role must be OPERATOR or CLIENT");
        }
        String email = request.email().toLowerCase(Locale.ROOT);
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new BusinessException("E-mail already exists");
        }
        UserDocument user = new UserDocument();
        user.setId(UUID.randomUUID().toString());
        user.setName(request.name());
        user.setEmail(email);
        user.setRole(normalizedRole);
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setCreatedAt(Instant.now());
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getId(), user.getRole());
        return new LoginResponseDTO(token, user.getId(), user.getRole(), user.getName());
    }

    public void updateFcmToken(String userId, String token) {
        UserDocument user = userRepository.findById(userId).orElseThrow(() -> new BusinessException("User not found"));
        user.setFcmToken(token);
        userRepository.save(user);
    }

    @PostConstruct
    void seedOperator() {
        try {
            seedUserIfMissing("operator@wtc.com", "123456", "WTC Operator", "OPERATOR");
        } catch (Exception e) {
            LOGGER.warn("Seed do operador padrão não executado: {}", e.getMessage());
        }
    }

    private void seedUserIfMissing(String login, String password, String name, String role) {
        if (userRepository.existsByEmailIgnoreCase(login)) {
            return;
        }
        UserDocument user = new UserDocument();
        user.setId(UUID.randomUUID().toString());
        user.setName(name);
        user.setEmail(login);
        user.setRole(role);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setCreatedAt(Instant.now());
        userRepository.save(user);
    }
}
