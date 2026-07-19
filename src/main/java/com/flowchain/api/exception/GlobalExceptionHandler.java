package com.flowchain.api.exception;


// @RestControllerAdvice — anotação do Spring que marca esta classe como
// um "conselheiro" (advice) global de todos os @RestController.
// O Spring passa a enviar para aqui as exceções lançadas em QUALQUER Controller.
import org.springframework.web.bind.annotation.RestControllerAdvice;


// @ExceptionHandler — diz ao Spring QUAL exceção este método trata.
// Sempre que uma IllegalArgumentException subir de um Controller,
// o Spring chama ESTE método em vez de devolver 500.
import org.springframework.web.bind.annotation.ExceptionHandler;


// ResponseEntity — a resposta HTTP completa (status + corpo),
// igual ao que já usas no UnitController
import org.springframework.http.ResponseEntity;

// HttpStatus — enum com todos os códigos HTTP (BAD_REQUEST = 400)
import org.springframework.http.HttpStatus;

// Map — vamos usar para construir o JSON de erro
import java.util.Map;



/**
 * Classe central de tratamento de erros da API.
 *
 * Em vez de cada Controller ter try/catch repetido,
 * todas as exceções não tratadas vêm parar aqui,
 * e é aqui que decidimos que código HTTP e que mensagem devolver.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Trata a IllegalArgumentException lançada pelos Services
     * (ex: nome de unidade duplicado no UnitService.createUnit).
     *
     * Antes: exceção subia até ao Spring → 500 Internal Server Error
     * Agora: é apanhada aqui → 400 Bad Request com mensagem clara
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex){

        // ex.getMessage() devolve a mensagem que o Service escreveu:
        // "Já existe uma unidade com o nome: Alpha"
        Map<String, String> erro = Map.of("erro", ex.getMessage());

        // Devolve 400 Bad Request + JSON com a mensagem
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }
}
