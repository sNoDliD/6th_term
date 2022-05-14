package com.example.lab2.web.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Builder;
import lombok.Value;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Builder(toBuilder = true)
@Value(staticConstructor = "of")
@JsonInclude(NON_NULL)
public class CarDto {
    @Null
    Integer id;
    @NotBlank
    @NotNull
    String name;
    @Null
    Boolean available;
    @NotNull
    Integer costPerDay;

    @JsonCreator
    public CarDto(@Null Integer id, @NotBlank @NotNull String name,
                  @Null Boolean available, @NotNull Integer costPerDay) {
        this.id = id;
        this.name = name;
        this.available = available;
        this.costPerDay = costPerDay;
    }
}
