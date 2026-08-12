package com.flowchain.api.repository;

// JpaRepository — interface base do Spring Data JPA.
// Só por a estendermos, ganhamos de borla:
// findAll(), findById(), save(), delete(), count()...
import org.springframework.data.jpa.repository.JpaRepository;

// A entidade que este repository gere
import com.flowchain.api.entity.User;

// UUID — o tipo da chave primária da entidade User
import java.util.UUID;

// Optional — "caixa" que pode conter um valor ou estar vazia.
// Evita o null e obriga quem chama a tratar o caso "não existe".
import java.util.Optional;

/**
 * Repository de acesso a dados da entidade User.
 *
 * JpaRepository<User, UUID>:
 *   - User → a entidade gerida
 *   - UUID → o tipo do ID dessa entidade
 *
 * Não escrevemos SQL nem implementação: o Spring Data JPA
 * gera tudo no arranque a partir dos nomes dos métodos.
 */

public interface UserRepository extends JpaRepository<User, UUID> {
    /**
     * Procura um utilizador pelo email — O MÉTODO DO LOGIN.
     *
     * Quando alguém fizer POST /api/auth/login, o AuthService
     * chama este método para encontrar a conta pelo email.
     *
     * Spring Data deduz do nome:
     * findByEmail → SELECT * FROM users WHERE email = ?
     *
     * Optional<User> porque o email pode não estar registado.
     */
    Optional<User> findByEmail(String email);

    /**
     * Verifica se já existe conta com este email.
     *
     * Para o registo de utilizadores (RF18): impedir duplicados
     * antes de tentar inserir — mesma lógica do existsByName
     * que já usas no UnitRepository.
     *
     * existsByEmail → SELECT COUNT(*) > 0 FROM users WHERE email = ?
     */
    boolean existsByEmail(String email);
}
