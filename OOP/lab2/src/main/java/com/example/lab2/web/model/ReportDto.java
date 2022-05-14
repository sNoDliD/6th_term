package com.example.lab2.web.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Builder(toBuilder = true)
@Value(staticConstructor = "of")
@JsonInclude(NON_NULL)
public class ReportDto {

    @NotBlank
    String clientId;
    @NotNull
    Integer carId;
    @NotNull
    Boolean hasInjuries;
    String message;
    Integer cost;

    @JsonCreator
    public ReportDto(String clientId, Integer carId, Boolean hasInjuries, String message, Integer cost) {
        this.clientId = clientId;
        this.carId = carId;
        this.hasInjuries = hasInjuries;
        this.message = message == null ? "" : message;
        this.cost = cost == null ? 0 : cost;
    }
}
