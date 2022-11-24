package com.example.springboottesting.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
public class UserDto {

    private Long id;

    @Email
    private String email;

    @Size(min = 3)
    private String firstName;

    @JsonIgnore
    private String password;

}
