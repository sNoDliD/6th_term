package com.example.lab2.service.report;

import com.example.lab2.model.Report;

import java.util.List;

public interface ReportService {

    List<Report> reports(String clientId);

    void report(Report report);
}
