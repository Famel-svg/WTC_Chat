package br.com.fiap.wtccrm.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.fiap.wtccrm.auth.security.JwtUtil;
import br.com.fiap.wtccrm.persistence.document.UserDocument;
import br.com.fiap.wtccrm.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthServiceSeedTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final JwtUtil jwtUtil = mock(JwtUtil.class);

    @Test
    void naoCriaOperadorSemVariaveisDeAmbiente() {
        AuthService service = new AuthService(userRepository, passwordEncoder, jwtUtil, new MockEnvironment());

        service.seedOperator();

        verify(userRepository, never()).save(any());
    }

    @Test
    void naoCriaOperadorComApenasEmailDefinido() {
        MockEnvironment env = new MockEnvironment().withProperty(AuthService.SEED_OPERATOR_EMAIL, "op@example.com");
        AuthService service = new AuthService(userRepository, passwordEncoder, jwtUtil, env);

        service.seedOperator();

        verify(userRepository, never()).save(any());
    }

    @Test
    void criaOperadorQuandoEmailESenhaEstaoDefinidos() {
        MockEnvironment env = new MockEnvironment()
                .withProperty(AuthService.SEED_OPERATOR_EMAIL, "Op@Example.com")
                .withProperty(AuthService.SEED_OPERATOR_PASSWORD, "senha-de-teste");
        when(userRepository.existsByEmailIgnoreCase(anyString())).thenReturn(false);
        when(passwordEncoder.encode("senha-de-teste")).thenReturn("hash");
        AuthService service = new AuthService(userRepository, passwordEncoder, jwtUtil, env);

        service.seedOperator();

        ArgumentCaptor<UserDocument> captor = ArgumentCaptor.forClass(UserDocument.class);
        verify(userRepository).save(captor.capture());
        assertThat(captor.getValue().getEmail()).isEqualTo("op@example.com");
        assertThat(captor.getValue().getRole()).isEqualTo("OPERATOR");
        assertThat(captor.getValue().getPasswordHash()).isEqualTo("hash");
    }
}
