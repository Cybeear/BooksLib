package com.bookslib.app.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Reader {
    private long id;

    @Pattern(regexp = "[^0-9]*", message = "Reader name must contain only letters")
    @Size(message = "Reader name must be longer then 5 and less then 20 characters", min = 5, max = 20)
    @NotBlank(message = "Reader name is missing")
    private String name;

    @Override
    public String toString() {
        return "reader - id: " + id + "\tname: " + name;
    }
}
