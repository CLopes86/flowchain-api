package com.flowchain.api.entity;

/**
 * Estados possíveis de um pedido semanal.
 *
 * Representa o ciclo de vida do pedido na operação real:
 *   PENDING   → submetido pela unidade, ainda não tratado
 *   CONFIRMED → Admin/Armazém confirmou que vai ser atendido
 *   DELIVERED → produtos entregues na unidade, pedido fechado
 *
 * A ordem natural é PENDING → CONFIRMED → DELIVERED.
 * Vem do campo status da tabela WEEKLY_ORDER (modelo de dados).
 */

public enum OrderStatus {

    // Pedido submetido pela unidade, à espera de tratamento
    PENDING,

    // Confirmado pelo Admin ou Armazém
    CONFIRMED,

    // Produtos entregues na unidade
    DELIVERED

}
