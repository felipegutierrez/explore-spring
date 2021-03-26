package com.github.felipegutierrez.explore.spring.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.felipegutierrez.explore.spring.model.DeliveryAddress;
import com.github.felipegutierrez.explore.spring.model.DeliveryAddressAvro;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URL;
import java.util.Random;

@Service
class AddressGenerator {

    private final Random random;
    private final DeliveryAddress[] addresses;
    private final DeliveryAddressAvro[] addressesAvro;

    public AddressGenerator() {
        URL resource = getClass().getClassLoader().getResource("data/address.json");
        final ObjectMapper mapper;
        random = new Random();
        mapper = new ObjectMapper();
        try {
            addresses = mapper.readValue(new File(resource.toURI()), DeliveryAddress[].class);
            addressesAvro = mapper.readValue(new File(resource.toURI()), DeliveryAddressAvro[].class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private int getIndex() {
        return random.nextInt(100);
    }

    public DeliveryAddress getNextAddress() {
        return addresses[getIndex()];
    }

    public DeliveryAddressAvro getNextAddressAvro() {
        return addressesAvro[getIndex()];
    }
}
