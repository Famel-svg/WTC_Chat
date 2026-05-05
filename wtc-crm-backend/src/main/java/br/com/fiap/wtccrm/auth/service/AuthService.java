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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Autenticação com persistência em MongoDB (coleção {@code users}).
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        seedOperator();
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

    private void seedOperator() {
        if (userRepository.existsByEmailIgnoreCase("operator@wtc.com")) {
            return;
        }
        UserDocument user = new UserDocument();
        user.setId(UUID.randomUUID().toString());
        user.setName("WTC Operator");
        user.setEmail("operator@wtc.com");
        user.setRole("OPERATOR");
        user.setPasswordHash(passwordEncoder.encode("123456"));
        user.setCreatedAt(Instant.now());
        userRepository.save(user);
    }
}
