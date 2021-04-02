package com.github.felipegutierrez.explore.spring.services;

import com.github.felipegutierrez.explore.spring.bindings.UserListenerBinding;
import com.github.felipegutierrez.explore.spring.model.UserDetails;
import com.github.felipegutierrez.explore.spring.model.UserLogin;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;

@Slf4j
@Service
@EnableBinding(UserListenerBinding.class)
public class LoginListenerService {

    @StreamListener
    public void process(@Input("user-master-channel") KTable<String, UserDetails> users,
                        @Input("user-login-channel") KTable<String, UserLogin> logins) {
        users.toStream().foreach((k, v) -> log.info("User Key: {}, Last Login: {}, Value{}",
                k, Instant.ofEpochMilli(v.getLastLogin()).atOffset(ZoneOffset.UTC), v));

        logins.toStream().foreach((k, v) -> log.info("Login Key: {}, Last Login: {}, Value{}",
                k, Instant.ofEpochMilli(v.getCreatedTime()).atOffset(ZoneOffset.UTC), v));

        logins
                .join(users, (left, right) -> {
                    right.setLastLogin(left.getCreatedTime());
                    return right;
                })
                .toStream()
                .foreach((k, v) -> log.info("Updated Last Login Key: {}, Last Login: {}", k, Instant.ofEpochMilli(v.getLastLogin()).atOffset(ZoneOffset.UTC)));
    }
}
