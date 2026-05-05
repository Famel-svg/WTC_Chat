package br.com.fiap.wtccrm.config;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Carrega o arquivo {@code .env} da raiz do repositório ou da pasta do backend antes do Spring Boot subir.
 * Variáveis já definidas no OS ({@code System.getenv}) têm prioridade.
 */
public final class DotenvBootstrap {

    private DotenvBootstrap() {}

    public static void load() {
        Path cwd = Path.of("").toAbsolutePath().normalize();
        List<Path> candidates = new ArrayList<>();
        candidates.add(cwd.resolve(".env"));
        Path parent = cwd.getParent();
        if (parent != null) {
            candidates.add(parent.resolve(".env"));
        }

        for (Path envFile : candidates) {
            if (!Files.isRegularFile(envFile)) {
                continue;
            }
            try {
                for (String raw : Files.readAllLines(envFile, StandardCharsets.UTF_8)) {
                    applyLine(raw);
                }
            } catch (Exception ignored) {
                // falha silenciosa: app usa só application.properties / env do sistema
            }
            break;
        }
    }

    static void applyLine(String line) {
        if (line == null) {
            return;
        }
        String t = line.trim();
        if (t.isEmpty() || t.startsWith("#")) {
            return;
        }
        int eq = t.indexOf('=');
        if (eq <= 0) {
            return;
        }
        String key = t.substring(0, eq).trim();
        String val = t.substring(eq + 1).trim();
        if ((val.startsWith("\"") && val.endsWith("\"")) || (val.startsWith("'") && val.endsWith("'"))) {
            val = val.substring(1, val.length() - 1);
        }
        if (System.getenv(key) != null || System.getProperty(key) != null) {
            return;
        }
        System.setProperty(key, val);
    }
}
