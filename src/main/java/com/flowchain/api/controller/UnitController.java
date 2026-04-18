package com.flowchain.api.controller;

// Importa as anotações REST do Spring
// @RestController → marca a classe como Controller REST
// @RequestMapping → define o caminho base da URL
// @GetMapping → endpoint para pedidos GET
// @PostMapping → endpoint para pedidos POST
// @DeleteMapping → endpoint para pedidos DELETE
// @PathVariable → extrai valor da URL
// @RequestBody → extrai o corpo do pedido HTTP
import org.springframework.web.bind.annotation.*;

// ResponseEntity → representa a resposta HTTP completa
// (corpo + headers + código de status)
import org.springframework.http.ResponseEntity;

// @Valid → activa as validações definidas no DTO
// (@NotBlank, @NotNull, etc.)
import jakarta.validation.Valid;

// Importa os DTOs que criámos
import com.flowchain.api.dto.UnitRequestDTO;
import com.flowchain.api.dto.UnitResponseDTO;

// Importa a entidade Unit
import com.flowchain.api.entity.Unit;

// Importa o UnitService — o Controller chama o Service
// nunca acede directamente ao Repository
import com.flowchain.api.service.UnitService;

// List → coleção ordenada de objetos
import java.util.List;

// UUID → tipo de dado do ID da unidade
import java.util.UUID;

// Stream API — para converter listas de entidades em listas de DTOs
// stream() → cria um fluxo de dados
// map() → transforma cada elemento
// toList() → converte de volta para lista
import java.util.stream.Collectors;

/**
 * Controller responsável pelos endpoints REST da entidade Unit.
 *
 * É a porta de entrada da API para operações relacionadas
 * com as unidades operacionais do FlowChain.
 *
 * Endpoints disponíveis:
 *   GET    /api/units      → lista todas as unidades
 *   GET    /api/units/{id} → busca uma unidade por ID
 *   POST   /api/units      → cria uma nova unidade
 *   DELETE /api/units/{id} → apaga uma unidade
 *
 * O Controller não tem lógica de negócio — apenas:
 *   1. Recebe o pedido HTTP
 *   2. Chama o Service
 *   3. Devolve a resposta HTTP
 */
@RestController
@RequestMapping("/api/units")
public class UnitController {

    /**
     * Injeção de dependência do UnitService.
     *
     * O Controller depende do Service — nunca do Repository directamente.
     * Isto garante a separação de responsabilidades:
     *   Controller → Service → Repository → Base de dados
     *
     * final → o service não pode ser substituído depois de injectado
     */
    private final UnitService unitService;

    /**
     * Construtor usado pelo Spring para injectar o UnitService.
     *
     * O Spring detecta automaticamente que o UnitController precisa
     * de um UnitService e injeta-o aqui — isto é Injecção de Dependência.
     *
     * @param unitService Service com a lógica de negócio das unidades
     */
    public UnitController(UnitService unitService) {
        this.unitService = unitService;
    }

    /**
     * Devolve a lista de todas as unidades operacionais.
     *
     * HTTP Method: GET
     * URL: /api/units
     * Resposta: 200 OK + lista de unidades em JSON
     *
     * Exemplo de resposta:
     * [
     *   { "id": "123...", "name": "Alpha", "type": "RESTAURANT", "isPrepCenter": true },
     *   { "id": "456...", "name": "Beta", "type": "RESTAURANT_TAKEAWAY", "isPrepCenter": true }
     * ]
     */
    @GetMapping
    public ResponseEntity<List<UnitResponseDTO>> getAllUnits() {

        // Chama o Service para buscar todas as unidades
        List<Unit> units = unitService.getAllUnits();

        // Converte a lista de entidades Unit para lista de DTOs
        // stream() → cria um fluxo de dados a partir da lista
        // map() → transforma cada Unit num UnitResponseDTO
        // collect() → converte o fluxo de volta para uma lista
        List<UnitResponseDTO> response = units.stream()
                .map(unit -> new UnitResponseDTO(
                        unit.getId(),
                        unit.getName(),
                        // .name() converte o enum UnitType para String
                        // ex: UnitType.RESTAURANT → "RESTAURANT"
                        unit.getType().name(),
                        unit.isPrepCenter()
                ))
                .collect(Collectors.toList());

        // Devolve 200 OK com a lista de DTOs
        return ResponseEntity.ok(response);
    }

    /**
     * Busca uma unidade pelo seu ID.
     *
     * HTTP Method: GET
     * URL: /api/units/{id}
     * Resposta: 200 OK + unidade em JSON
     *           404 Not Found se a unidade não existir
     *
     * @param id ID da unidade a buscar (extraído da URL)
     */
    @GetMapping("/{id}")
    public ResponseEntity<UnitResponseDTO> getUnitById(@PathVariable UUID id) {

        // Chama o Service para buscar a unidade pelo ID
        // Devolve Optional<Unit> — pode ou não existir
        return unitService.getUnitById(id)
                // Se existir → converte para DTO e devolve 200 OK
                .map(unit -> ResponseEntity.ok(new UnitResponseDTO(
                        unit.getId(),
                        unit.getName(),
                        unit.getType().name(),
                        unit.isPrepCenter()
                )))
                // Se não existir → devolve 404 Not Found
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Cria uma nova unidade operacional.
     *
     * HTTP Method: POST
     * URL: /api/units
     * Body: JSON com os dados da nova unidade (UnitRequestDTO)
     * Resposta: 201 Created + unidade criada em JSON
     *
     * @param dto Dados da nova unidade enviados pelo cliente
     *
     * @Valid → activa as validações do UnitRequestDTO
     * Se alguma validação falhar, o Spring devolve automaticamente 400 Bad Request
     */
    @PostMapping
    public ResponseEntity<UnitResponseDTO> createUnit(@Valid @RequestBody UnitRequestDTO dto) {

        // Converte o DTO para entidade Unit
        // O ID não é definido aqui — é gerado automaticamente pelo PostgreSQL
        Unit unit = new Unit();
        unit.setName(dto.getName());
        unit.setType(dto.getType());
        unit.setPrepCenter(dto.isPrepCenter());

        // Chama o Service para criar a unidade na base de dados
        Unit savedUnit = unitService.createUnit(unit);

        // Converte a entidade guardada para DTO de resposta
        UnitResponseDTO response = new UnitResponseDTO(
                savedUnit.getId(),
                savedUnit.getName(),
                savedUnit.getType().name(),
                savedUnit.isPrepCenter()
        );

        // Devolve 201 Created com o DTO da unidade criada
        // ResponseEntity.status(201).body(response) → código 201 + corpo JSON
        return ResponseEntity.status(201).body(response);
    }

    /**
     * Apaga uma unidade pelo seu ID.
     *
     * HTTP Method: DELETE
     * URL: /api/units/{id}
     * Resposta: 204 No Content (sem corpo na resposta)
     *
     * 204 No Content → operação bem sucedida mas sem dados para devolver
     *
     * @param id ID da unidade a apagar (extraído da URL)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUnit(@PathVariable UUID id) {

        // Chama o Service para apagar a unidade
        unitService.deleteUnit(id);

        // Devolve 204 No Content — apagado com sucesso
        // Void → sem corpo na resposta
        return ResponseEntity.noContent().build();
    }
}