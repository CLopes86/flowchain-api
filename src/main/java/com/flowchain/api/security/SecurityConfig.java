package com.flowchain.api.security;

// @Configuration — marca esta classe como fonte de configuração do Spring
// O Spring processa-a no arranque e regista os @Bean que ela produz
import org.springframework.context.annotation.Configuration;

// @Bean — marca métodos cujo resultado vai para o contentor do Spring
import org.springframework.context.annotation.Bean;

// O objeto que descreve a cadeia de filtros de segurança
import org.springframework.security.web.SecurityFilterChain;

// O "construtor" da cadeia — configuramo-lo e ele produz a SecurityFilterChain
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

// Política de criação de sessões — vamos usar STATELESS
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * Configuração central do Spring Security para o FlowChain.
 *
 * Ao definirmos o nosso próprio SecurityFilterChain, o Spring Security
 * abandona os comportamentos por defeito (form login, user gerado, sessões)
 * e passa a usar exclusivamente as regras definidas aqui.
 */
@Configuration
public class SecurityConfig {
    /**
     * Define a cadeia de filtros de segurança da aplicação.
     * <p>
     * O Spring chama este método no arranque, guarda o resultado
     * no contentor, e aplica esta cadeia a todos os pedidos HTTP.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Desliga a proteção CSRF — só faz sentido no modelo sessões+cookies.
                // Com JWT no header Authorization, este ataque não se aplica.
                .csrf(csrf -> csrf.disable())

                // Proíbe o Spring de criar ou usar sessões HTTP.
                // STATELESS = cada pedido é independente e traz a sua prova (o token).
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Regras de autorização, avaliadas POR ORDEM:
                .authorizeHttpRequests(auth -> auth
                        // Caminhos do Swagger — públicos (documentação e testes)
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/api-docs/**").permitAll()
                        // Tudo o resto — só com autenticação válida
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
