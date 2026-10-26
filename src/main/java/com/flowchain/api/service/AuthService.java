package com.flowchain.api.service;

// @Service — marca esta classe como um bean de lógica de negócio.
// O Spring cria a instância no arranque e guarda-a no seu contentor.
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.flowchain.api.repository.UserRepository;
import com.flowchain.api.security.JwtService;
import com.flowchain.api.dto.LoginRequestDTO;
import com.flowchain.api.entity.User;
import com.flowchain.api.dto.LoginResponseDTO;

/**
 * Lógica de negócio da autenticação (login).
 */
@Service
public class AuthService {
    /**
     * As três ferramentas de que esta classe precisa.
     *
     * final → depois de atribuídas no construtor, nunca mudam.
     * Garante que ninguém as troca a meio da execução.
     */
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    /**
     * Construtor — é aqui que o Spring entrega as dependências.
     *
     * No arranque, o Spring vê que o AuthService precisa destes três
     * objetos, vai buscá-los ao contentor, e chama este construtor
     * com eles. Nunca escrevemos "new UserRepository()".
     */
    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /**
     * Autentica um utilizador.
     *
     * @param dto email e password enviados pelo cliente
     */
    public LoginResponseDTO login(LoginRequestDTO dto){
        // PASSO 1 — Procurar o utilizador pelo email.
        //
        // findByEmail devolve Optional<User>: pode ter um User
        // lá dentro, ou estar vazio (email não registado).
        //
        // orElseThrow → "se tiveres valor, dá-mo;
        //                se estiveres vazio, lança esta exceção"
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Credenciais inválidas"));

        // PASSO 2 — Validar a password.
        //
        // matches(passwordEmTexto, hashGuardado) → devolve true ou false
        //
        // O "!" é a negação: a condição lê-se
        // "SE a password NÃO bater certo, então..."
        if (!passwordEncoder.matches(dto.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Credencias invalidas");
        }

        // PASSO 3 — Credenciais válidas → gerar o token.
        //
        // O AuthService não sabe nada de assinaturas nem de claims:
        // delega essa mecânica ao JwtService.
        String token = jwtService.generateToken(user);

        // PASSO 4 — Montar a resposta pública.
        //
        // Usamos o construtor gerado pelo @AllArgsConstructor do Lombok.
        // A ordem dos argumentos segue a ordem dos campos no DTO:
        // token, type, email, name, role
        //
        // Repara no que NÃO vai aqui: o passwordHash.
        // A entidade é interna; o DTO é a porta pública.
        return  new LoginResponseDTO(
                token,
                "Bearer",
                user.getEmail(),
                user.getName(),
                user.getRole().name()
        );
    }
}
