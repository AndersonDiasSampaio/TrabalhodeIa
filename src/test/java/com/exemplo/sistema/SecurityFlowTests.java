package com.exemplo.sistema;

import com.exemplo.sistema.model.Usuario;
import com.exemplo.sistema.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityFlowTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        usuarioRepository.deleteAll();
    }

    @Test
    @DisplayName("Banco de dados deve iniciar limpo sem usuários pré-configurados")
    void databaseShouldStartEmpty() {
        assertThat(usuarioRepository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("Rota protegida /bem-vindo deve redirecionar para /login quando não autenticado")
    void unauthenticatedAccessToProtectedUrlShouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/bem-vindo"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @DisplayName("Tela de login /login deve ser pública e retornar HTTP 200")
    void loginPageShouldBePublic() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    @DisplayName("Tela de cadastro /cadastro deve ser pública e retornar HTTP 200")
    void cadastroPageShouldBePublic() throws Exception {
        mockMvc.perform(get("/cadastro"))
                .andExpect(status().isOk())
                .andExpect(view().name("cadastro"));
    }

    @Test
    @DisplayName("Cadastro de novo usuário via formulário deve persistir no banco e permitir login subsequente")
    void shouldRegisterNewUserSuccessfully() throws Exception {
        String testUser = "carlos";

        mockMvc.perform(post("/cadastro")
                        .with(csrf())
                        .param("nomeCompleto", "Carlos Eduardo")
                        .param("username", testUser)
                        .param("password", "carlos123")
                        .param("confirmPassword", "carlos123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?cadastrado=true"));

        // Validar que o usuário foi salvo no banco
        assertThat(usuarioRepository.existsByUsername(testUser)).isTrue();
        Usuario salvo = usuarioRepository.findByUsername(testUser).orElseThrow();
        assertThat(salvo.getNomeCompleto()).isEqualTo("Carlos Eduardo");

        // Validar que o novo usuário consegue logar
        mockMvc.perform(formLogin("/login").user(testUser).password("carlos123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bem-vindo"));
    }

    @Test
    @DisplayName("Cadastro com senhas divergentes deve falhar com erro de validação")
    void registrationWithMismatchedPasswordsShouldFail() throws Exception {
        mockMvc.perform(post("/cadastro")
                        .with(csrf())
                        .param("nomeCompleto", "Teste Senha Divergente")
                        .param("username", "usuario_divergente")
                        .param("password", "senha123")
                        .param("confirmPassword", "outrasenha"))
                .andExpect(status().isOk())
                .andExpect(view().name("cadastro"))
                .andExpect(model().hasErrors());
    }

    @Test
    @DisplayName("Login com credenciais cadastradas deve autenticar e redirecionar para /bem-vindo")
    void validLoginShouldRedirectToWelcome() throws Exception {
        // Criar usuário de teste no banco
        Usuario usuario = new Usuario("Ana Lima", "analima", passwordEncoder.encode("senhaAna123"), "ROLE_USER");
        usuarioRepository.save(usuario);

        mockMvc.perform(formLogin("/login").user("analima").password("senhaAna123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bem-vindo"));
    }

    @Test
    @DisplayName("Login com credenciais incorretas deve redirecionar para /login?error=true")
    void invalidLoginShouldRedirectToError() throws Exception {
        mockMvc.perform(formLogin("/login").user("inexistente").password("senha_errada"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error=true"));
    }

    @Test
    @WithMockUser(username = "analima", roles = {"USER"})
    @DisplayName("Usuário autenticado deve conseguir acessar /bem-vindo com sucesso (HTTP 200)")
    void authenticatedUserCanAccessWelcomePage() throws Exception {
        mockMvc.perform(get("/bem-vindo"))
                .andExpect(status().isOk())
                .andExpect(view().name("bem-vindo"))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attribute("username", "analima"));
    }

    @Test
    @DisplayName("Logout deve invalidar sessão e redirecionar para /login?logout=true")
    void logoutShouldRedirectToLoginWithLogoutParam() throws Exception {
        mockMvc.perform(logout("/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?logout=true"));
    }
}
