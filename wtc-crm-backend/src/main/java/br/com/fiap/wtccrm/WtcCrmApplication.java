package br.com.fiap.wtccrm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Ponto de entrada da API WTC CRM. {@link EnableAsync} habilita execução assíncrona (ex.: auditoria).
 */
@SpringBootApplication
@EnableAsync
public class WtcCrmApplication {

    public static void main(String[] args) {
        SpringApplication.run(WtcCrmApplication.class, args);
    }
}
