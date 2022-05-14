package com.example.lab2.service.report;

import com.example.lab2.model.Book;
import com.example.lab2.model.Car;
import com.example.lab2.model.Report;
import com.example.lab2.repository.BookRepository;
import com.example.lab2.repository.CarRepository;
import com.example.lab2.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final BookRepository bookRepository;
    private final CarRepository carRepository;

    @Override
    public List<Report> reports(String clientId) {
        return isNull(clientId) ? reportRepository.findAll() : reportRepository.findAllByClientId(clientId);
    }

    @Override
    public void report(Report report) {
        final var book = bookRepository.findAllByClientIdAndCarIdAndAllowIsTrueAndPaidIsTrue(report.getClientId(), report.getCar().getId()).orElseThrow(() -> new EntityNotFoundException("Book not found or already reported"));
        final var car = book.getCar();
        bookRepository.delete(book);
        car.setAvailable(true);
        carRepository.saveAndFlush(car);
        reportRepository.saveAndFlush(report);
    }
}
