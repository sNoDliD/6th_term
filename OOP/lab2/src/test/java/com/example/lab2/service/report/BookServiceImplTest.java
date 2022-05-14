package com.example.lab2.service.report;

import com.example.lab2.model.Book;
import com.example.lab2.model.Car;
import com.example.lab2.repository.BookRepository;
import com.example.lab2.repository.CarRepository;
import com.example.lab2.repository.ReportRepository;
import com.example.lab2.service.book.BookService;
import com.example.lab2.service.book.BookServiceImpl;
import com.example.lab2.web.ReportController;
import com.example.lab2.web.converter.ReportConverter;
import com.example.lab2.web.model.ReportDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.actuate.metrics.AutoConfigureMetrics;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class BookServiceImplTest {


    CarRepository carRepository = mock(CarRepository.class);

    BookRepository bookRepository = mock(BookRepository.class);

    BookService bookService = new BookServiceImpl(carRepository, bookRepository);

    Book book = Book.builder().car(Car.builder().id(1).costPerDay(12).build()).clientId("1").allow(true).paid(false).build();

    @Test
    void bookCar() {
        when(carRepository.findById(book.getCar().getId())).thenReturn(Optional.ofNullable(Car.builder().id(1).costPerDay(12).build()));
        book.setCost(book.getDuration() * 12);

        bookService.bookCar(book);

        verify(bookRepository).saveAndFlush(book);
    }

    @Test
    void payCar() {
        when(carRepository.findAllByAvailableIsTrueAndId(book.getCar().getId())).thenReturn(Optional.ofNullable(Car.builder().id(1).costPerDay(12).build()));
        when(bookRepository.findAllByClientIdAndCarIdAndPaidIsFalseAndAllowIsTrue(book.getClientId(), book.getCar().getId())).thenReturn(Optional.ofNullable(book));

        bookService.payCar(book);

        verify(carRepository).saveAndFlush(Car.builder().id(1).costPerDay(12).available(false).build());

        book.setPaid(true);
        verify(bookRepository).saveAndFlush(book);
        book.setPaid(false);
    }

    @Test
    void rejectCar() {
        when(bookRepository.findAllByClientIdAndCarIdAndPaidIsFalseAndAllowIsTrue(book.getClientId(), book.getCar().getId())).thenReturn(Optional.ofNullable(book));

        bookService.reject(book);
        book.setAllow(false);
        book.setMessage(book.getMessage());

        verify(bookRepository).saveAndFlush(book);
    }

}