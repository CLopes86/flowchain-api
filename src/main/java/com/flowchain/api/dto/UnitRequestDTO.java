package com.flowchain.api.dto;

// Lombok — gera getters, setters, toString, equals e hashCode automaticamente
import lombok.Data;

// Lombok — gera construtor vazio
// Necessário para o Jackson deserializar o JSON para este objeto
// Jackson é a biblioteca que converte JSON → Java e Java → JSON
import lombok.NoArgsConstructor;

// Lombok — gera construtor com todos os campos
import lombok.AllArgsConstructor;

// Importa as anotações de validação
// Usadas para garantir que os dados enviados pelo cliente são válidos
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// Importa o enum UnitType para validar o tipo de unidade
import com.flowchain.api.entity.UnitType;

/**
 * DTO (Data Transfer Object) para receber dados de criação de uma unidade.
 *
 * Este objeto representa o corpo (body) do pedido HTTP quando o cliente
 * quer criar uma nova unidade operacional.
 *
 * Exemplo do JSON que o cliente envia:
 * {
 *   "name": "Alpha",
 *   "type": "RESTAURANT",
 *   "isPrepCenter": true
 * }
 *
 * Nota: O ID não é enviado pelo cliente — é gerado automaticamente
 * pelo PostgreSQL quando a unidade é criada.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnitRequestDTO {

    /**
     * Nome da unidade operacional.
     *
     * @NotBlank → validação que garante que o nome não é:
     *   - null (vazio)
     *   - "" (string vazia)
     *   - "   " (só espaços)
     * Se o cliente enviar um nome inválido, a API devolve automaticamente
     * um erro 400 (Bad Request) com a mensagem definida aqui.
     */
    @NotBlank(message = "O nome da unidade é obrigatório")
    private String name;

    /**
     * Tipo da unidade operacional.
     *
     * @NotNull → garante que o tipo não é null
     * Valores aceites: RESTAURANT, TAKEAWAY, RESTAURANT_TAKEAWAY
     * Se o cliente enviar um tipo inválido, a API devolve um erro 400.
     */
    @NotNull(message = "O tipo da unidade é obrigatório")
    private UnitType type;

    /**
     * Indica se esta unidade é um centro de preparação.
     *
     * true  → Alpha (carnes) ou Beta (preparados)
     * false → todas as outras unidades
     *
     * boolean primitivo → por defeito é false se não for enviado
     * Não precisa de @NotNull porque boolean primitivo nunca é null
     */
    private boolean prepCenter;
}
