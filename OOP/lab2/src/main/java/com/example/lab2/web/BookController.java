package com.example.lab2.web;

import com.example.lab2.model.Book;
import com.example.lab2.model.Car;
import com.example.lab2.service.book.BookService;
import com.example.lab2.web.converter.BookConverter;
import com.example.lab2.web.model.BookDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController implements UserInformationSecurityContextHolder {
    private final BookService service;
    private final BookConverter converter;

    @GetMapping
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public List<BookDto> books(Principal principal) {
        String clientId = null;
        if (getRoles().contains("CLIENT")) {
            clientId = principal.getName();
        }
        return service.books(clientId).stream().map(converter::toDto).toList();
    }

    @GetMapping("/car")
    @PreAuthorize("hasRole('CLIENT')")
    public void book(BookDto bookDto, Principal principal) {
        final var book = converter.toModel(bookDto);
        book.setClientId(principal.getName());
        service.bookCar(book);
    }

    @GetMapping("/car/pay")
    @PreAuthorize("hasRole('CLIENT')")
    public void pay(BookDto bookDto, Principal principal) {
        final var book = Book.builder().car(Car.builder().id(bookDto.getCarId()).build()).clientId(principal.getName()).build();
        service.payCar(book);
    }

    @GetMapping("/car/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public void reject(BookDto bookDto, Principal principal) {
        final var book = Book.builder().car(Car.builder().id(bookDto.getCarId()).build()).clientId(bookDto.getClientId()).message(bookDto.getMessage()).build();
        service.reject(book);
    }

}
