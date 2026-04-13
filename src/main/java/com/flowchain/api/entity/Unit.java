package com.flowchain.api.entity;

// Importa todas as anotações JPA necessárias para mapear a classe à base de dados
// @Entity, @Table, @Id, @GeneratedValue, @Column, @Enumerated, EnumType
import jakarta.persistence.*;

// Lombok — gera automaticamente getters, setters e toString para todos os campos
import lombok.Data;

// Lombok — gera automaticamente um construtor sem argumentos (construtor vazio)
// O JPA exige sempre um construtor vazio para conseguir criar objetos da entidade
import lombok.NoArgsConstructor;

// Lombok — gera automaticamente um construtor com todos os campos como argumentos
// Útil para criar objetos com todos os dados de uma vez
import lombok.AllArgsConstructor;

// UUID — tipo de dado para identificadores únicos universais
// Formato: 123e4567-e89b-12d3-a456-426614174000
// Muito mais seguro do que IDs numéricos sequenciais (1, 2, 3...)
// porque é impossível de adivinhar
import java.util.UUID;

/**
 * Entidade que representa uma unidade operacional da cadeia de restauração.
 *
 * Uma unidade pode ser um restaurante, uma loja takeaway ou ambos.
 * O sistema gere 7 unidades: Alpha, Beta, Gamma, Delta, Epsilon, Zeta e Eta.
 *
 * Alpha e Beta têm papéis especiais:
 *   - Alpha → centro de porcionamento de bacalhau e entrecosto
 *   - Beta  → hub operacional (sopa, arroz de pato, bacalhau da quinta, batata frita)
 *             e base do estafeta
 *
 * Esta classe é mapeada automaticamente pelo Hibernate para a tabela 'units'
 * na base de dados PostgreSQL.
 */

// @Data — anotação do Lombok que gera automaticamente:
//   - getters para todos os campos (ex: getId(), getName())
//   - setters para todos os campos (ex: setId(), setName())
//   - toString() para mostrar os dados do objeto como texto
//   - equals() e hashCode() para comparar objetos
@Data

// @NoArgsConstructor — gera um construtor vazio: new Unit()
// O JPA/Hibernate exige este construtor para conseguir
// criar instâncias da entidade quando lê dados da base de dados
@NoArgsConstructor

// @AllArgsConstructor — gera um construtor com todos os campos:
// new Unit(id, name, type, isPrepCenter)
// Útil para criar objetos com todos os dados preenchidos de uma vez
@AllArgsConstructor

// @Entity — diz ao Hibernate que esta classe Java é uma entidade JPA
// ou seja, que deve ser mapeada para uma tabela na base de dados
@Entity

// @Table — define o nome da tabela na base de dados
// Se não colocasses esta anotação, o Hibernate usaria o nome da classe
// em minúsculas como nome da tabela (unit)
// Usamos o plural 'units' por convenção
@Table(name = "units")
public class Unit {

    /**
     * Identificador único da unidade.
     *
     * UUID (Universally Unique Identifier) é um código de 36 caracteres
     * gerado aleatoriamente. Exemplo: 123e4567-e89b-12d3-a456-426614174000
     *
     * Vantagens sobre IDs numéricos (1, 2, 3...):
     *   - Impossível de adivinhar → mais seguro
     *   - Único a nível mundial → sem conflitos em sistemas distribuídos
     *   - Não revela quantos registos existem na base de dados
     */

    // @Id — marca este campo como chave primária da tabela
    // A chave primária identifica unicamente cada linha da tabela
    @Id

    // @GeneratedValue — o ID é gerado automaticamente
    // strategy = GenerationType.UUID → o PostgreSQL gera o UUID automaticamente
    // Não precisas de definir o ID manualmente ao criar uma unidade
    @GeneratedValue(strategy = GenerationType.UUID)

    // @Column — define as propriedades da coluna na base de dados
    // updatable = false → o ID nunca pode ser alterado depois de criado
    // nullable = false  → o ID não pode ser nulo (obrigatório)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /**
     * Nome da unidade operacional.
     * Exemplos: Alpha, Beta, Gamma, Delta, Epsilon, Zeta, Eta.
     */

    // nullable = false → o nome é obrigatório, não pode ser vazio
    // unique = true    → não podem existir duas unidades com o mesmo nome
    //                    o PostgreSQL garante isto automaticamente
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    /**
     * Tipo da unidade operacional.
     * Usa o enum UnitType que garante que só valores válidos são aceites:
     *   - RESTAURANT          → apenas restaurante com serviço de mesa
     *   - TAKEAWAY            → apenas loja takeaway
     *   - RESTAURANT_TAKEAWAY → restaurante com serviço takeaway
     */

    // @Enumerated(EnumType.STRING) → guarda o enum como texto na base de dados
    // Exemplo: guarda "RESTAURANT" em vez de 0, "TAKEAWAY" em vez de 1
    // Muito melhor que guardar números porque é legível diretamente na BD
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private UnitType type;

    /**
     * Indica se esta unidade é um centro de preparação de produtos.
     *
     * Apenas duas unidades são centros de preparação:
     *   - Alpha (isPrepCenter = true) → porciona bacalhau e entrecosto
     *   - Beta  (isPrepCenter = true) → prepara sopa, arroz de pato,
     *                                   bacalhau da quinta e batata frita
     *
     * As restantes unidades recebem os produtos já preparados (isPrepCenter = false).
     */

    // boolean → tipo de dado que só aceita verdadeiro (true) ou falso (false)
    // nullable = false → este campo é obrigatório, não pode ser nulo
    @Column(name = "is_prep_center", nullable = false)
    private boolean isPrepCenter;
}