package com.tunehub.app.services;

import com.tunehub.app.entities.User;

public interface UserService {
	
	public String addUser(User user);
	
	public boolean emailExist(String email);

	public boolean validateUser(String email, String password);

	public String getRole(String email);

	public User getUser(String email);

	public String deleteAccount(Integer id);

	public String updateUser(User user);
}
