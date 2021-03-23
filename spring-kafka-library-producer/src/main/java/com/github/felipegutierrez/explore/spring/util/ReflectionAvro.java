package com.github.felipegutierrez.explore.spring.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.felipegutierrez.explore.spring.domain.Book;
import com.github.felipegutierrez.explore.spring.domain.LibraryEvent;
import com.github.felipegutierrez.explore.spring.domain.LibraryEventType;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.file.CodecFactory;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.reflect.ReflectData;
import org.apache.avro.reflect.ReflectDatumReader;
import org.apache.avro.reflect.ReflectDatumWriter;

import java.io.File;
import java.io.IOException;

@Slf4j
public class ReflectionAvro<T> {

    public static void main(String[] args) throws JsonProcessingException {
        ReflectionAvro reflectionAvroBook = new ReflectionAvro<Book>();
        reflectionAvroBook.createAvroSchema(Book.class, "book.avro");

        ReflectionAvro reflectionAvroLibraryEvent = new ReflectionAvro<LibraryEvent>();
        reflectionAvroLibraryEvent.createAvroSchema(LibraryEvent.class, "libraryEvent.avro");

        ObjectMapper objectMapper = new ObjectMapper();
        Book book = Book.builder()
                .bookId(1)
                .bookAuthor("Felipe")
                .bookName("3 anos na Alemanha")
                .build();
        LibraryEvent libraryEvent = LibraryEvent.builder()
                .libraryEventId(23)
                .book(book)
                .libraryEventType(LibraryEventType.NEW)
                .build();
        String value = objectMapper.writeValueAsString(libraryEvent);
        System.out.println(value);
    }

    public void createAvroSchema(Class clazz, String fileName) {
        Schema schema = ReflectData.get().getSchema(clazz);
        log.info("schema = " + schema.toString(true));

        String avroDir = "/tmp/avro/";

        // create a file of ReflectedCustomers
        try {
            log.info("Writing /tmp/avro/" + fileName);
            File directory = new File(avroDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            File file = new File(avroDir + fileName);
            DatumWriter<T> writer = new ReflectDatumWriter<>(clazz);
            DataFileWriter<T> out = new DataFileWriter<>(writer)
                    .setCodec(CodecFactory.deflateCodec(9))
                    .create(schema, file);

            // out.append(new Book("Bill", "Clark", "The Rocket"));
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // read from an avro into our Reflected class
        // open a file of ReflectedCustomers
        try {
            log.info("Reading /tmp/avro/" + fileName);
            File file = new File(avroDir + fileName);
            DatumReader<T> reader = new ReflectDatumReader<>(clazz);
            DataFileReader<T> in = new DataFileReader<>(file, reader);

            // read ReflectedCustomers from the file & print them as JSON
            /*
            for (T reflectedCustomer : in) {
                log.info("Reading book id {}, author: {}, name: {}",
                        reflectedCustomer.getBookId(),
                        reflectedCustomer.getBookAuthor(),
                        reflectedCustomer.getBookName());
            }
             */
            // close the input file
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
