package com.example.lab2.service.book;

import com.example.lab2.model.Book;

import java.util.List;

public interface BookService {
    List<Book> books(String clientId);

    void bookCar(Book book);

    void payCar(Book book);

    void reject(Book book);

}
