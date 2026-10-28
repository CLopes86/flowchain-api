package com.flowchain.api.security;

// @Service — marca a classe como bean de lógica gerido pelo Spring.
// Assim podemos injetá-la no AuthService e, mais tarde, no filtro JWT.
import org.springframework.stereotype.Service;

// @Value — injeta valores lidos do application.yaml
import org.springframework.beans.factory.annotation.Value;

// jjwt — a API (o contrato) que adicionámos ao pom.xml
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

// SecretKey — a representação criptográfica da chave de assinatura
import javax.crypto.SecretKey;

// Date — o jjwt trabalha com java.util.Date nas datas do token
import java.util.Date;
// Claims — representa o conjunto de dados (o payload) de dentro do token
import io.jsonwebtoken.Claims;
import com.flowchain.api.entity.User;

/**
 * Serviço responsável pela mecânica dos tokens JWT:
 * gerar, assinar e (mais tarde) validar e ler claims.
 *
 * Separado do AuthService por responsabilidade:
 *   AuthService → fluxo de login (procurar user, validar password)
 *   JwtService  → mecânica dos tokens
 */
@Service
public class JwtService {

    /**
     * Chave secreta lida do application.yaml (jwt.secret).
     * É com ela que os tokens são assinados — quem a tiver
     * consegue forjar tokens válidos. Nunca sai do servidor.
     */
    @Value("${jwt.secret}")
    private String secretKey;

    /**
     * Validade do token em milissegundos (jwt.expiration).
     * 86400000 ms = 24 horas.
     */
    @Value("${jwt.expiration}")
    private long expiration;

    /**
     * Converte a String da configuração num objeto SecretKey,
     * que é o formato que o jjwt precisa para assinar.
     *
     * private → só esta classe usa; é um detalhe interno.
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * Gera um token JWT para o utilizador autenticado.
     *
     * O token vai conter (no payload):
     *   sub   → o email (identifica o dono)
     *   role  → o papel (ADMIN, WAREHOUSE, UNIT, COURIER)
     *   iat   → quando foi emitido
     *   exp   → quando expira
     *
     * @param user utilizador já autenticado (password validada)
     * @return a String do token: header.payload.assinatura
     */
    public String generateToken(User user) {

        // Momento atual e momento da expiração
        Date agora = new Date();
        Date validoAte = new Date(agora.getTime() + expiration);

        return Jwts.builder()
                // "sub" — o claim standard que identifica o dono do token
                .subject(user.getEmail())

                // Claim personalizado: o role vai viajar dentro do token,
                // para o filtro poder autorizar sem ir à base de dados
                .claim("role", user.getRole().name())

                // "iat" (issued at) e "exp" (expiration)
                .issuedAt(agora)
                .expiration(validoAte)

                // Assina com a chave secreta → gera a 3ª parte do token
                .signWith(getSigningKey())

                // Fecha o builder e produz a String final
                .compact();
    }

    /**
     * Lê todos os claims (o payload) de dentro de um token.
     *
     * Este método faz DUAS coisas ao mesmo tempo:
     *   1. Valida a assinatura e a validade do token
     *   2. Devolve o conteúdo do payload
     *
     * Se o token for inválido (assinatura adulterada, expirado,
     * malformado), lança exceção — não devolve nada.
     *
     * private → é um detalhe interno; lá fora usam-se os métodos
     * mais específicos que vamos construir a seguir.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                // Diz ao parser qual a chave para verificar a assinatura
                .verifyWith(getSigningKey())
                // Fecha a configuração e produz o parser
                .build()
                // Lê o token: descodifica, verifica a assinatura e a validade
                .parseSignedClaims(token)
                // Devolve só a parte do payload
                .getPayload();
    }

    /**
     * Extrai o email (o claim "sub") de dentro do token.
     *
     * É assim que o filtro vai descobrir QUEM está a fazer o pedido.
     */
    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Extrai o role (claim personalizado "role") de dentro do token.
     *
     * É com este valor que o Spring Security vai decidir, mais tarde,
     * se o utilizador pode ou não aceder a cada endpoint.
     *
     * get("role", String.class):
     *   "role"       → o nome do claim (tal como o escrevemos no generateToken)
     *   String.class → o tipo em que queremos o valor
     */
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    /**
     * Verifica se um token é válido.
     *
     * Tenta ler os claims: se conseguir, o token é genuíno e está
     * dentro da validade → true.
     * Se lançar qualquer exceção (assinatura inválida, expirado,
     * malformado) → false.
     *
     * @return true se o token for válido, false caso contrário
     */
    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
