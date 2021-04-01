package com.github.felipegutierrez.explore.spring.services;

import com.github.felipegutierrez.explore.spring.bindings.EmployeeListenerBinding;
import com.github.felipegutierrez.explore.spring.model.Employee;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@EnableBinding(EmployeeListenerBinding.class)
public class EmployeeStreamListener {
    @Autowired
    RecordBuilder recordBuilder;

    @StreamListener("employee-input-channel")
    public void process(KStream<String, Employee> input) {
        input.peek((k, v) -> log.info("Key: {}, Value:{}", k, v));
    }
}
