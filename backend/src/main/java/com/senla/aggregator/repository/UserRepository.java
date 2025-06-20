package com.senla.aggregator.repository;

import com.senla.aggregator.model.Role;
import com.senla.aggregator.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    List<User> findAllByRole(Role role);

    Optional<User> findByUsername(String username);
}
