package com.aledev.algafood.api.exceptionhandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.aledev.algafood.domain.exceptions.EntidadeEmUsoException;
import com.aledev.algafood.domain.exceptions.EntidadeNaoEncontradaException;
import com.aledev.algafood.domain.exceptions.NegocioException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;


@ControllerAdvice //focamos os pontos de tratamento de exceptions de todo o projto atraves desse controlador
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String MSG_USER_ERROR = "Ocorreu um erro interno e insperado no sitema. Tente novamente e se o " +
    "problema persistir, entre em contato com o administrador do sistema.";

    @Autowired
    private MessageSource messageSource;
    
    @ExceptionHandler(EntidadeNaoEncontradaException.class) //defino um metodo capaz de substituir o stack trace de uma requisicao com erro, esse mapeamento ocorre para a exception que eh defina.
    public ResponseEntity<?> handleEntidadeNaoEncontradaException(EntidadeNaoEncontradaException e, WebRequest request){
        HttpStatus status = HttpStatus.NOT_FOUND;
        ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
        
        Problem problem = createProblemBuilder(status, problemType, e.getMessage()).userMessage(MSG_USER_ERROR).build();
        return handleExceptionInternal(e, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(EntidadeEmUsoException.class) 
    public ResponseEntity<?> handleEntidadeEmUsoException(EntidadeEmUsoException e, WebRequest request){
        HttpStatus status = HttpStatus.CONFLICT;
        ProblemType problemType = ProblemType.ENTIDADE_EM_USO;
        String detail = e.getMessage();
        
        Problem problem = createProblemBuilder(status, problemType, detail).userMessage(detail).build();
        return handleExceptionInternal(e, problem, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
    
    @ExceptionHandler(NegocioException.class) 
    public ResponseEntity<?> handleNegocioException(NegocioException e, WebRequest request){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ProblemType problemType = ProblemType.ERRO_NEGOCIO;
        
        Problem problem = createProblemBuilder(status, problemType, e.getMessage()).userMessage(MSG_USER_ERROR).build();
        return handleExceptionInternal(e, problem, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUncaught(Exception ex,  WebRequest request){
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ProblemType problemType = ProblemType.ERRO_DE_SISTEMA;
        String detail = MSG_USER_ERROR;

        Problem problem = createProblemBuilder(status, problemType, detail).userMessage(MSG_USER_ERROR).build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @Override
    @Nullable
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
        String detail = String.format("O recurso '%s', que tentou acessar, e inexistente.", ex.getRequestURL());
        Problem problem = createProblemBuilder(status, problemType, detail).userMessage(MSG_USER_ERROR).build();
       
        return handleExceptionInternal(ex, problem, headers, status, request);
    }
    @Override
    @Nullable
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
    HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemType problemType = ProblemType.DADOS_INVALIDOS;
        String detail = "Um ou mais campos estao incorretos. Faça o preenchimento correto dos campos e tente novamente.";
        BindingResult bindingResult = ex.getBindingResult();
        List<Problem.Field> problemFiel = bindingResult.getFieldErrors().stream()
                            .map((fieldError) -> {
                                String message = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
                                return Problem.Field.builder().name(fieldError.getField()).userMessage(message)
                                        .build();
                            }).collect(Collectors.toList());

        Problem  problem = createProblemBuilder(status, problemType, detail).userMessage(detail).fields(problemFiel).build();
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    @Nullable
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Throwable rootCause = ExceptionUtils.getRootCause((ex));

        if(rootCause instanceof InvalidFormatException){
            return handldeInvalidFormatException((InvalidFormatException) rootCause, headers, status, request);
        }else if(rootCause instanceof PropertyBindingException){
            return handlePropertyBindingExept((PropertyBindingException) rootCause, headers, status, request);
        }
        ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
        String detail = "O corpo da requisição está inválido. Veridique o erro de sintaxe.";
        Problem problem = createProblemBuilder(status, problemType, detail).userMessage(MSG_USER_ERROR).build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    @Nullable
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        if (ex instanceof MethodArgumentTypeMismatchException) {
            return handleMethodArgumentTypeMismatch(
                    (MethodArgumentTypeMismatchException) ex, headers, status, request);
        }
        return super.handleTypeMismatch(ex, headers, status, request);
    }

    private ResponseEntity<Object> handleMethodArgumentTypeMismatch(
        MethodArgumentTypeMismatchException ex, HttpHeaders headers,
        HttpStatusCode status, WebRequest request) {
        ProblemType problemType = ProblemType.PARAMETRO_INVALIDO;
        
        String detail = String.format("O parâmetro de URL '%s' recebeu o valor '%s', "
                + "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
                ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

        Problem problem = createProblemBuilder(status, problemType, detail).userMessage(MSG_USER_ERROR).build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private ResponseEntity<Object> handlePropertyBindingExept(PropertyBindingException rootCause, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        String path = rootCause.getPath().stream().map((key) -> key.getFieldName()).collect(Collectors.joining("."));
        ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;

        String detail = String.format("A propriedade '%s' nao existe corrija ou remova essa propriedade.", path);
        Problem problem = createProblemBuilder(status, problemType, detail).userMessage(MSG_USER_ERROR).build();
        return handleExceptionInternal(rootCause, problem, headers, status, request);
    }

    private ResponseEntity<Object> handldeInvalidFormatException(InvalidFormatException ex, HttpHeaders headers, 
            HttpStatusCode status, WebRequest request) {

        String path = ex.getPath().stream().map((key) -> key.getFieldName()).collect(Collectors.joining("."));
                
        ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
        String detail = String.format("A propriedade '%s' recebeu um valor %s que é de um tipo inválido. "
          + "Corrija e informe um valor que é compativel com o tipo %s.", path, ex.getValue(), ex.getTargetType().getSimpleName()); 
        
        Problem problem = createProblemBuilder(status, problemType, detail).userMessage(MSG_USER_ERROR).build();
        return handleExceptionInternal(ex, problem, headers, status, request);
    }
    
    @Override
    @Nullable
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers,
    HttpStatusCode statusCode, WebRequest request) {
        if(body == null){
            body = Problem.builder().title(ex.getMessage()).status(statusCode.value()).userMessage(MSG_USER_ERROR).build();
        }else if (body instanceof String) {
            body = Problem.builder().title((String) body).status(statusCode.value()).userMessage(MSG_USER_ERROR).build();
        }
        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }
    
    private Problem.ProblemBuilder createProblemBuilder(HttpStatusCode status, ProblemType problemType, String detail) {
        return Problem.builder()
        .timestamp(LocalDateTime.now())
        .status(status.value())
        .type(problemType.getUri())
        .title(problemType.getTitle())
        .detail(detail);
    }
}
