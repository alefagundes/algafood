package com.aledev.algafood.api.exceptionhandler;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.aledev.algafood.domain.exceptions.EntidadeEmUsoException;
import com.aledev.algafood.domain.exceptions.EntidadeNaoEncontradaException;
import com.aledev.algafood.domain.exceptions.NegocioException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@ControllerAdvice //focamos os pontos de tratamento de exceptions de todo o projto atraves desse controlador
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    
    
    @ExceptionHandler(EntidadeNaoEncontradaException.class) //defino um metodo capaz de substituir o stack trace de uma requisicao com erro, esse mapeamento ocorre para a exception que eh defina.
    public ResponseEntity<?> handleEntidadeNaoEncontradaException(EntidadeNaoEncontradaException e, WebRequest request){
        HttpStatus status = HttpStatus.NOT_FOUND;
        ProblemType problemType = ProblemType.ENTIDADE_NAO_ENCONTRADA;
        
        Problem problem = createProblemBuilder(status, problemType, e.getMessage()).build();
        
        /*   Problem problem = Problem.builder().status(status.value())
        .type("https://aledev.api/entidade-nao-encontrada")
        .title("Entidade nao encontrada").detail(detail).build(); */
        return handleExceptionInternal(e, problem, new HttpHeaders(), status, request);
    }

    @Override
    @Nullable
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Throwable rootCause = ExceptionUtils.getRootCause((ex));

        if(rootCause instanceof InvalidFormatException){
            return handldeInvalidFormatException((InvalidFormatException) rootCause, headers, status, request);
        }
        HttpStatus statusBad = HttpStatus.BAD_REQUEST;
        ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
        String detail = "O corpo da requisição está inválido. Veridique o erro de sintaxe.";

        Problem problem = createProblemBuilder(statusBad, problemType, detail).build();

        return handleExceptionInternal(ex, problem, headers, statusBad, request);
    }
    
    private ResponseEntity<Object> handldeInvalidFormatException(InvalidFormatException ex, HttpHeaders headers, 
            HttpStatusCode status, WebRequest request) {

        String path = ex.getPath().stream().map((item) -> item.getFieldName()).collect(Collectors.joining("."));
                
        HttpStatus statusDefined = HttpStatus.BAD_REQUEST;
        ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
        String detail = String.format("A propriedade '%s' recebeu um valor %s que é de um tipo inválido. "
          + "Corrija e informe um valor que é compativel com o tipo %s.", path, ex.getValue(), ex.getTargetType().getSimpleName()); 
        
        Problem problem = createProblemBuilder(statusDefined, problemType, detail).build();
        return handleExceptionInternal(ex, problem, headers, statusDefined, request);
    }

    @ExceptionHandler(EntidadeEmUsoException.class) 
    public ResponseEntity<?> handleEntidadeEmUsoException(EntidadeEmUsoException e, WebRequest request){
        HttpStatus status = HttpStatus.CONFLICT;
        ProblemType problemType = ProblemType.ENTIDADE_EM_USO;
        
        Problem problem = createProblemBuilder(status, problemType, e.getMessage()).build();
        return handleExceptionInternal(e, problem, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
    
    @ExceptionHandler(NegocioException.class) 
    public ResponseEntity<?> handleNegocioException(NegocioException e, WebRequest request){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ProblemType problemType = ProblemType.ERRO_NEGOCIO;
        
        Problem problem = createProblemBuilder(status, problemType, e.getMessage()).build();
        return handleExceptionInternal(e, problem, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
    
    @Override
    @Nullable
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers,
    HttpStatusCode statusCode, WebRequest request) {
        if(body == null){
            body = Problem.builder().title(ex.getMessage()).status(statusCode.value()).build();
        }else if (body instanceof String) {
            body = Problem.builder().title((String) body).status(statusCode.value()).build();
        }
        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }
    
    private Problem.ProblemBuilder createProblemBuilder(HttpStatus status, ProblemType problemType, String detail) {
        return Problem.builder()
        .status(status.value())
        .type(problemType.getUri())
        .title(problemType.getTitle())
        .detail(detail);
    }
}
