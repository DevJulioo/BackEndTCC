package com.projeto.tcc.security;

 // Imports necessários (verifique se todos estão presentes)
 import com.projeto.tcc.repository.UseRepository; // Verifique se o nome do repositório está correto (UseRepository ou UserRepository?)
 import jakarta.servlet.FilterChain;
 import jakarta.servlet.ServletException;
 import jakarta.servlet.http.HttpServletRequest;
 import jakarta.servlet.http.HttpServletResponse;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
 import org.springframework.security.core.context.SecurityContextHolder;
 import org.springframework.security.core.userdetails.UserDetails;
 import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
 import org.springframework.stereotype.Component;
 import org.springframework.web.filter.OncePerRequestFilter;

 import java.io.IOException;
 import java.util.Arrays;
 import java.util.List;

 @Component
 public class SecurityFilter extends OncePerRequestFilter {

     @Autowired
     private TokenService tokenService; // Seu serviço para validar tokens JWT

     @Autowired
     private UseRepository userRepository; // Seu repositório de usuários (Verifique o nome: UseRepository ou UserRepository?)

     // Lista de caminhos públicos (deve CORRESPONDER ao SecurityConfig)
     private static final List<AntPathRequestMatcher> PUBLIC_PATHS = Arrays.asList(
             new AntPathRequestMatcher("/auth/**"),             // Rotas de autenticação
             new AntPathRequestMatcher("/uploads/**", "GET"), // Acesso a arquivos estáticos (uploads) via GET
             new AntPathRequestMatcher("/api-docs/**"),       // Recursos da definição OpenAPI
             new AntPathRequestMatcher("/swagger-ui/**"),     // Recursos estáticos do Swagger UI (CSS, JS, etc.)
             new AntPathRequestMatcher("/swagger-ui.html"),   // Página principal do Swagger UI
             new AntPathRequestMatcher("/v3/api-docs/**"),    // Definição OpenAPI v3
             new AntPathRequestMatcher("/"),                  // Rota raiz
             new AntPathRequestMatcher("/error")               // Página de erro padrão
     );

     @Override
     protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
             throws ServletException, IOException {

         // ---- LOGS E LÓGICA PARA PULAR ROTAS PÚBLICAS ----
         String requestURI = request.getRequestURI();
         System.out.println(">>> SecurityFilter: Interceptando request para: " + requestURI); // Log 1: Qual URI?

         // Verifica se o caminho da requisição atual é público
         boolean isPublicPath = PUBLIC_PATHS.stream().anyMatch(matcher -> matcher.matches(request));
         System.out.println(">>> SecurityFilter: isPublicPath para " + requestURI + "? " + isPublicPath); // Log 2: Foi identificado como público?

         if (isPublicPath) {
             System.out.println(">>> SecurityFilter: Pulando validação de token para path público: " + requestURI); // Log 3: Pulou?
             filterChain.doFilter(request, response); // Continua a cadeia SEM validar token
             return; // Sai do filtro aqui
         }
         // ---- FIM DA LÓGICA PARA PULAR ROTAS PÚBLICAS ----


         // --- LÓGICA PARA ROTAS PROTEGIDAS ---
         System.out.println(">>> SecurityFilter: Tentando recuperar token para path protegido: " + requestURI); // Log 4: Chegou na parte protegida?
         var token = this.recoverToken(request); // Tenta extrair o token do header Authorization

         if (token != null) { // Se um token foi encontrado
             var email = tokenService.validateToken(token); // Valida o token e extrai o email/subject
             System.out.println(">>> SecurityFilter: Token encontrado. Email/Subject extraído: " + email); // Log 5: Token válido?

             if (email != null && !email.isEmpty()) { // Se a validação foi bem-sucedida
                 // Busca o usuário no banco de dados pelo email extraído do token
                 // Garanta que sua entidade User implementa UserDetails
                 UserDetails userDetails = this.userRepository.findByEmail(email).orElse(null); // Use orElse(null) se retornar Optional

                 if (userDetails != null) { // Se o usuário correspondente ao token foi encontrado no DB
                     // Cria o objeto de autenticação para o Spring Security
                     var authorities = userDetails.getAuthorities();
                     var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                     // Define o usuário como autenticado no contexto da requisição atual
                     SecurityContextHolder.getContext().setAuthentication(authentication);
                     System.out.println(">>> SecurityFilter: Usuário '" + userDetails.getUsername() + "' autenticado e contexto definido."); // Log 6: Usuário autenticado
                 } else {
                     // Caso o usuário do token não seja encontrado no DB (pode ter sido deletado)
                     System.out.println(">>> SecurityFilter: Nenhum usuário encontrado para o email/subject do token: " + email);
                     SecurityContextHolder.clearContext(); // Limpa o contexto para garantir
                 }

             } else {
                 // Caso o tokenService.validateToken retorne null ou vazio (token inválido ou expirado)
                 System.out.println(">>> SecurityFilter: Token inválido ou expirado (validateToken retornou nulo ou vazio)."); // Log 7: Token inválido
                 SecurityContextHolder.clearContext(); // Limpa o contexto
             }
         } else {
             // Caso nenhum token Bearer seja encontrado no header para uma rota protegida
             System.out.println(">>> SecurityFilter: Nenhum token Bearer encontrado para path protegido: " + requestURI); // Log 8: Sem token
             SecurityContextHolder.clearContext(); // Garante que o contexto esteja limpo
         }

         // Continua a execução da cadeia de filtros (essencial!)
         // Outros filtros do Spring Security (como o de autorização) rodarão depois deste
         filterChain.doFilter(request, response);
     }

     /**
      * Extrai o token JWT do cabeçalho Authorization da requisição.
      * Espera o formato "Bearer <token>".
      * @param request A requisição HTTP.
      * @return O token JWT (sem o prefixo "Bearer ") ou null se não encontrado/inválido.
      */
     private String recoverToken(HttpServletRequest request){
         var authHeader = request.getHeader("Authorization"); // Pega o valor do header Authorization
         if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null; // Retorna null se o header não existir ou não começar com "Bearer "
         }
         // Extrai apenas o token, removendo o prefixo "Bearer "
         try {
             return authHeader.substring(7); // "Bearer ".length() == 7
         } catch (IndexOutOfBoundsException e) {
             // Loga um erro se o header for malformado (ex: só "Bearer")
             System.err.println(">>> SecurityFilter: Erro ao extrair token do header Authorization malformado: " + authHeader);
             return null;
         }
     }
 }
