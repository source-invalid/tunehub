package com.tunehub.app.services;

import java.util.List;

import com.tunehub.app.entities.Song;
import com.tunehub.app.entities.User;

public interface SongService {
	
	public String addSong(Song song);

	public boolean songExist(String songID);

	public List<Song> getCreatorSongs(User user);
	
	public String deleteSongs(Integer songID);

	public boolean songExistById(Integer songID);

	public String updateSong(Song song);

	public List<Song> fetchAllSongs();

	//public List<Song> fetchAllFavSongs(User user);

	public Song getSong(Integer songID);

	//public String deleteSongfromAll(Song song, User user);

}
