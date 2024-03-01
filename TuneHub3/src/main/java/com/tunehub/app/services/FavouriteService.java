package com.tunehub.app.services;

import java.util.List;

import com.tunehub.app.entities.Favourite;
import com.tunehub.app.entities.Song;
import com.tunehub.app.entities.User;

public interface FavouriteService {

	public List<Song> fetchAllFavSongs(User user);

	public Favourite getFavByUser(User user);

	public String updateFav(Favourite fav);

	//public String createFav(User user);

	//public List<Favourite> getAllFavBySong(Song song);

}
