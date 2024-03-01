package com.tunehub.app.controllers;

import java.util.List;

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
public class SongController {
	@Autowired
	SongService song_service;
	@Autowired
	UserService user_service;
	@Autowired
	PlayListService playlist_service;
	@Autowired
	FavouriteService fav_service;
	
	@GetMapping("/map-addSong")
	public String mapAddSong(HttpSession session) {
		Object email=session.getAttribute("email");
		if(email==null) {
			return "loginForm";
		}
		else {
			if(user_service.getRole((String)email).equals("creator")) {
				return "addSongForm";
			}
			else{
				session.removeAttribute("email");
				return "loginForm";
			}
		}
		
	}
	
	@PostMapping("/addSong")
	public String addSong(@ModelAttribute Song song, HttpSession session, Model model) {
		try {
			Object email=session.getAttribute("email");
			if(email==null) {
				return "loginForm";
			}
			else {
				if(user_service.getRole((String)email).equals("creator")) {
					if(song_service.songExist(song.getName())==false) {
						User user=user_service.getUser((String)email);
						song.setCreator(user);
						song_service.addSong(song);
						user.getSongs().add(song);
						user_service.updateUser(user);
						model.addAttribute("msg", "Song added successfully.");
						return "addSongForm";
					}
					else {
						model.addAttribute("errorMsg", "Failed, Song already exist.");
						return "addSongForm";
					}
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
	
	@GetMapping("/manageSongs")
	public String manageSongs(HttpSession session, Model model ) {
		Object email=session.getAttribute("email");
		if(email==null) {
			return "loginForm";
		}
		else {
			User user=user_service.getUser((String)email);
			if(user.getRole().equals("creator")) {
				//fetching all songs added by the creator and adding it to the thymeleaf model
				List<Song> songslist =song_service.getCreatorSongs(user);
				if(songslist==null || songslist.isEmpty()) {
					model.addAttribute("status", "EmptyList");
					return "displayCreatorSongs";
				}
				else {
					model.addAttribute("songslist", song_service.getCreatorSongs(user));
					return "displayCreatorSongs";
				}
			}
			else {
				session.removeAttribute("email");
				return "loginForm";
			}
		}
	}
	
	@GetMapping("/map-deleteSongs")
	public String mapDeleteSongs(HttpSession session, Model model ) {
		Object email=session.getAttribute("email");
		if(email==null) {
			return "loginForm";
		}
		else {
			User user=user_service.getUser((String)email);
			if(user.getRole().equals("creator")) {
				//fetching all songs added by the creator and adding it to the thymeleaf model
				List<Song> songslist =song_service.getCreatorSongs(user);
				if(songslist==null || songslist.isEmpty()) {
					model.addAttribute("status", "EmptyList");
					return "deleteSongsList";
				}
				else {
					model.addAttribute("songslist", songslist);
					return "deleteSongsList";
				}
				
			}
			else {
				session.removeAttribute("email");
				return "loginForm";
			}
		}
		
	}
	
	@PostMapping("/deleteSongs")
	public String deleteSongs(HttpSession session,@RequestParam List<Integer> song_id, Model model) {
		try {
			Object email=session.getAttribute("email");
			if(email==null) {
				return "loginForm";
			}
			else {
				User user=user_service.getUser((String)email);
				if(user.getRole().equals("creator")) {
					for(Integer songID : song_id) {
						Song song=song_service.getSong(songID);
						//song_service.deleteSongfromAll(song, user);
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
							user.getSongs().remove(song);
							user_service.updateUser(user);
							song_service.deleteSongs(songID);
						}
					}
					model.addAttribute("status", "SongDeleted");
					return "deleteSongsList";
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
	
	@GetMapping("/exploreSongs")
	public String exploreSongs(HttpSession session, Model model) {
		Object email=session.getAttribute("email");
		if(email==null) {
			return "loginForm";
		}
		else {
			User user=user_service.getUser((String)email);
			if(user.getRole().equals("user")) {
				if(user.isPremium()==true) {
					List<Song> songslist=song_service.fetchAllSongs();
					if(songslist==null || songslist.isEmpty()) {
						model.addAttribute("status", "EmptyList");
						return "exploreSongs";
					}
					else {
						model.addAttribute("songslist", songslist);
						return "exploreSongs";
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
	
	
}
