package com.flowchain.api.service;

// Importa a anotação @Service do Spring
// Marca esta classe como um componente de serviço
// O Spring gere automaticamente a sua criação e ciclo de vida
import org.springframework.stereotype.Service;

// Importa a entidade Unit
import com.flowchain.api.entity.Unit;

// Importa o UnitRepository para aceder à base de dados
import com.flowchain.api.repository.UnitRepository;

// Importa List — para devolver listas de unidades
import java.util.List;

// Importa Optional — para devolver valores que podem ou não existir
import java.util.Optional;

// Importa UUID — tipo de dado do ID
import java.util.UUID;

/**
 * Service responsável pela lógica de negócio relacionada com as unidades operacionais.
 *
 * Esta camada fica entre o Controller e o Repository:
 *   Controller → UnitService → UnitRepository → Base de dados
 *
 * É aqui que ficam as validações, regras de negócio e processamento de dados.
 * O Controller não deve ter lógica de negócio — apenas chama o Service.
 */
@Service
public class UnitService {

    /**
     * Injeção de dependência do UnitRepository.
     *
     * O Spring injeta automaticamente uma instância do UnitRepository
     * quando cria o UnitService — isto chama-se Injeção de Dependência (DI).
     *
     * final → garante que o repository não pode ser substituído depois de injetado
     */
    private final UnitRepository unitRepository;

    /**
     * Construtor usado pelo Spring para injetar o UnitRepository.
     *
     * Usamos injeção por construtor em vez de @Autowired porque:
     *   - É mais seguro — os campos são final e imutáveis
     *   - É mais fácil de testar — podemos passar mocks no construtor
     *   - É a forma recomendada pelo Spring e pela indústria
     *
     * @param unitRepository Repository para acesso à base de dados
     */
    public UnitService(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    /**
     * Devolve a lista de todas as unidades operacionais.
     *
     * Chama o método findAll() do JpaRepository que executa:
     * SELECT * FROM units
     *
     * @return Lista com todas as unidades registadas
     */
    public List<Unit> getAllUnits() {
        return unitRepository.findAll();
    }
    /**
     * Busca uma unidade pelo seu ID.
     *
     * Devolve Optional<Unit> porque a unidade pode não existir.
     * O Controller decide o que fazer se não encontrar — normalmente
     * devolve um erro 404 (Not Found).
     *
     * @param id ID da unidade a pesquisar
     * @return Optional com a unidade encontrada ou vazio se não existir
     */
    public Optional<Unit> getUnitById(UUID id) {
        return unitRepository.findById(id);
    }

    /**
     * Cria uma nova unidade operacional.
     *
     * Antes de guardar, valida se já existe uma unidade com o mesmo nome.
     * Se existir, lança uma exceção para informar o Controller.
     *
     * @param unit Dados da nova unidade a criar
     * @return Unidade criada com o ID gerado automaticamente
     * @throws IllegalArgumentException se já existir uma unidade com o mesmo nome
     */
    public Unit createUnit(Unit unit){
        // Verifica se já existe uma unidade com o mesmo nome
        // Regra de negócio: não podem existir duas unidades com o mesmo nome
        if (unitRepository.existsByName(unit.getName())) {
            throw new IllegalArgumentException(
                    "Já existe uma unidade com o nome: " + unit.getName()
            );
        }

        // Guarda a unidade na base de dados
        // O JPA gera automaticamente o UUID e executa:
        // INSERT INTO units (id, name, type, is_prep_center) VALUES (?, ?, ?, ?)
        return unitRepository.save(unit);
    }

    /**
     * Apaga uma unidade pelo seu ID.
     *
     * @param id ID da unidade a apagar
     */
    public void deleteUnit(UUID id){
        unitRepository.deleteById(id);
    }

}
