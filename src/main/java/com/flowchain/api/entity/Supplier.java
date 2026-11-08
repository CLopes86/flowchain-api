package com.flowchain.api.entity;


// Todas as anotações JPA: @Entity, @Table, @Id, @GeneratedValue, @Column
import jakarta.persistence.*;

// Lombok — gera getters, setters, toString, equals e hashCode
import lombok.Data;

// Lombok — gera o construtor vazio (exigido pelo JPA/Hibernate)
import lombok.NoArgsConstructor;

// Lombok — gera o construtor com todos os campos
import lombok.AllArgsConstructor;

// UUID — tipo do identificador único
import java.util.UUID;

/**
 * Entidade que representa um fornecedor externo.
 *
 * Exemplos reais da operação: Recheio, Beira Sumos.
 * As unidades submetem pedidos semanais a estes fornecedores,
 * e o Admin consulta a lista consolidada por fornecedor (RF14).
 *
 * Vem da tabela SUPPLIER do modelo de dados (grupo Referências).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "suppliers")

public class Supplier {
    /**
     * Identificador único do fornecedor.
     * Gerado automaticamente pelo PostgreSQL em formato UUID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private UUID id;

    /**
     * Nome do fornecedor.
     *
     * nullable = false → obrigatório
     * unique = true    → não podem existir dois fornecedores
     *                    com o mesmo nome (evita duplicados
     *                    criados por engano)
     */
    @Column(name = "name", nullable = false, unique = true)
    private String name;

}
