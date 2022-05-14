package com.example.lab2.web.converter;


import com.example.lab2.model.Book;
import com.example.lab2.model.Car;
import com.example.lab2.web.model.BookDto;
import com.example.lab2.web.model.CarDto;
import org.springframework.stereotype.Component;

@Component
public class BookConverter implements Converter<Book, BookDto> {

    @Override
    public BookDto toDto(Book model) {
        return new BookDto(model.getClientId(), model.getCar().getId(), model.getDuration(), model.getCost(), model.isAllow(), model.isPaid(), model.getMessage());
    }

    @Override
    public Book toModel(BookDto dto) {
        return Book.builder().car(Car.builder().id(dto.getCarId()).build()).duration(dto.getDuration()).build();
    }
}
