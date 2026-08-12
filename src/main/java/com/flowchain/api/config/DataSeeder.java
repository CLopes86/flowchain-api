package com.flowchain.api.config;

// CommandLineRunner — interface do Spring Boot: o método run()
// é executado UMA VEZ, logo a seguir ao arranque da aplicação,
// já com o contentor montado e a ligação à BD pronta.
import org.springframework.boot.CommandLineRunner;

// @Component — marca a classe como um bean gerido pelo Spring.
// É a anotação "genérica" da família: @Service, @Repository e
// @RestController são especializações dela. Usamos @Component
// porque esta classe não é lógica de negócio nem acesso a dados.
import org.springframework.stereotype.Component;

// A interface do codificador de passwords (o bean BCrypt que criámos)
import org.springframework.security.crypto.password.PasswordEncoder;

import com.flowchain.api.entity.User;
import com.flowchain.api.entity.Role;
import com.flowchain.api.repository.UserRepository;

/**
 * Cria dados iniciais no arranque da aplicação (data seeding).
 *
 * Resolve o problema do "ovo e galinha": sem nenhum utilizador
 * na base de dados, não haveria como fazer login nem testar a
 * autenticação. Esta classe cria o primeiro ADMIN.
 *
 * Só cria se ainda não existir — pode reiniciar a app à vontade.
 */
@Component
public class DataSeeder implements CommandLineRunner {
    /**
     * Injeção de dependência por construtor.
     *
     * O Spring vê que esta classe precisa de um UserRepository e de
     * um PasswordEncoder, procura-os no contentor, e passa-os aqui.
     * Nunca escrevemos "new" — é o Spring que liga os fios.
     *
     * final → garante que não são substituídos depois de injetados.
     */
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Executado automaticamente após o arranque da aplicação.
     *
     * @Override → indica que estamos a implementar um método
     * declarado na interface CommandLineRunner.
     */
    @Override
    public void run(String... args){
        String  adminEmail = "admin@flowchain.com";

        // Só queremos saber SIM ou NÃO → existsByEmail é o método certo
        // (mais eficiente que findByEmail: a BD conta em vez de
        //  transportar a linha inteira)
        if (userRepository.existsByEmail(adminEmail)) {
            System.out.println(">>> Utilizador ADMIN já existe — seeding ignorado.");
            return;   // sai do método, não cria nada
        }

        // Constrói o novo utilizador
        User admin = new User();
        admin.setName("Patroa");
        admin.setEmail(adminEmail);

        // AQUI ACONTECE A MAGIA: a password é transformada em hash.
        // O que fica guardado na BD nunca é "admin123",
        // mas sim algo como "$2a$10$N9qo8uLOickgx2ZM..."
        admin.setPasswordHash(passwordEncoder.encode("admin123"));

        admin.setRole(Role.ADMIN);

        // unit fica a null — ADMIN tem visão transversal,
        // não pertence a nenhuma unidade (nullable = true no @JoinColumn)

        // save() → método que veio de borla do JpaRepository.
        // Spring Data → Hibernate escreve o INSERT → PostgreSQL executa
        userRepository.save(admin);

        System.out.println(">>> Utilizador ADMIN criado: " + adminEmail);
    }
}
