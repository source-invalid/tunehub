package com.tunehub.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.tunehub.app.services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class NavController {
	@Autowired
	UserService user_service;
	
	@GetMapping("/map-register")
	public String mapRegister(HttpSession session) {
		session.removeAttribute("email");
		return "registerForm";
	}
	
	@GetMapping("/map-login")
	public String mapLogin(HttpSession session) {
		session.removeAttribute("email");
		return "loginForm";
	}
	
	@GetMapping("/map-about")
	public String mapAbout(HttpSession session, Model model) {
		Object email=session.getAttribute("email");
		if(email==null) {
			return "about";
		}
		else {
			model.addAttribute("role", user_service.getRole((String)email));
			return "about";
		}
		
	}
	
	@GetMapping("/map-home")
	public String mapHome(HttpSession session) {
		Object email=session.getAttribute("email");
		if(email==null) {
			return "index";
		}
		else {
			if(user_service.getRole((String)email).equals("creator")) {
				return "creatorHome";
			}
			else{
				return "userHome";
			}
		}		
		
	}
	
	
	
	
}
