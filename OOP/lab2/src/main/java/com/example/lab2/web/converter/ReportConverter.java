package com.example.lab2.web.converter;


import com.example.lab2.model.Book;
import com.example.lab2.model.Car;
import com.example.lab2.model.Report;
import com.example.lab2.web.model.BookDto;
import com.example.lab2.web.model.ReportDto;
import org.springframework.stereotype.Component;

@Component
public class ReportConverter implements Converter<Report, ReportDto> {

    @Override
    public ReportDto toDto(Report model) {
        return new ReportDto(model.getClientId(), model.getCar().getId(), model.isHasInjuries(), model.getMessage(), model.getCost());
    }

    @Override
    public Report toModel(ReportDto dto) {
        return Report.builder().clientId(dto.getClientId()).car(Car.builder().id(dto.getCarId()).build()).hasInjuries(dto.getHasInjuries()).message(dto.getMessage()).cost(dto.getCost()).build();
    }
}
