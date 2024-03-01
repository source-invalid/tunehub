package com.tunehub.app.services;

import java.util.List;

import com.tunehub.app.entities.PlayList;
import com.tunehub.app.entities.User;

public interface PlayListService {

	public boolean playlistExist(String name);

	public String addPlaylist(PlayList playlist);

	public List<PlayList> getCreatorPlaylists(User user);

	public boolean playlistExistById(Integer playlistID);

	public String deletePlaylist(Integer playlistID);

	public List<PlayList> fetchAllPlaylists();
	
	public String updatePlaylist(PlayList playlist);

	public PlayList getPlaylistById(Integer playlistID);

}
