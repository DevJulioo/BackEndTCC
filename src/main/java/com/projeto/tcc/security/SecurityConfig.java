package com.projeto.tcc.security;

 // Imports necessários
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.context.annotation.Bean;
 import org.springframework.context.annotation.Configuration;
 import org.springframework.http.HttpMethod; // Embora não usado explicitamente agora, é bom manter
 import org.springframework.security.authentication.AuthenticationManager;
 import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
 import org.springframework.security.config.annotation.web.builders.HttpSecurity;
 import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
 import org.springframework.security.config.http.SessionCreationPolicy;
 import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
 import org.springframework.security.crypto.password.PasswordEncoder;
 import org.springframework.security.web.SecurityFilterChain;
 import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

 @Configuration
 @EnableWebSecurity
 public class SecurityConfig {

     @Autowired
     private SecurityFilter securityFilter; // Seu filtro JWT customizado

     @Bean
     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
         http // Configuração encadeada para clareza
             .csrf(csrf -> csrf.disable()) // Desabilita CSRF (comum para APIs stateless)
             .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Garante sessões stateless
             .authorizeHttpRequests(authorize -> authorize
                 // --- Agrupamento de Rotas Públicas ---
                 .requestMatchers(
                         "/",                     // Raiz
                         "/error",                // Página de erro padrão do Spring
                         "/auth/**",              // Rotas de autenticação (login, register) - permite GET, POST, etc.
                         "/uploads/**",           // Rota para arquivos estáticos (uploads) - permite GET por padrão, ajuste se necessário
                         "/v3/api-docs/**",       // Definição OpenAPI JSON/YAML (gerado pelo springdoc)
                         "/swagger-ui/**",      // Recursos estáticos do Swagger UI (CSS, JS, etc.)
                         "/swagger-ui.html"     // Página HTML principal do Swagger UI
                 ).permitAll() // Permite acesso anônimo a todas as rotas listadas acima

                 // --- ROTAS DE MENTOR ---
                 // Apenas usuários com a ROLE "MENTOR" podem acessar
                 .requestMatchers("/api/mentor/**").hasRole("MENTOR")

                 // --- ROTAS DE ALUNO ---
                 // Apenas usuários com a ROLE "ALUNO" podem acessar
                 .requestMatchers("/api/aluno/**").hasRole("ALUNO")
                 .requestMatchers("/api/feedbacks").hasRole("ALUNO") // Confirmar se esta rota é exclusiva para ALUNO

                 // --- Rotas Comuns (requerem apenas autenticação, qualquer role logada) ---
                 .requestMatchers("/api/users/me/**").authenticated() // Dados do próprio usuário logado
                 .requestMatchers("/api/reunioes/**").authenticated() // Operações de reuniões (criar, listar, etc.)
                 .requestMatchers("/api/mentores").authenticated() // Listar mentores (verificar se não deveria ser pública)

                 // --- O Resto ---
                 // Qualquer outra requisição não listada acima requer autenticação
                 .anyRequest().authenticated()
             )
             // Adiciona seu filtro JWT (SecurityFilter) antes do filtro padrão de autenticação de formulário
             .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);

         return http.build(); // Constrói a cadeia de filtros de segurança
     }

     @Bean
     public PasswordEncoder passwordEncoder() {
         // Define o BCrypt como o codificador de senhas padrão
         return new BCryptPasswordEncoder();
     }

     @Bean
     public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
         // Obtém o AuthenticationManager padrão do Spring Security, necessário para o processo de login
         return authenticationConfiguration.getAuthenticationManager();
     }
 }
