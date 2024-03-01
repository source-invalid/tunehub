package com.tunehub.app.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.tunehub.app.entities.Favourite;
import com.tunehub.app.entities.User;



public interface FavouriteRepository extends JpaRepository<Favourite, Integer>{
	
	public Favourite findByUser(User user);
	
	//public List<Favourite> findByFav_songs(Song fav_songs);
	
	
}
