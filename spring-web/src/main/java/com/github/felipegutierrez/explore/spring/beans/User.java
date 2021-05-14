package com.github.felipegutierrez.explore.spring.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {

    @Id
    private int id;
    private String username;
    private String password;
    private String gender;
    private String activity;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
}
