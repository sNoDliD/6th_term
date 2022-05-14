package com.example.lab2.web.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Builder;
import lombok.Value;

import javax.validation.groups.Default;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Builder(toBuilder = true)
@Value(staticConstructor = "of")
@JsonInclude(NON_NULL)
public class BookDto {
    @NotNull(groups = RejectGroup.class)
    String clientId;
    @NotNull(groups = RejectGroup.class)
    Integer carId;
    @NotNull
    Integer duration;
    @Null
    Integer cost;
    @Null
    Boolean allow;
    @Null
    Boolean paid;
    @NotNull(groups = RejectGroup.class)
    String message;

    @JsonCreator
    public BookDto(@NotNull(groups = RejectGroup.class) String clientId, @NotNull(groups = RejectGroup.class) Integer carId,
                   @NotNull Integer duration, @Null Integer cost,
                   @Null Boolean allow, @Null Boolean paid, @NotNull(groups = RejectGroup.class) String message) {
        this.clientId = clientId;
        this.carId = carId;
        this.duration = duration;
        this.cost = cost;
        this.allow = allow;
        this.paid = paid;
        this.message = message;
    }

    public interface RejectGroup extends Default {

    }
}