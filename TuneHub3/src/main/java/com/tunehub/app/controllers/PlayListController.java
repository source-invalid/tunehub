package com.tunehub.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tunehub.app.entities.PlayList;
import com.tunehub.app.entities.Song;
import com.tunehub.app.entities.User;
import com.tunehub.app.services.PlayListService;
import com.tunehub.app.services.SongService;
import com.tunehub.app.services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class PlayListController {
	@Autowired
	UserService user_service;
	@Autowired
	SongService song_service;
	@Autowired
	PlayListService playlist_service;
	
	@GetMapping("/map-createPlaylist")
	public String mapCreatePlaylist(HttpSession session, Model model) {
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
					return "createPlaylistForm";
				}
				else {
					model.addAttribute("songslist", songslist);
					return "createPlaylistForm";
				}
				
			}
			else{
				session.removeAttribute("email");
				return "loginForm";
			}
		}
		
	}
	
	@PostMapping("/createPlaylist")
	public String CreatePlaylist(@ModelAttribute PlayList playlist, HttpSession session, Model model) {
		try {
			Object email=session.getAttribute("email");
			if(email==null) {
				return "loginForm";
			}
			else {
				if(user_service.getRole((String)email).equals("creator")) {
					if(playlist_service.playlistExist(playlist.getName())==false) {
						User user=user_service.getUser((String)email);
						playlist.setCreator(user);
						playlist_service.addPlaylist(playlist);//adding playlist into database
						//Updating song table with playlist
						for(Song song : playlist.getSongs()) {
							song.getPlaylist().add(playlist);
							song_service.updateSong(song);
						}
						//updating user table with playlist
						user.getPlaylists().add(playlist);
						user_service.updateUser(user);
						model.addAttribute("status", "createPlaylistSuccess");
						return "createPlaylistForm";
					}
					else {
						model.addAttribute("status", "createPlaylistFailed");
						return "createPlaylistForm";
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
	
	@GetMapping("/managePlaylists")
	public String managePlaylists(HttpSession session, Model model){
		
		Object email=session.getAttribute("email");
		if(email==null) {
			return "loginForm";
		}
		else {
			User user=user_service.getUser((String)email);
			if(user.getRole().equals("creator")) {
				List<PlayList> playlists =playlist_service.getCreatorPlaylists(user);
				//fetching all songs added by the creator and adding it to the thymeleaf model
				if(playlists==null || playlists.isEmpty()) {
					model.addAttribute("status", "EmptyList");
					return "displayCreatorPlaylist";
				}
				else {
					model.addAttribute("playlists", playlists);
					return "displayCreatorPlaylist";
				}
			}
			else {
				session.removeAttribute("email");
				return "loginForm";
			}
		}
		
	}
	
	
	@GetMapping("/map-deletePlaylists")
	public String mapDeletePlaylist(HttpSession session, Model model) {
		Object email=session.getAttribute("email");
		if(email==null) {
			return "loginForm";
		}
		else {
			User user=user_service.getUser((String)email);
			if(user.getRole().equals("creator")) {
				//fetching all playlists added by the creator and adding it to the thymeleaf model
				List<PlayList> playlists =playlist_service.getCreatorPlaylists(user);
				if(playlists==null || playlists.isEmpty()) {
					model.addAttribute("status", "EmptyList");
					return "deletePlaylistsForm";
				}
				else {
					model.addAttribute("playlists", playlists);
					return "deletePlaylistsForm";
				}
				
			}
			else {
				session.removeAttribute("email");
				return "loginForm";
			}
		}
		
	}
	
	
	@PostMapping("/deletePlaylists")
	public String deletePlaylists(HttpSession session,@RequestParam List<Integer> playlist_id, Model model) {
		try {
			Object email=session.getAttribute("email");
			if(email==null) {
				return "loginForm";
			}
			else {
				User user=user_service.getUser((String)email);
				if(user.getRole().equals("creator")) {
						for(Integer playlistID : playlist_id) {
							if(playlist_service.playlistExistById(playlistID)) { //Checking song exist or not to avoid null error during deletion
								PlayList playlist=playlist_service.getPlaylistById(playlistID);
								for(Song song : playlist.getSongs()) {
									song.getPlaylist().remove(playlist);
									song_service.updateSong(song);
								}
								user.getPlaylists().remove(playlist);
								user_service.updateUser(user);
								playlist_service.deletePlaylist(playlistID);
							}
							
						}
						model.addAttribute("status", "PlaylistDeleted");
						return "deletePlaylistsForm";					
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
	
	@GetMapping("/explorePlaylists")
	public String explorePlaylists(HttpSession session, Model model) {
		Object email=session.getAttribute("email");
		if(email==null) {
			return "loginForm";
		}
		else {
			User user=user_service.getUser((String)email);
			if(user.getRole().equals("user")) {
				if(user.isPremium()==true) {
					List<PlayList> playlists = playlist_service.fetchAllPlaylists();
					if(playlists==null || playlists.isEmpty()) {
						model.addAttribute("status", "EmptyList");
						return "explorePlaylists";
					}
					else {
						model.addAttribute("playlists", playlists);
						return "explorePlaylists";
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
