package com.flowchain.api.entity;

/**
 * Papéis (roles) dos utilizadores do FlowChain.
 *
 * O role determina o que cada utilizador pode fazer no sistema.
 * É este valor que vai viajar dentro do token JWT (claim "role")
 * e que o Spring Security vai ler para autorizar cada pedido.
 *
 * Vem diretamente do modelo de dados (tabela USER, campo role)
 * e da Análise de Requisitos (secção 3 — Atores do Sistema).
 */

public enum Role {

    // Patroa — supervisão geral, listas consolidadas, gestão de utilizadores
    ADMIN,

    // Armazém — stock, distribuições semanais, confirmação de entregas
    WAREHOUSE,

    // As 7 unidades — pedidos semanais, controlo de frango, pagamentos
    UNIT,

    // Estafeta — transferências urgentes entre unidades
    COURIER
}
