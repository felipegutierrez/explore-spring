package com.github.felipegutierrez.explore.spring.bindings;

import com.github.felipegutierrez.explore.spring.model.Employee;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;

public interface EmployeeListenerBinding {

    @Input("employee-input-channel")
    KStream<String, Employee> employeeInputStream();

}
