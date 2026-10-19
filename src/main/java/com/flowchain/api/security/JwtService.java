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
}
