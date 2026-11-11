package com.flowchain.api.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// Anotação do HIBERNATE (não do JPA) — preenche a data
// automaticamente no momento do INSERT
import org.hibernate.annotations.CreationTimestamp;

// LocalDate → só data (sem hora) | LocalDateTime → data + hora
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.UUID;

/**
 * Entidade que representa um pedido semanal de uma unidade
 * a um fornecedor.
 *
 * Cobre os requisitos RF01, RF02 e RF03: cada unidade submete
 * pedidos semanais ao fornecedor externo, ao armazém ou a outros
 * fornecedores (ex: Recheio, Beira Sumos).
 *
 * Vem da tabela WEEKLY_ORDER do modelo de dados (grupo Pedidos).
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "weekly_orders")
public class WeeklyOrder {

    /**
     * Identificador único do pedido.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /**
     * A unidade que fez o pedido.
     *
     * @ManyToOne → MUITOS pedidos apontam para UMA unidade
     * (a Gamma faz muitos pedidos ao longo do tempo).
     *
     * @JoinColumn → define a coluna FK na tabela weekly_orders:
     * name = "unit_id"  → nome da coluna (igual ao modelo de dados)
     * nullable = false  → todo o pedido TEM de ter uma unidade
     *
     * O tipo do campo é a ENTIDADE Unit, não UUID: o Hibernate
     * guarda o UUID na coluna, mas no Java trabalhamos com o objeto.
     */
    @ManyToOne
    @JoinColumn(name = "unit_id", nullable = false)
    private Unit unit;

    /**
     * O fornecedor a quem o pedido se destina.
     *
     * Mesma lógica: MUITOS pedidos → UM fornecedor
     * (o Recheio recebe pedidos de várias unidades, várias semanas).
     */
    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    /**
     * A semana a que o pedido se refere.
     *
     * LocalDate (só data, sem hora) porque aqui a hora não
     * interessa — o que conta é a semana da operação.
     */
    @Column(name = "week_date", nullable = false)
    private LocalDate weekDate;

    /**
     * Estado atual do pedido: PENDING → CONFIRMED → DELIVERED.
     *
     * @Enumerated(EnumType.STRING) → guarda o enum como TEXTO na BD
     * ("PENDING" em vez de 0). Muito mais legível ao consultar a
     * tabela, e imune a problemas se a ordem do enum mudar.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    /**
     * Data e hora em que o pedido foi submetido.
     *
     * @CreationTimestamp → preenchido automaticamente no INSERT.
     * LocalDateTime porque aqui a hora interessa: saber que o
     * pedido entrou às 9h ou às 18h tem valor operacional.
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
