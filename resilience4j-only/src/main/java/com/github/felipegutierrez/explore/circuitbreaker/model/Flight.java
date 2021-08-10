package com.github.felipegutierrez.explore.circuitbreaker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flight {
    String flightNumber;
    String flightDate;
    String from;
    String to;
}
