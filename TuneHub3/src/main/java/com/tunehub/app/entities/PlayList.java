package com.tunehub.app.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class PlayList {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String name;
	@ManyToOne
	private User creator;
	
	@ManyToMany
	private List<Song> songs;

	public PlayList() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PlayList(int id, String name, User creator, List<Song> songs) {
		super();
		this.id = id;
		this.name = name;
		this.creator = creator;
		this.songs = songs;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public List<Song> getSongs() {
		return songs;
	}

	public void setSongs(List<Song> songs) {
		this.songs = songs;
	}

	@Override
	public String toString() {
		return "PlayList [id=" + id + ", name=" + name + ", creator=" + creator + ", songs=" + songs + "]";
	}
	
}
