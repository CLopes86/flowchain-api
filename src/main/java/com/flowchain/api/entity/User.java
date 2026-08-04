package com.flowchain.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// Anotação do HIBERNATE (não do JPA!) — preenche o campo
// automaticamente com a data/hora do momento da criação
import org.hibernate.annotations.CreationTimestamp;

// Tipo moderno do Java para data + hora (equivale ao TIMESTAMP da BD)
import java.time.LocalDateTime;

import java.util.UUID;

/**
 * Entidade que representa um utilizador do sistema FlowChain.
 *
 * Vem da tabela USER do modelo de dados. Cada utilizador tem um role
 * (ADMIN, WAREHOUSE, UNIT, COURIER) que define as suas permissões,
 * e pode estar associado a uma unidade operacional (unit_id).
 *
 * IMPORTANTE: a tabela chama-se "users" (plural) porque "user"
 * é palavra reservada do PostgreSQL — usar "user" causaria erros de SQL.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")

public class User {

    // Igual à Unit — UUID gerado automaticamente pelo PostgreSQL
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    // Nome do utilizador — obrigatório
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Email — vai funcionar como o "username" do login.
     * unique = true → não podem existir duas contas com o mesmo email.
     * É por este campo que o AuthService vai procurar o utilizador
     * quando alguém fizer login.
     */
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /**
     * Hash BCrypt da password — NUNCA a password original!
     * O nome do campo (passwordHash) lembra-nos a regra:
     * o que entra aqui já vem transformado pelo BCrypt.
     */
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    // Igual ao UnitType da Unit — guardado como texto legível na BD
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    /**
     * A unidade a que este utilizador pertence.
     *
     * @ManyToOne → MUITOS utilizadores apontam para UMA unidade
     * (vários funcionários da Gamma → a mesma Unit Gamma).
     *
     * @JoinColumn → define a coluna FK na tabela users:
     * name = "unit_id" → o nome da coluna (igual ao modelo de dados)
     * nullable = true → pode ser nulo (Admin e Armazém não têm unidade)
     *
     * O tipo do campo é a ENTIDADE Unit, não UUID! O Hibernate
     * guarda o UUID na coluna, mas no Java trabalhamos com o objeto.
     */
    @ManyToOne
    @JoinColumn(name = "unit_id", nullable = true)
    private Unit unit;

    /**
     * Data e hora de criação da conta.
     *
     * @CreationTimestamp (Hibernate) → preenchido automaticamente
     * no momento do INSERT — nunca fazemos setCreatedAt() à mão.
     * updatable = false → nunca muda depois de criado.
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
