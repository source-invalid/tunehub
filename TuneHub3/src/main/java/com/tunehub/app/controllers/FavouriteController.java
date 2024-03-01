package com.tunehub.app.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tunehub.app.entities.Favourite;
import com.tunehub.app.entities.Song;
import com.tunehub.app.entities.User;
import com.tunehub.app.services.FavouriteService;
import com.tunehub.app.services.SongService;
import com.tunehub.app.services.UserService;

import jakarta.servlet.http.HttpSession;
@Controller
public class FavouriteController {
	@Autowired
	FavouriteService fav_service;
	@Autowired
	UserService user_service;
	@Autowired
	SongService song_service;
	
	@GetMapping("/exploreFavourites")
	public String exploreFavourites(HttpSession session, Model model) {
		Object email=session.getAttribute("email");
		if(email==null) {
			return "loginForm";
		}
		else {
			User user=user_service.getUser((String)email);
			if(user.getRole().equals("user")) {
				if(user.isPremium()==true) {
					List<Song> favSongs=fav_service.fetchAllFavSongs(user);
					if(favSongs==null || favSongs.isEmpty()) {
						model.addAttribute("status", "EmptyList");
						return "exploreFavSongs";
					}
					else {
						model.addAttribute("songslist", favSongs);
						return "exploreFavSongs";
					}
					
				}
				else {
					return "buyPremium";
				}
				
			}
			else{
				session.removeAttribute("email");
				return "loginForm";
			}
		}
	}
	
	@GetMapping("/addToFavouriteForm")
	public String addToFavouriteForm(HttpSession session, Model model) {
		Object email=session.getAttribute("email");
		if(email==null) {
			return "loginForm";
		}
		else {
			User user=user_service.getUser((String)email);
			if(user.getRole().equals("user")) {
				if(user.isPremium()==true) {
					List<Song> songs=user.getUserFavourite().getFav_songs();
					List<Song> songlist=new ArrayList<Song>();
					List<Song> allSongs=song_service.fetchAllSongs();
					if(allSongs==null || allSongs.isEmpty()) {
						model.addAttribute("status", "EmptyList");
						return "addToFavouriteForm";
					}
					else {
						for(Song song : allSongs) {
							if(songs!=null && (songs.contains(song)==false)) {
								songlist.add(song);
							}
						}
						model.addAttribute("songslist", songlist);
						return "addToFavouriteForm";
					}
					
				}
				else {
					return "buyPremium";
				}
				
			}
			else{
				session.removeAttribute("email");
				return "loginForm";
			}
		}
	}
	
	@PostMapping("/addToFavourite")
	public String addToFavourite(HttpSession session,@RequestParam List<Integer> song_id, Model model) {
		try {
			Object email=session.getAttribute("email");
			if(email==null) {
				return "loginForm";
			}
			else {
				User user=user_service.getUser((String)email);
				if(user.getRole().equals("user")) {
					Favourite fav=user.getUserFavourite();
					//fav.setUser(user);
					for(Integer songID : song_id) {
						if(song_service.songExistById(songID)) { //Checking song exist or not to avoid null error during deletion
							Song song=song_service.getSong(songID);
							fav.getFav_songs().add(song);
							fav_service.updateFav(fav);
							song.getFavourites().add(fav);
							song_service.updateSong(song);
						}
					}
					model.addAttribute("status", "addFavSuccess");
					return "addToFavouriteForm";
				}
				else{
					session.removeAttribute("email");
					return "loginForm";
				}
			}
		} catch (Exception e) {
			return "errorPage";
		}
		
	}
	
	@GetMapping("/removeFromFavouriteForm")
	public String removeFromFavouriteForm(HttpSession session, Model model) {
		Object email=session.getAttribute("email");
		if(email==null) {
			return "loginForm";
		}
		else {
			User user=user_service.getUser((String)email);
			if(user.getRole().equals("user")) {
				if(user.isPremium()==true) {
					model.addAttribute("songslist", fav_service.fetchAllFavSongs(user));
					return "removeFromFavouriteForm";
				}
				else {
					return "buyPremium";
				}
				
			}
			else{
				session.removeAttribute("email");
				return "loginForm";
			}
		}
	}
	
	@PostMapping("/removeFromFavourite")
	public String removeFromFavourite(HttpSession session,@RequestParam List<Integer> song_id, Model model) {
		try {
			Object email=session.getAttribute("email");
			if(email==null) {
				return "loginForm";
			}
			else {
				User user=user_service.getUser((String)email);
				if(user.getRole().equals("user")) {
					//Favourite fav=fav_service.getFavByUser(user);
					Favourite fav=user.getUserFavourite();
					for(Integer songID : song_id) {
						if(song_service.songExistById(songID)) { //Checking song exist or not to avoid null error during deletion
							Song song=song_service.getSong(songID);
							fav.getFav_songs().remove(song);
							fav_service.updateFav(fav);
							song.getFavourites().remove(fav);
							song_service.updateSong(song);
						}
					}
					model.addAttribute("status", "removeFavSuccess");
					return "removeFromFavouriteForm";
				}
				else{
					session.removeAttribute("email");
					return "loginForm";
				}
			}
		} catch (Exception e) {
			return "errorPage";
		}
	}
}
