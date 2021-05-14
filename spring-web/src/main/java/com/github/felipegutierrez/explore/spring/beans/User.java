package com.github.felipegutierrez.explore.spring.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {

    @Id
    private int id;

    @Size(min=6, message = "{username.cannot.be.less.than.six.characters}")
    private String username;

    @Pattern(regexp = "((?=.*[A-Z]).{6,10})", message = "Password must have one upper case, one lower case and should be between 6 and 10 characters")
    private String password;
    private String gender;

    @NotNull(message = "Activity cannot be left empty")
    private String activity;

    @NotEmpty(message = "First name cannot be empty")
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
}
