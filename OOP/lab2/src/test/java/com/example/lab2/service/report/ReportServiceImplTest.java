package com.example.lab2.service.report;

import com.example.lab2.model.Book;
import com.example.lab2.model.Car;
import com.example.lab2.repository.BookRepository;
import com.example.lab2.repository.CarRepository;
import com.example.lab2.repository.ReportRepository;
import com.example.lab2.web.ReportController;
import com.example.lab2.web.converter.ReportConverter;
import com.example.lab2.web.model.ReportDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.actuate.metrics.AutoConfigureMetrics;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReportServiceImplTest {

    ReportRepository reportRepository = mock(ReportRepository.class);

    CarRepository carRepository = mock(CarRepository.class);

    BookRepository bookRepository = mock(BookRepository.class);

    ReportController reportController = new ReportController(new ReportServiceImpl(reportRepository,
            bookRepository, carRepository), new ReportConverter());

    Book book = Book.builder().car(Car.builder().id(1).build()).clientId("1").allow(true).paid(true).build();

    @Test
    void report() {
        when(bookRepository.findAllByClientIdAndCarIdAndAllowIsTrueAndPaidIsTrue("1", 1)).thenReturn(Optional.ofNullable(book));

        reportController.report(new ReportDto("1", 1, false, "ok", null));
        verify(carRepository).saveAndFlush(Car.builder().id(1).available(true).build());
        verify(bookRepository).delete(book);
    }
}