package com.example.lab2.service.book;

import com.example.lab2.model.Book;
import com.example.lab2.repository.BookRepository;
import com.example.lab2.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final CarRepository carRepository;
    private final BookRepository bookRepository;

    @Override
    public List<Book> books(String clientId) {
        return isNull(clientId) ? bookRepository.findAll() : bookRepository.findAllByClientId(clientId);
    }

    @Override
    public void bookCar(Book book) {
        final var car = carRepository.findById(book.getCar().getId()).orElseThrow(() -> new EntityNotFoundException("Car not found"));
        book.setCost(book.getDuration() * car.getCostPerDay());
        book.setPaid(false);
        book.setAllow(true);

        bookRepository.saveAndFlush(book);
    }

    @Override
    public void payCar(Book book) {
        final var car = carRepository.findAllByAvailableIsTrueAndId(book.getCar().getId()).orElseThrow(() -> new EntityNotFoundException("Car not found or unavailable"));
        final var bookDb = bookRepository.findAllByClientIdAndCarIdAndPaidIsFalseAndAllowIsTrue(book.getClientId(), book.getCar().getId()).orElseThrow(() -> new EntityNotFoundException("Book not found or payed"));

        car.setAvailable(false);
        carRepository.saveAndFlush(car);

        bookDb.setPaid(true);
        bookRepository.saveAndFlush(bookDb);
    }

    @Override
    public void reject(Book book) {
        final var bookDb = bookRepository.findAllByClientIdAndCarIdAndPaidIsFalseAndAllowIsTrue(book.getClientId(), book.getCar().getId()).orElseThrow(() -> new EntityNotFoundException("Book not found or rejected re payed"));
        bookDb.setAllow(false);
        bookDb.setMessage(book.getMessage());

        bookRepository.saveAndFlush(bookDb);
    }
}
