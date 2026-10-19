package com.flowchain.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

/**
 * DTO que representa o corpo do pedido de login.
 *
 * O cliente envia:
 * {
 *   "email": "admin@flowchain.com",
 *   "password": "admin123"
 * }
 *
 * Só estes dois campos — nada mais entra por esta porta.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {
    /**
     * Email do utilizador — funciona como "username" no login.
     *
     * @NotBlank → não pode ser null, nem "", nem só espaços
     * @Email    → tem de ter formato de email (algo@algo.algo)
     *
     * Se falhar, o Spring devolve 400 Bad Request automaticamente,
     * antes sequer de o Controller ser chamado.
     */
    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Formato de email invalido")

    private  String email;

    /**
     * Password em texto simples — tal como o utilizador a escreveu.
     *
     * Vai viajar no corpo do pedido e o servidor vai compará-la
     * com o hash BCrypt guardado na base de dados.
     * NUNCA é guardada nem registada em logs.
     */
    @NotBlank(message = "A password é obrigatoria")
    private String password;

}
