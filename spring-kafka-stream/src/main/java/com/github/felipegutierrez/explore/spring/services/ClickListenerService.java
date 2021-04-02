package com.github.felipegutierrez.explore.spring.services;

import com.github.felipegutierrez.explore.spring.bindings.ClicksListenerBinding;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@EnableBinding(ClicksListenerBinding.class)
public class ClickListenerService {

}
