package com.aledev.algafood.api.exceptionhandler;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class Problema {
    private LocalDateTime dataHome;
    private String message;
}
