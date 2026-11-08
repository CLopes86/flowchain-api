package com.flowchain.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.UUID;

/**
 * Entidade que representa um produto do catálogo da cadeia.
 *
 * Exemplos reais: Bacalhau, Carvão, Arroz, Batata frita, Embalagens.
 * Os produtos aparecem nas linhas dos pedidos semanais (ORDER_ITEM),
 * nos movimentos de stock do armazém e nas distribuições.
 *
 * Vem da tabela PRODUCT do modelo de dados (grupo Referências).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    /**
     * Identificador único do produto.
     * Gerado automaticamente pelo PostgreSQL em formato UUID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /**
     * Nome do produto.
     * Exemplos: "Bacalhau", "Carvão", "Arroz", "Batata frita".
     *
     * unique = true → impede produtos duplicados no catálogo
     */
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    /**
     * Categoria do produto.
     * Exemplos: "Carne", "Bebida", "Embalagem", "Preparado".
     *
     * String em vez de enum porque o catálogo cresce com o negócio:
     * uma categoria nova não deve obrigar a alterar e recompilar código.
     */
    @Column(name = "category", nullable = false)
    private String category;

    /**
     * Unidade de medida usada nos pedidos deste produto.
     * Exemplos: "kg", "emb.", "cx.", "un.".
     *
     * É o que dá sentido às quantidades: pedir "5" de bacalhau
     * só significa alguma coisa se soubermos que são 5 kg.
     */
    @Column(name = "unit_of_measure", nullable = false)
    private String unitOfMeasure;
}


