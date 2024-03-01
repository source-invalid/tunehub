package com.tunehub.app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tunehub.app.entities.PlayList;
import com.tunehub.app.entities.User;
import com.tunehub.app.repositories.PlayListRepository;

@Service
public class PlayListServiceImplementation implements PlayListService{
	@Autowired
	PlayListRepository playlist_repo;

	@Override
	public boolean playlistExist(String name) {
		if(playlist_repo.findByName(name)==null) {
			return false;
		}
		else {
			return true;
		}
		
	}

	@Override
	public String addPlaylist(PlayList playlist) {
		playlist_repo.save(playlist);
		return "playlist saved";
	}

	@Override
	public String updatePlaylist(PlayList playlist) {
		playlist_repo.save(playlist);
		return "playlist updated";
	}
	
	@Override
	public List<PlayList> getCreatorPlaylists(User user) {
		return playlist_repo.findByCreator(user);
	}

	@Override
	public boolean playlistExistById(Integer playlistID) {
		if(playlist_repo.findById(playlistID)==null) {
			return false;
		}
		else {
			return true;
		}
		
	}

	@Override
	public String deletePlaylist(Integer playlistID) {
		playlist_repo.deleteById(playlistID);
		return "playlist deleted";
	}

	@Override
	public List<PlayList> fetchAllPlaylists() {
		return playlist_repo.findAll();
	}

	@Override
	public PlayList getPlaylistById(Integer playlistID) {
		return playlist_repo.getById(playlistID);
	}

	

	
}
