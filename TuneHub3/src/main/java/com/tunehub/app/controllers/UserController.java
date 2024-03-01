package com.tunehub.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tunehub.app.entities.Favourite;
import com.tunehub.app.entities.PlayList;
import com.tunehub.app.entities.Song;
import com.tunehub.app.entities.User;
import com.tunehub.app.services.FavouriteService;
import com.tunehub.app.services.PlayListService;
import com.tunehub.app.services.SongService;
import com.tunehub.app.services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
	@Autowired
	UserService user_service;
	@Autowired
	FavouriteService fav_service;
	@Autowired
	SongService song_service;
	@Autowired
	PlayListService playlist_service;
	
	@PostMapping("/register")
	public String userRegistration(@ModelAttribute User user, Model model) {
		
		if(user_service.emailExist(user.getEmail())==false) {
			user_service.addUser(user);
			Favourite fav=new Favourite(0,user,null);
			fav_service.updateFav(fav);
			user.setUserFavourite(fav);
			//fav_service.createFav(user);
			user_service.addUser(user);
			model.addAttribute("msg", "You have registered successfully");
			return "loginForm";
		}
		else {
			model.addAttribute("errorMsg", "Email id already exist.Try Again with different email id.");
			return "registerForm";
//			return "registrationFail";
		}
		
	}
	
	@PostMapping("/login")
	public String userLogin(@RequestParam String email,@RequestParam String password, HttpSession session, Model model) {
		if(user_service.emailExist(email)==true) {
			if(user_service.validateUser(email,password)==true) {
				session.setAttribute("email",email);
				if(user_service.getRole(email).equals("creator")) {
					return "creatorHome";
				}
				else {
					return "userHome";
				}
			}
			else {
				model.addAttribute("errorMsg", "Invalid email id or password");
				return "loginForm";
			}
		}
		else {
			model.addAttribute("errorMsg", "Invalid email id or password");
			return "loginForm";
		}
		
	}
	
	@GetMapping("/map-account")
	public String mapAccount(Model model, HttpSession session) {
		Object email=session.getAttribute("email");
		if(email==null) {
			return "loginForm";
		}
		else {
			User user=user_service.getUser((String)email);
			model.addAttribute("user",user);
			if(user.getRole().equals("creator")) {
				return "accountCreator";
			}
			else {
				return "accountUser";
			}
		}
		
	}
	
	@GetMapping("/deleteAccount")
	public String deleteAccount(HttpSession session) {
		Object email=session.getAttribute("email");
		if(email==null) {
			return "loginForm";
		}
		else {
			User user= user_service.getUser((String)email);
			//Removing songs form user,all playlists and favourites. after that deleting song
			for(Song song : user.getSongs()) {
				if(song!=null) { //Checking song exist or not to avoid null error during deletion
					for(PlayList playlist : song.getPlaylist()) {
						playlist.getSongs().remove(song);
						playlist_service.updatePlaylist(playlist);
					}
					//for(Favourite fav : fav_service.getAllFavBySong(song)) {
					for(Favourite fav : song.getFavourites()) {
						fav.getFav_songs().remove(song);
						fav_service.updateFav(fav);
					}
//					user.getSongs().remove(song);
//					user_service.updateUser(user);
					song_service.deleteSongs(song.getId());
				}
			}
			for(PlayList playlist : user.getPlaylists()) {
				playlist_service.deletePlaylist(playlist.getId());
				
			}
			
			user_service.deleteAccount(user.getId());
			session.removeAttribute("email");
			return "accountDeleted";
		}
		
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("email");
		return "loginForm";
	}
}
