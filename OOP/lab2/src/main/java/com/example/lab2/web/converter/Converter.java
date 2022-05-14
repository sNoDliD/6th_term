package com.example.lab2.web.converter;


public interface Converter<M, D> {

    D toDto(M model);

    M toModel(D dto);
}
