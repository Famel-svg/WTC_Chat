package br.com.fiap.wtccrm.auth.security;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

class JwtUtilSecretTest {

    private final ApplicationContextRunner runner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(PropertyPlaceholderAutoConfiguration.class))
            .withUserConfiguration(JwtUtil.class)
            // espelha application.properties: sem default para JWT_SECRET
            .withPropertyValues("jwt.secret=${JWT_SECRET}");

    @Test
    void falhaNaSubidaSemJwtSecret() {
        runner.run(context -> {
            assertThat(context).hasFailed();
            assertThat(context.getStartupFailure()).rootCause().hasMessageContaining("JWT_SECRET");
        });
    }

    @Test
    void falhaNaSubidaComJwtSecretVazio() {
        runner.withPropertyValues("JWT_SECRET=   ").run(context -> {
            assertThat(context).hasFailed();
            assertThat(context.getStartupFailure())
                    .rootCause()
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("JWT_SECRET");
        });
    }

    @Test
    void sobeEGeraTokenComJwtSecretDefinido() {
        runner.withPropertyValues("JWT_SECRET=segredo-de-teste-com-mais-de-32-bytes!!").run(context -> {
            assertThat(context).hasNotFailed();
            JwtUtil jwtUtil = context.getBean(JwtUtil.class);
            String token = jwtUtil.generateToken("u1", "OPERATOR");
            assertThat(jwtUtil.validateToken(token)).isTrue();
            assertThat(jwtUtil.extractUserId(token)).isEqualTo("u1");
        });
    }
}
