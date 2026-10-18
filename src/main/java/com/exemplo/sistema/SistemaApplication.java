package com.exemplo.sistema;

import com.exemplo.sistema.model.Usuario;
import com.exemplo.sistema.repository.UsuarioRepository;
import org.springframework.core.env.Environment; // <--- IMPORT CORRETO
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SistemaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SistemaApplication.class, args);
    }

    @Bean
    public CommandLineRunner initDatabase(UsuarioRepository usuarioRepository,
                                          PasswordEncoder passwordEncoder,
                                          Environment env) {
        return args -> {
            // valores provenientes de application.properties -> variáveis de ambiente do Actions
            String username = env.getProperty("default.user.username");
            String rawPassword = env.getProperty("default.user.password");
            String role = env.getProperty("default.user.role");

            // fail-fast: todos os secrets são obrigatórios
            if (username == null || username.isBlank()) {
                throw new IllegalStateException("DEFAULT_USER_USERNAME não definido — abortando inicialização do usuário padrão");
            }
            if (rawPassword == null || rawPassword.isBlank()) {
                throw new IllegalStateException("DEFAULT_USER_PASSWORD não definido — abortando inicialização do usuário padrão");
            }
            if (role == null || role.isBlank()) {
                throw new IllegalStateException("DEFAULT_USER_ROLE não definido — abortando inicialização do usuário padrão");
            }

            if (!usuarioRepository.existsByUsername(username)) {
                Usuario usuarioPadrao = new Usuario(
                        "Usuário Padrão",
                        username,
                        passwordEncoder.encode(rawPassword),
                        role
                );
                usuarioRepository.save(usuarioPadrao);
            }
        };
    }
}
