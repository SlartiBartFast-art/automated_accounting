package ru.customer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {

    @Size(min = 2, max = 10, message = "The length of full name must be between 2 and 100 characters.")
    @NotBlank(message = "coloring must not be empty")
    private String coloring;
    @NotBlank(message = "operator must not be empty")
    private String operator;
    @NotBlank(message = "cottonPart must not be empty")
        private String cottonPart;

}
