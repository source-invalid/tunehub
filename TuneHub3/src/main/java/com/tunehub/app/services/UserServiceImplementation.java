package com.tunehub.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tunehub.app.entities.User;
import com.tunehub.app.repositories.UserRepository;
@Service
public class UserServiceImplementation implements UserService{
	@Autowired
	UserRepository user_repo;

	@Override
	public String addUser(User user) {
		user_repo.save(user);
		return "User added";
	}

	@Override
	public boolean emailExist(String email) {
		if(user_repo.getUserByEmail(email)==null) {
			return false;
		}
		else {
			return true;
		}
		
	}

	@Override
	public boolean validateUser(String email, String password) {
		if(user_repo.getUserByEmail(email).getPassword().equals(password)) {
			return true;
		}
		else {
			return false;
		}
		
	}

	@Override
	public String getRole(String email) {
		return user_repo.getUserByEmail(email).getRole();
	}

	@Override
	public User getUser(String email) {
		return user_repo.getUserByEmail(email);
	}

	@Override
	public String deleteAccount(Integer id) {
		user_repo.deleteById(id);
		return "Account Deleted";
	}

	@Override
	public String updateUser(User user) {
		user_repo.save(user);
		return "User updated";
	}
	
}
