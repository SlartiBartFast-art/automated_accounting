package ru.job4j.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColorDto {

    private Long id;
    @NotBlank(message = "Coloring must be not empty")
    private String coloring;
}
