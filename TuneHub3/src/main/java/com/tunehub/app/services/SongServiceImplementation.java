package com.tunehub.app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tunehub.app.entities.Favourite;
import com.tunehub.app.entities.PlayList;
import com.tunehub.app.entities.Song;
import com.tunehub.app.entities.User;
import com.tunehub.app.repositories.SongRepository;
@Service
public class SongServiceImplementation implements SongService {
	
	@Autowired
	SongRepository song_repo;

	@Override
	public String addSong(Song song) {
		song_repo.save(song);
		return "Song Saved";
	}

	@Override
	public boolean songExist(String name) {
		
		if(song_repo.findByName(name)==null) {
			return false;
		}
		else {
			return true;
		}
		
	}


	@Override
	public List<Song> getCreatorSongs(User user) {
		return song_repo.findByCreator(user);
	}

	@Override
	public String deleteSongs(Integer songID) {
		song_repo.deleteById(songID);
		return "Deleted";
	}

	@Override
	public boolean songExistById(Integer songID) {
		if(song_repo.findById(songID)==null) {
			return false;
		}
		else {
			return true;
		}
		
	}

	@Override
	public String updateSong(Song song) {
		song_repo.save(song);
		return "Song updated";
	}

	@Override
	public List<Song> fetchAllSongs() {
		
		return song_repo.findAll();
	}

//	@Override
//	public List<Song> fetchAllFavSongs(User user) {
//		return song_repo.findByUserFavourite(user);
//	}

	@Override
	public Song getSong(Integer songID) {
		return song_repo.getById(songID);
	}





}
