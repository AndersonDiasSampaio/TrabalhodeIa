package com.exemplo.sistema;

import com.exemplo.sistema.model.Usuario;
import com.exemplo.sistema.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.core.env.Environment;
@SpringBootApplication
public class SistemaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SistemaApplication.class, args);
    }
/*
    @Bean
    public CommandLineRunner initDatabase(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (!usuarioRepository.existsByUsername("usuario")) {
                Usuario usuarioPadrao = new Usuario(
                        "Usuário Padrão",
                        "usuario",
                        passwordEncoder.encode("123456"),
                        "ROLE_USER"
                );
                usuarioRepository.save(usuarioPadrao);
            }
        };
    }*/



    @Bean
    public CommandLineRunner initDatabase(UsuarioRepository usuarioRepository,
                                          PasswordEncoder passwordEncoder,
                                          Environment env) {
        return args -> {
            // Lança IllegalStateException com mensagem clara se faltar
            String username = env.getRequiredProperty("default.user.username"); // throws if não definido
            String rawPassword = env.getRequiredProperty("default.user.password");
            String role = env.getProperty("default.user.role"); // role com default

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
