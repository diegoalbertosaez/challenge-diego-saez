package com.mahva.diego.saez.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mahva.diego.saez.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

}
