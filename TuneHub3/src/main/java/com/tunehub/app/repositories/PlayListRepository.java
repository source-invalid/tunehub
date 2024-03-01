package com.tunehub.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tunehub.app.entities.PlayList;
import com.tunehub.app.entities.User;

import java.util.List;


public interface PlayListRepository extends JpaRepository<PlayList, Integer>{
	
	public PlayList findByName(String name);
	
	List<PlayList> findByCreator(User creator);
}
