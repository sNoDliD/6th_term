package com.example.lab2.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @Column(name = "client_id", nullable = false)
    private String clientId;

    @ManyToOne
    @JoinColumn(name = "car_id", referencedColumnName = "id", nullable = false)
    private Car car;

    @Column(name = "rental_period_in_day", nullable = false)
    private int duration;

    @Column(name = "cost", nullable = false)
    private int cost;

    @Column(name = "allow", nullable = false)
    private boolean allow;

    @Column(name = "paid", nullable = false)
    private boolean paid;

    @Column(name = "cause")
    private String message;
}
