package com.aledev.algafood.core.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE}) //pode ser usado em classe interface e enum
@Retention(RUNTIME)
@Constraint(validatedBy = { ValorZeriIncluiDescricaoValidator.class})
public @interface ValorZeroIncluiDescricao {

    String message() default "descricao obrigatoria invalida";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default {};
    String valorField();
    String descricaoField();
    String descricaoObrigatoria();
}
