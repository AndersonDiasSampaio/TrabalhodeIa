package com.exemplo.sistema.controller;

import com.exemplo.sistema.dto.CadastroUsuarioDTO;
import com.exemplo.sistema.model.Usuario;
import com.exemplo.sistema.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class WebController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public WebController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            return "redirect:/bem-vindo";
        }
        return "login";
    }

    @GetMapping("/cadastro")
    public String cadastro(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            return "redirect:/bem-vindo";
        }
        if (!model.containsAttribute("usuarioDTO")) {
            model.addAttribute("usuarioDTO", new CadastroUsuarioDTO());
        }
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String processarCadastro(@Valid @ModelAttribute("usuarioDTO") CadastroUsuarioDTO dto,
                                    BindingResult bindingResult,
                                    Model model) {
        // Validação de confirmação de senha
        if (dto.getPassword() != null && !dto.getPassword().equals(dto.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.usuarioDTO", "As senhas não coincidem");
        }

        // Validação de duplicidade de nome de usuário
        if (usuarioRepository.existsByUsername(dto.getUsername())) {
            bindingResult.rejectValue("username", "error.usuarioDTO", "Este nome de usuário já está em uso");
        }

        if (bindingResult.hasErrors()) {
            return "cadastro";
        }

        // Criar e salvar o novo usuário com senha criptografada via BCrypt
        Usuario novoUsuario = new Usuario(
                dto.getNomeCompleto().trim(),
                dto.getUsername().trim().toLowerCase(),
                passwordEncoder.encode(dto.getPassword()),
                "ROLE_USER"
        );
        usuarioRepository.save(novoUsuario);

        return "redirect:/login?cadastrado=true";
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/bem-vindo";
    }

    @GetMapping("/bem-vindo")
    public String bemVindo(Model model, Authentication authentication) {
        if (authentication != null) {
            String username = authentication.getName();
            String roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .map(r -> r.replace("ROLE_", ""))
                    .collect(Collectors.joining(", "));

            Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);
            String nomeExibicao = usuarioOpt.map(Usuario::getNomeCompleto).orElse(username);
            String dataCadastro = usuarioOpt.map(u -> u.getDataCadastro() != null 
                    ? u.getDataCadastro().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) 
                    : "Padrão").orElse("Padrão");

            model.addAttribute("username", username);
            model.addAttribute("nomeCompleto", nomeExibicao);
            model.addAttribute("roles", roles);
            model.addAttribute("dataCadastro", dataCadastro);
            model.addAttribute("totalUsuarios", usuarioRepository.count());
            model.addAttribute("horaAcesso", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        }
        return "bem-vindo";
    }
}
