package com.example.lab2.repository;

import com.example.lab2.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    List<Book> findAllByClientId(String clientId);

    Optional<Book> findAllByClientIdAndCarIdAndAllowIsTrueAndPaidIsTrue(String clientId, Integer carId);

    Optional<Book> findAllByClientIdAndCarIdAndPaidIsFalseAndAllowIsTrue(String clientId, Integer carId);
}
