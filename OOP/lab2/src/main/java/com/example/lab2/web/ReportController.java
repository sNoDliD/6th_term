package com.example.lab2.web;

import com.example.lab2.model.Report;
import com.example.lab2.service.car.CarService;
import com.example.lab2.service.report.ReportService;
import com.example.lab2.web.converter.CarConverter;
import com.example.lab2.web.converter.ReportConverter;
import com.example.lab2.web.model.CarDto;
import com.example.lab2.web.model.ReportDto;
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
@RequiredArgsConstructor
public class ReportController implements UserInformationSecurityContextHolder {
    private final ReportService service;
    private final ReportConverter converter;

    @GetMapping("/reports")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public List<ReportDto> reports(Principal principal) {
        String clientId = null;
        if (getRoles().contains("CLIENT")) {
            clientId = principal.getName();
        }
        return service.reports(clientId).stream().map(converter::toDto).toList();
    }

    @GetMapping("/report")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void report(ReportDto reportDto) {
        log.error("{}", reportDto);
        final var report = converter.toModel(reportDto);
        log.error("{}", report);
        service.report(report);
    }
}
