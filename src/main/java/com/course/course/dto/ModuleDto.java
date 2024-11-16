package com.course.course.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ModuleDto {

    @NotBlank
    private String title;
    @NotBlank
    private String description;
}
