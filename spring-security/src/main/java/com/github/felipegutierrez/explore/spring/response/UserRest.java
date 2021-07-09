package com.github.felipegutierrez.explore.spring.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRest {
    private String userId;
    private String userFirstname;
    private String userLastname;
}
