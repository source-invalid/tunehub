package com.tunehub.app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tunehub.app.entities.Favourite;
import com.tunehub.app.entities.Song;
import com.tunehub.app.entities.User;
import com.tunehub.app.repositories.FavouriteRepository;

@Service
public class FavouriteServiceImplementation implements FavouriteService{
	@Autowired
	FavouriteRepository fav_repo;

	@Override
	public List<Song> fetchAllFavSongs(User user) {
		return fav_repo.findByUser(user).getFav_songs();
	}

	@Override
	public Favourite getFavByUser(User user) {
		return fav_repo.findByUser(user);
	}

	@Override
	public String updateFav(Favourite fav) {
		fav_repo.save(fav);
		return  "Favourite updated";
	}

//	@Override
//	public String createFav(User user) {
//		Favourite fav=new Favourite(0,user,null);
//		fav_repo.save(fav);
//		user.setUserFavourite(fav);
//		return "Favourite Created";
//	}

//	@Override
//	public List<Favourite> getAllFavBySong(Song song) {
//		
//		return fav_repo.findByFav_songs(song);
//	}

}
