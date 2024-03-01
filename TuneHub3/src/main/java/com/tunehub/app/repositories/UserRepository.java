package com.tunehub.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tunehub.app.entities.User;


public interface UserRepository extends JpaRepository<User, Integer>{
	
	public User getUserByEmail(String email);
}
