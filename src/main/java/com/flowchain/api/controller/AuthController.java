package com.flowchain.api.controller;

// Anotações REST do Spring:
// @RestController → marca a classe como controller REST
// @RequestMapping → define o caminho base da URL
// @PostMapping    → endpoint para pedidos POST
// @RequestBody    → extrai o corpo (JSON) do pedido
import org.springframework.web.bind.annotation.*;

// ResponseEntity → a resposta HTTP completa (corpo + status)
import org.springframework.http.ResponseEntity;

// @Valid → ativa as validações declaradas no DTO (@NotBlank, @Email)
import jakarta.validation.Valid;

import com.flowchain.api.dto.LoginRequestDTO;
import com.flowchain.api.dto.LoginResponseDTO;
import com.flowchain.api.service.AuthService;

/**
 * Controller dos endpoints de autenticação.
 *
 * Endpoint disponível:
 *   POST /api/auth/login → autentica e devolve o token JWT
 *
 * Não tem lógica de negócio: recebe o pedido, chama o
 * AuthService e devolve a resposta.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    /**
     * Injeção de dependência por construtor.
     * O Spring vai ao contentor buscar o AuthService e passa-o aqui.
     */
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Autentica um utilizador e devolve o token JWT.
     *
     * HTTP Method: POST
     * URL: /api/auth/login
     * Body: { "email": "...", "password": "..." }
     * Resposta: 200 OK + token e dados do utilizador
     *           400 Bad Request se as credenciais forem inválidas
     *
     * @Valid → se o email vier vazio ou mal formatado, o Spring
     * devolve 400 automaticamente, sem este método sequer correr.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {

        // Toda a lógica está no Service
        LoginResponseDTO response = authService.login(dto);

        // 200 OK + o corpo com o token
        return ResponseEntity.ok(response);
    }
}
