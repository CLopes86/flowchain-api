package com.flowchain.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


/**
 * DTO que representa a resposta de um login bem-sucedido.
 *
 * A API devolve:
 * {
 *   "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBmbG93Y2hhaW4uY29tIn0...",
 *   "type": "Bearer",
 *   "email": "admin@flowchain.com",
 *   "name": "Patroa",
 *   "role": "ADMIN"
 * }
 *
 * Nota: NUNCA inclui o passwordHash nem qualquer campo interno.
 * O DTO é a porta pública; a entidade User fica cá dentro.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    /**
     * O token JWT gerado pelo JwtService.
     * O cliente guarda-o e envia-o em cada pedido seguinte,
     * no header: Authorization: Bearer <token>
     */
    private String token;

    /**
     * O tipo do token — sempre "Bearer".
     *
     * "Bearer" significa "portador": quem apresentar este token
     * é tratado como o seu dono. É a convenção do protocolo HTTP
     * para autenticação por token.
     */
    private String type;

    /**
     * Email do utilizador autenticado.
     * Útil para a app confirmar com que conta entrou.
     */
    private String email;

    /**
     * Nome do utilizador — para a interface mostrar "Olá, Patroa".
     */
    private String name;

    /**
     * Role em String (ADMIN, WAREHOUSE, UNIT, COURIER).
     *
     * Permite à app adaptar o menu: um COURIER não vê
     * as opções de gestão de utilizadores, por exemplo.
     *
     * String em vez do enum Role — o cliente não precisa
     * de conhecer os tipos internos do nosso Java.
     * (Mesma decisão que tomaste no UnitResponseDTO com o type.)
     */
    private String role;




}
