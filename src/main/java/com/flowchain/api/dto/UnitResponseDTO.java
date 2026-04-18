package com.flowchain.api.dto;
// Lombok — gera getters, setters, toString, equals e hashCode
import lombok.Data;

// Lombok — gera construtor vazio
// Necessário para o Jackson serializar este objeto para JSON
import lombok.NoArgsConstructor;

// Lombok — gera construtor com todos os campos
// Usado para criar o DTO a partir da entidade Unit no Service
import lombok.AllArgsConstructor;

// UUID — tipo de dado do ID da unidade
// Gerado automaticamente pelo PostgreSQL
import java.util.UUID;

/**
 * DTO (Data Transfer Object) para devolver dados de uma unidade ao cliente.
 *
 * Este objeto representa o corpo (body) da resposta HTTP quando a API
 * devolve informações sobre uma unidade operacional.
 *
 * Exemplo do JSON que a API devolve:
 * {
 *   "id": "123e4567-e89b-12d3-a456-426614174000",
 *   "name": "Alpha",
 *   "type": "RESTAURANT",
 *   "isPrepCenter": true
 * }
 *
 * Diferença para o UnitRequestDTO:
 *   - Tem o campo ID — gerado automaticamente pelo PostgreSQL
 *   - Não tem anotações de validação — são só para dados de entrada
 *   - O type é String em vez de UnitType — mais legível para o cliente
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnitResponseDTO {

    /**
     * Identificador único da unidade.
     * Gerado automaticamente pelo PostgreSQL no formato UUID.
     * Exemplo: 123e4567-e89b-12d3-a456-426614174000
     */
    private UUID id;

    /**
     * Nome da unidade operacional.
     * Exemplos: Alpha, Beta, Gamma, Delta, Epsilon, Zeta, Eta.
     */
    private String name;

    /**
     * Tipo da unidade como String.
     * Usamos String em vez de UnitType para o cliente não precisar
     * de conhecer os valores internos do enum.
     * Valores possíveis: "RESTAURANT", "TAKEAWAY", "RESTAURANT_TAKEAWAY"
     */
    private String type;

    /**
     * Indica se esta unidade é um centro de preparação.
     * true  → Alpha (carnes) ou Beta (preparados)
     * false → todas as outras unidades
     */
    private boolean isPrepCenter;
}
