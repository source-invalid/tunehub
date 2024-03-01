package com.tunehub.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tunehub.app.entities.Song;
import com.tunehub.app.entities.User;

public interface SongRepository extends JpaRepository<Song, Integer>{

	public Song findByName(String name);
	
	public List<Song> findByCreator(User creator);

	//public List<Song> findByUserFavourite(User user);
	

}
