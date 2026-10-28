package com.flowchain.api.security;

// @Component — marca a classe como bean gerido pelo Spring,
// para a podermos injetar na cadeia de filtros do SecurityConfig
import org.springframework.stereotype.Component;

// A classe base que vamos estender — garante que o filtro
// corre UMA VEZ por pedido
import org.springframework.web.filter.OncePerRequestFilter;

// Objetos do pedido e da resposta HTTP (API Servlet)
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// FilterChain — a "corrente" de filtros; serve para passar o pedido adiante
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import java.io.IOException;
// O "cofre" onde o Spring Security guarda quem está autenticado no pedido atual
import org.springframework.security.core.context.SecurityContextHolder;

// A representação de uma autenticação com username/password (vamos usá-la sem password)
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

// Representa uma permissão (um role) no Spring Security
import org.springframework.security.core.authority.SimpleGrantedAuthority;

// Detalhes extra do pedido (IP, sessão) — anexados à autenticação
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.util.List;

/**
 * Filtro que intercepta TODOS os pedidos HTTP, antes dos Controllers,
 * para ler e validar o token JWT do header Authorization.
 *
 * Se o token for válido, coloca o utilizador no contexto de segurança
 * do Spring — e a partir daí o pedido é tratado como autenticado.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * O filtro precisa do JwtService para ler e validar tokens.
     * Injeção por construtor — o Spring entrega-o do contentor.
     */
    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * Executado a cada pedido HTTP.
     *
     * @param request  o pedido que chegou (headers, corpo, URL...)
     * @param response a resposta que será devolvida
     * @param filterChain a corrente de filtros — é por aqui que
     *                    passamos o pedido ao próximo da fila
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Ler o header "Authorization" do pedido.
        //    Pode não existir → devolve null.
        final String authHeader = request.getHeader("Authorization");

        // 2. Se não houver header, ou não começar por "Bearer ",
        //    não há token para processar: passa o pedido adiante
        //    sem autenticar ninguém.
        //
        //    O pedido segue como anónimo — e as regras do SecurityConfig
        //    decidem: caminho público entra, caminho protegido é barrado.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;   // sai do método, não executa mais nada aqui
        }

        // 3. Extrair o token: tudo o que vem DEPOIS de "Bearer "
        //    "Bearer " tem 7 caracteres (B-e-a-r-e-r-espaço),
        //    por isso cortamos a String a partir da posição 7.
        final String token = authHeader.substring(7);

        // 4. Validar o token. Se for inválido (assinatura adulterada,
        //    expirado, malformado), não autenticamos ninguém —
        //    o pedido segue como anónimo.
        if (jwtService.isTokenValid(token)) {

            // 5. Ler quem é e qual o papel, de dentro do token.
            //    Nenhuma ida à base de dados — está tudo no payload!
            String email = jwtService.extractEmail(token);
            String role = jwtService.extractRole(token);

            // 6. Converter o role para o formato que o Spring Security entende.
            //    Convenção: os roles levam o prefixo "ROLE_"
            //    "ADMIN" → "ROLE_ADMIN"
            var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

            // 7. Construir o objeto de autenticação.
            //    Argumentos: (quem é, credenciais, permissões)
            //    A password vai a null: já foi validada no login,
            //    e o token é a prova suficiente.
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(email, null, authorities);

            // 8. Anexar detalhes do pedido (IP, etc.) — útil para auditoria
            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request));

            // 9. GUARDAR NO CONTEXTO DE SEGURANÇA.
            //    A partir daqui, o Spring Security considera este
            //    pedido AUTENTICADO. É o passo que faz tudo funcionar.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        // Passa o pedido ao próximo filtro da corrente.
        // Se não chamarmos isto, o pedido MORRE aqui e nunca
        // chega ao Controller.
        filterChain.doFilter(request, response);
    }
}