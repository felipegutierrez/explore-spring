package com.github.felipegutierrez.explore.spring.repository;

import com.github.felipegutierrez.explore.spring.beans.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
}
