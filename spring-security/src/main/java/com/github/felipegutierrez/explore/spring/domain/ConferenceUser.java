package com.github.felipegutierrez.explore.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder=true)
public class ConferenceUser {
    private String username;
    private String password;
    private List<String> submissions;
    private boolean speaker;
    private boolean admin;
}
