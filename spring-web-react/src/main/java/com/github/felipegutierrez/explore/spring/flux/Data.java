package com.github.felipegutierrez.explore.spring.flux;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class Data {
    private Integer key;
    private String value;
    private Integer partition;
}
