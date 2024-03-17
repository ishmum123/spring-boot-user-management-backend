package com.aidev.restozen.database.repositories;

import com.aidev.restozen.database.entities.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserTypeRepository extends JpaRepository<UserType, Long> {
    Optional<UserType> findByName(String name);

    Set<UserType> findByNameIn(Set<String> names);
}