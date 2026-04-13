package com.flowchain.api.entity;

/**
 * Tipos possíveis de uma unidade operacional.
 * RESTAURANT         → apenas restaurante com serviço de mesa
 * TAKEAWAY           → apenas loja takeaway
 * RESTAURANT_TAKEAWAY → restaurante com serviço takeaway
 */

public enum UnitType {

    // Apenas restaurante com serviço de mesa
    RESTAURANT,

    // Apenas loja takeaway
    TAKEAWAY,

    // Restaurante com serviço takeaway
    RESTAURANT_TAKEAWAY
}
