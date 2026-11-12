package com.flowchain.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// BigDecimal — número decimal EXATO, para quantidades e dinheiro
import java.math.BigDecimal;

import java.util.UUID;

/**
 * Entidade que representa uma linha de produto dentro de um
 * pedido semanal.
 *
 * Um WeeklyOrder (cabeçalho) tem várias OrderItem (linhas):
 *   Pedido Gamma → Recheio
 *     • Bacalhau ...... 20 kg     ← uma OrderItem
 *     • Arroz ......... 15 kg     ← outra OrderItem
 *
 * Vem da tabela ORDER_ITEM do modelo de dados (grupo Pedidos).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_items")
public class OrderItem {
    /**
     * Identificador único da linha de pedido.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /**
     * O pedido a que esta linha pertence.
     *
     * @ManyToOne → MUITAS linhas apontam para UM pedido
     * (o pedido da Gamma tem 4 linhas, todas a apontar para ele).
     *
     * @JoinColumn → cria a coluna FK "weekly_order_id" nesta tabela.
     * nullable = false → uma linha SEM pedido não faz sentido nenhum.
     */
    @ManyToOne
    @JoinColumn(name = "weekly_order_id", nullable = false)
    private WeeklyOrder weeklyOrder;

    /**
     * O produto pedido nesta linha.
     *
     * @ManyToOne → MUITAS linhas apontam para UM produto
     * (o bacalhau aparece em pedidos de várias unidades,
     *  em várias semanas).
     */
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /**
     * Quantidade pedida.
     *
     * BigDecimal e NUNCA double: o double guarda os números em
     * binário e perde precisão nos decimais (0.1 + 0.2 dá
     * 0.30000000000000004). Numa lista consolidada que soma
     * centenas de linhas, esses erros acumulam-se.
     *
     * precision = 10 → no máximo 10 dígitos no total
     * scale     = 2  → dois deles são casas decimais
     * Ou seja: até 99 999 999,99
     */
    @Column(name = "quantity", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity;

    /**
     * Observações da linha. Exemplos reais: "se possível",
     * "urgente", "só se houver da marca X".
     *
     * columnDefinition = "TEXT" → diz ao Hibernate para usar o tipo
     * TEXT do PostgreSQL em vez do varchar(255) por defeito.
     * TEXT não tem limite de tamanho — adequado a texto livre.
     *
     * Sem nullable = false → o campo é OPCIONAL (a maioria das
     * linhas não leva observação nenhuma).
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}

