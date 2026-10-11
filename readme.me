# Sistema de Autenticação com Spring Boot, Thymeleaf e Spring Security

Sistema web moderno desenvolvido em **Java 21**, **Spring Boot 3**, **Thymeleaf**, **Spring Data JPA** e **Spring Security**, com proteção de rotas, cadastro de novos usuários, criptografia de senhas com BCrypt e interface moderna com estética glassmorphism em modo escuro.

---

## 🔒 Mitigações de Vulnerabilidades – [OWASP Top 10:2025](https://owasp.org/Top10/2025/)

Este projeto foi projetado seguindo as melhores práticas de segurança corporativa do ecossistema Spring. Abaixo estão as **vulnerabilidades catalogadas no documento oficial da [OWASP Top 10:2025](https://owasp.org/Top10/2025/)** que esta implementação mitiga ativamente:

---

### 1. [A01:2025 – Broken Access Control](https://owasp.org/Top10/2025/A01_2025-Broken_Access_Control/) (Quebra de Controle de Acesso)
* **Descrição do Risco:** Usuários anônimos ou sem privilégios conseguem acessar rotas restritas, endpoints sensíveis ou funções administrativas sem a devida autorização.
* **Como o código mitiga:**
  * **Negação por Padrão (*Deny by Default*):** Na classe [`SecurityConfig.java`](src/main/java/com/exemplo/sistema/config/SecurityConfig.java), a diretiva `.anyRequest().authenticated()` garante que todo recurso interno (como `/bem-vindo` ou a raiz `/`) seja inacessível sem sessão autenticada:
    ```java
    .authorizeHttpRequests(authorize -> authorize
        .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico", "/error").permitAll()
        .requestMatchers("/login", "/cadastro").permitAll()
        .anyRequest().authenticated()
    )
    ```
  * **Filtros de Interceptação Server-Side:** Qualquer requisição desautorizada é interceptada no nível de filtro HTTP antes da invocação do controlador, gerando redirecionamento imediato (HTTP 302) para `/login`.
  * **Proteção contra CSRF (Cross-Site Request Forgery):** Mantida ativada por padrão pelo Spring Security e sincronizada via formulários Thymeleaf (`th:action`), protegendo endpoints de estado (`/login`, `/cadastro`, `/logout`) contra comandos forjados a partir de origens maliciosas.

---

### 2. [A02:2025 – Security Misconfiguration](https://owasp.org/Top10/2025/A02_2025-Security_Misconfiguration/) (Configuração Incorreta de Segurança)
* **Descrição do Risco:** Servidores ou aplicações com configurações padrão inseguras, falta de cabeçalhos HTTP defensivos (*security headers*), páginas em cache com dados sensíveis e vulnerabilidades a ataques como *Clickjacking* e *MIME-sniffing*.
* **Como o código mitiga:**
  * **Injeção Automática de Cabeçalhos HTTP de Segurança:** O Spring Security injeta por padrão cabeçalhos de defesa em todas as respostas HTTP:
    * `X-Content-Type-Options: nosniff` (impede que o navegador infira tipos MIME incorretos e execute scripts maliciosos disfarçados).
    * `X-Frame-Options: SAMEORIGIN` / `DENY` (previne que a aplicação seja embutida em iframes maliciosos, neutralizando ataques de *Clickjacking*).
    * `Cache-Control: no-cache, no-store, max-age=0, must-revalidate` e `Pragma: no-cache` (evita que páginas protegidas contendo dados do usuário autenticado fiquem salvas no cache de navegadores ou proxies intermediários).
  * **Isolamento de Recursos:** Configuração explícita de permissões apenas para pastas de assets estáticos estritamente necessárias (`/css/**`, `/js/**`, `/images/**`).

---

### 3. [A04:2025 – Cryptographic Failures](https://owasp.org/Top10/2025/A04_2025-Cryptographic_Failures/) (Falhas Criptográficas)
* **Descrição do Risco:** Armazenamento de credenciais em texto puro (*plaintext*) ou uso de algoritmos fracos/desatualizados (como MD5 ou SHA-1 simples sem salt), tornando senhas vulneráveis a vazamentos da base de dados.
* **Como o código mitiga:**
  * **Hash Forte com BCrypt:** O sistema adota o bean [`PasswordEncoder`](src/main/java/com/exemplo/sistema/config/SecurityConfig.java) implementado com `BCryptPasswordEncoder`:
    ```java
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    ```
  * **Salt Automático e Hashing Adaptativo:** Em [`WebController.java`](src/main/java/com/exemplo/sistema/controller/WebController.java), a senha é tratada com BCrypt antes da gravação:
    ```java
    novoUsuario.setPassword(passwordEncoder.encode(dto.getPassword()));
    ```
    O BCrypt gera automaticamente um *salt* aleatório único para cada credencial e utiliza um fator de custo computacional (work factor) configurável, inviabilizando quebras por tabelas pré-computadas (*Rainbow Tables*) e ataques de força bruta offline.

---

### 4. [A05:2025 – Injection](https://owasp.org/Top10/2025/A05_2025-Injection/) (Injeção – SQL Injection e XSS)
* **Descrição do Risco:** Envio de dados não confiáveis inseridos diretamente em comandos interpretados, como consultas SQL maliciosas (*SQL Injection*) ou scripts injetados no navegador da vítima (*Cross-Site Scripting - XSS*).
* **Como o código mitiga:**
  * **Mitigação de SQL Injection via Spring Data JPA / Hibernate:** Todas as consultas no [`UsuarioRepository.java`](src/main/java/com/exemplo/sistema/repository/UsuarioRepository.java) são executadas através de métodos tipados (`findByUsername`, `existsByUsername`), que utilizam internamente **PreparedStatements** com parâmetros vinculados (*bind variables*), separando completamente o código SQL dos dados fornecidos pelo usuário.
  * **Mitigação de XSS (Cross-Site Scripting) via Thymeleaf:** Todas as saídas dinâmicas nos templates (como o nome do usuário em [`bem-vindo.html`](src/main/resources/templates/bem-vindo.html)) utilizam a diretiva padrão `th:text="${...}"`, que aplica escape contextual de caracteres HTML (`<`, `>`, `&`, `"`, `'`). Se um atacante cadastrar um nome como `<script>alert(1)</script>`, ele será renderizado como texto inofensivo (`&lt;script&gt;...`), impedindo a execução arbitrária de scripts no navegador.

---

### 5. [A06:2025 – Insecure Design](https://owasp.org/Top10/2025/A06_2025-Insecure_Design/) (Design Inseguro)
* **Descrição do Risco:** Ausência de padrões de design defensivo, validação de integridade nos fluxos e vulnerabilidade a ataques de *Mass Assignment* (onde campos sensíveis da entidade são alterados diretamente pelo formulário).
* **Como o código mitiga:**
  * **Padrão DTO (Data Transfer Object) e Prevenção de Mass Assignment:** O formulário de cadastro consome exclusivamente o [`CadastroUsuarioDTO.java`](src/main/java/com/exemplo/sistema/dto/CadastroUsuarioDTO.java), impedindo que um usuário manipule parâmetros de requisição para injetar propriedades restritas (como alterar seu próprio perfil para `ROLE_ADMIN` diretamente pela requisição HTTP).
  * **Validação Declarativa Prévia com Bean Validation:** Validação formal na camada de entrada com anotações `@NotBlank` e restrições de tamanho `@Size(min = 6)` avaliadas com `@Valid` e `BindingResult`, rejeitando dados corrompidos ou malformados antes de invocar a regra de negócio.

---

### 6. [A07:2025 – Authentication Failures](https://owasp.org/Top10/2025/A07_2025-Authentication_Failures/) (Falhas de Identificação e Autenticação)
* **Descrição do Risco:** Falhas no ciclo de vida da autenticação, como reutilização de sessões antigas (*Session Fixation* e *Session Hijacking*), aceitação de credenciais fracas ou enumeração de usuários através de mensagens de erro discrepantes.
* **Como o código mitiga:**
  * **Destruição Completa de Sessão no Logout:** Em [`SecurityConfig.java`](src/main/java/com/exemplo/sistema/config/SecurityConfig.java), o manipulador de logout invalida a sessão HTTP no servidor e remove os cookies no cliente:
    ```java
    .logout(logout -> logout
        .logoutUrl("/logout")
        .logoutSuccessUrl("/login?logout=true")
        .invalidateHttpSession(true)
        .deleteCookies("JSESSIONID")
        .permitAll()
    )
    ```
    Isso impede que uma sessão permaneça reutilizável após o logout.
  * **Prevenção de Enumeração de Usuários:** Ao falhar na autenticação, a aplicação redireciona para `/login?error=true` com mensagem neutra, não revelando ao atacante se o erro ocorreu por usuário inexistente ou senha incorreta.
  * **Validação de Confirmação de Senha:** O controlador valida explicitamente se a senha e a confirmação coincidem antes de realizar qualquer operação de gravação no banco.

---

### 📑 Matriz de Conformidade OWASP Top 10:2025

| Vulnerabilidade OWASP 2025 | Mecanismo de Defesa no Código | Componente Responsável |
| :--- | :--- | :--- |
| **A01 – Broken Access Control** | `SecurityFilterChain`, `anyRequest().authenticated()`, Token CSRF | [`SecurityConfig.java`](src/main/java/com/exemplo/sistema/config/SecurityConfig.java) |
| **A02 – Security Misconfiguration** | Cabeçalhos HTTP de segurança (`X-Frame-Options`, `nosniff`, `no-cache`) | Spring Security Default Headers |
| **A04 – Cryptographic Failures** | `BCryptPasswordEncoder` com salt automático e hashing adaptativo | [`SecurityConfig.java`](src/main/java/com/exemplo/sistema/config/SecurityConfig.java), [`WebController.java`](src/main/java/com/exemplo/sistema/controller/WebController.java) |
| **A05 – Injection (SQL / XSS)** | Consultas tipadas JPA (PreparedStatements) e Escape HTML no Thymeleaf (`th:text`) | [`UsuarioRepository.java`](src/main/java/com/exemplo/sistema/repository/UsuarioRepository.java), [`bem-vindo.html`](src/main/resources/templates/bem-vindo.html) |
| **A06 – Insecure Design** | Isolamento por DTO (Prevenção de Mass Assignment) e Bean Validation | [`CadastroUsuarioDTO.java`](src/main/java/com/exemplo/sistema/dto/CadastroUsuarioDTO.java), [`WebController.java`](src/main/java/com/exemplo/sistema/controller/WebController.java) |
| **A07 – Authentication Failures** | `invalidateHttpSession`, `deleteCookies("JSESSIONID")`, mensagens neutras | [`SecurityConfig.java`](src/main/java/com/exemplo/sistema/config/SecurityConfig.java), [`login.html`](src/main/resources/templates/login.html) |

---

## 🚀 Como Executar o Projeto

### Pré-requisitos
- **Java 21**
- **Maven 3.8+**

### Iniciar a Aplicação
No terminal do projeto:
```bash
mvn spring-boot:run
```
O servidor iniciará na porta `8080`.  
Acesse no seu navegador: **[http://localhost:8080](http://localhost:8080)**

---

## 📝 Cadastro e Acesso de Usuários

O sistema inicia com o banco de dados limpo, sem usuários fixos ou pré-configurados.

1. Acesse: **[http://localhost:8080/cadastro](http://localhost:8080/cadastro)** (ou clique em *"Cadastre-se aqui"* na tela de login).
2. Informe o **Nome Completo**, **Nome de Usuário** e crie uma senha.
3. A senha é criptografada com **BCrypt** antes de ser gravada no banco de dados H2.
4. Após o cadastro, você será redirecionado para a tela de login com uma confirmação de sucesso.
5. Faça login com o usuário e senha recém-criados.

---

## 🛡️ Mecanismo de Rotas e Segurança

- **Rotas Públicas:** `/login`, `/cadastro`, `/css/**`, `/js/**` e `/h2-console/**`.
- **Rotas Protegidas:** `/bem-vindo`, `/` e qualquer outra URL interna exigem autenticação obrigatória via `SecurityFilterChain`.
- **Interceptação:** Se um visitante não autenticado tentar acessar `/bem-vindo` diretamente, o Spring Security intercepta e redireciona (HTTP 302) para `/login`.
- **Página de Boas-Vindas:** Exibe mensagem personalizada *"Bem-vindo ao sistema, [Nome]!"*, com dados de sessão, perfil e total de usuários salvos no banco.
- **Logout Seguro:** Encerra a sessão `JSESSIONID`, limpa cookies e redireciona para `/login?logout=true`.
- **Console H2 (Opcional):** Acesse **[http://localhost:8080/h2-console](http://localhost:8080/h2-console)** (JDBC URL: `jdbc:h2:mem:sistemadb`, Usuário: `sa`, Senha em branco).

---

## 📂 Estrutura do Projeto

```
TrabalhodeIa/
├── pom.xml                                   # Dependências e plugins do Maven
├── README.md                                 # Documentação completa do projeto
├── readme.me                                 # Cópia da documentação
├── src/
│   ├── main/
│   │   ├── java/com/exemplo/sistema/
│   │   │   ├── SistemaApplication.java      # Classe principal de inicialização
│   │   │   ├── config/
│   │   │   │   └── SecurityConfig.java       # Configuração de rotas e segurança
│   │   │   ├── controller/
│   │   │   │   └── WebController.java        # Controlador das rotas /login, /cadastro e /bem-vindo
│   │   │   ├── dto/
│   │   │   │   └── CadastroUsuarioDTO.java   # DTO com validações para novos usuários
│   │   │   ├── model/
│   │   │   │   └── Usuario.java              # Entidade JPA persistida no banco H2
│   │   │   ├── repository/
│   │   │   │   └── UsuarioRepository.java    # Repositório Spring Data JPA
│   │   │   └── service/
│   │   │       └── CustomUserDetailsService.java # Integração do Spring Security com o banco H2
│   │   └── resources/
│   │       ├── application.properties        # Configurações do H2, JPA e servidor
│   │       ├── static/
│   │       │   ├── css/style.css             # Estilos modernos em glassmorphism
│   │       │   └── js/main.js                # Interatividade (ver/ocultar senha)
│   │       └── templates/
│   │           ├── login.html                # Tela de Login com Thymeleaf
│   │           ├── cadastro.html             # Tela de Cadastro com validações
│   │           └── bem-vindo.html            # Página de boas-vindas protegida
│   └── test/java/com/exemplo/sistema/
│       └── SecurityFlowTests.java            # Suíte de 10 testes de integração do fluxo de segurança
```

---

## 🧪 Testes Automatizados

O projeto inclui uma suíte completa de 10 testes de integração cobrindo todos os fluxos:
```bash
mvn test
```
Cenários validados:
- Banco de dados iniciando limpo sem usuários pré-configurados
- Interceptação de rotas protegidas (302 para `/login`)
- Acesso às telas públicas (`/login` e `/cadastro`)
- Cadastro com sucesso e persistência no banco H2
- Validação de senhas divergentes no formulário de cadastro
- Login com credenciais válidas do banco
- Rejeição de credenciais inválidas com redirecionamento para `?error=true`
- Renderização de `/bem-vindo` para usuário autenticado
- Invalidação de sessão no logout (302 para `?logout=true`)
