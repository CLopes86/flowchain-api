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
     * <p>
     * O Spring vê que esta classe precisa de um UserRepository e de
     * um PasswordEncoder, procura-os no contentor, e passa-os aqui.
     * Nunca escrevemos "new" — é o Spring que liga os fios.
     * <p>
     * final → garante que não são substituídos depois de injetados.
     */
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
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
    public void run(String... args) {
        String adminEmail = "admin@flowchain.com";

        if (!userRepository.existsByEmail(adminEmail)) {

            User admin = new User();
            admin.setName("Patroa");
            admin.setEmail(adminEmail);
            admin.setPasswordHash(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);

            userRepository.save(admin);

            System.out.println(">>> Utilizador ADMIN criado: " + adminEmail);
        }

        // ---------- Segundo utilizador: COURIER (para testar autorização) ----------

        // ---------- Segundo utilizador: COURIER (para testar autorização) ----------

        String courierEmail = "estafeta@flowchain.com";

        // Mesma guarda de idempotência: só cria se ainda não existir
        if (!userRepository.existsByEmail(courierEmail)) {

            User courier = new User();
            courier.setName("Estafeta");
            courier.setEmail(courierEmail);
            courier.setPasswordHash(passwordEncoder.encode("estafeta123"));
            courier.setRole(Role.COURIER);

            // unit fica a null por agora — mais tarde podemos associá-lo à Beta
            // (que é a base do estafeta, segundo a Análise de Requisitos)

            userRepository.save(courier);

            System.out.println(">>> Utilizador COURIER criado: " + courierEmail);
        }
    }
}
