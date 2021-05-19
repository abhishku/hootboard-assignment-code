package com.hootboard.assignment.repository;

import com.hootboard.assignment.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

  Boolean findByUsername(String username);

  Users findByUsernameAndPassword(String username, String password);
}
