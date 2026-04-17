package com.flowchain.api.repository;

// Importa o JpaRepository do Spring Data JPA
// JpaRepository já tem todos os métodos básicos de acesso à base de dados
// findAll(), findById(), save(), delete(), count(), etc.
import org.springframework.data.jpa.repository.JpaRepository;

// Importa a entidade Unit que este repository vai gerir
import com.flowchain.api.entity.Unit;

// Importa UUID — o tipo de dado do ID da entidade Unit
import java.util.UUID;

// Importa Optional — usado para retornar valores que podem ou não existir
// Evita o NullPointerException quando um registo não é encontrado
import java.util.Optional;

/**
 * Repository responsável pelo acesso à base de dados da entidade Unit.
 *
 * Estende JpaRepository que recebe dois parâmetros:
 *   - Unit → a entidade que este repository gere
 *   - UUID → o tipo de dado do ID da entidade
 *
 * O Spring Data JPA gera automaticamente a implementação desta interface
 * em tempo de execução — não precisas de escrever SQL nem implementar
 * os métodos manualmente.
 */
public interface UnitRepository extends JpaRepository<Unit, UUID> {
    /**
     * Busca uma unidade pelo nome.
     *
     * O Spring Data JPA gera automaticamente o SQL a partir do nome do método:
     * findByName → SELECT * FROM units WHERE name = ?
     *
     * Devolve Optional<Unit> porque a unidade pode ou não existir.
     * Se existir → Optional.of(unit)
     * Se não existir → Optional.empty()
     *
     * @param name Nome da unidade a pesquisar (ex: "Alpha", "Beta")
     * @return Optional com a unidade encontrada ou vazio se não existir
     */
    Optional<Unit> findByName(String name);

    /**
     * Verifica se já existe uma unidade com o nome fornecido.
     *
     * Útil para validar se um nome já está em uso antes de criar uma nova unidade.
     * O Spring gera automaticamente:
     * existsByName → SELECT COUNT(*) > 0 FROM units WHERE name = ?
     *
     * @param name Nome a verificar
     * @return true se existir uma unidade com esse nome, false caso contrário
     */
    boolean existsByName(String name);
}
