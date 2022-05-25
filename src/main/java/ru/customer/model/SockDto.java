package ru.customer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SockDto {

    private int id;

    @Min(value = 1, message = "CottonPart must be not empty and more than 1")
    private int cottonPart;
    @Min(value = 1, message = "Quantity must be not empty and more than 1")
    private int quantity;
    @Valid
    private ColorDto color;
}
