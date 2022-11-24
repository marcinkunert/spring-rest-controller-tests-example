package com.example.springboottesting.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
public class AddUserRequestDto {

    private Long id;

    @Email
    private String email;

    @Size(min = 3)
    private String firstName;
}
