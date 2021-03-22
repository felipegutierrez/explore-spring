package com.github.felipegutierrez.explore.spring.jpa;

import com.github.felipegutierrez.explore.spring.entity.LibraryEvent;
import org.springframework.data.repository.CrudRepository;

public interface LibraryEventRepository extends CrudRepository<LibraryEvent, Integer> {
}
