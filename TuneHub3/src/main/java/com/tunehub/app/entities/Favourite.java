package com.tunehub.app.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
@Entity
public class Favourite {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@OneToOne(mappedBy = "userFavourite")
	private User user;
	@ManyToMany
	private List<Song> fav_songs;
	public Favourite() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Favourite(int id, User user, List<Song> fav_songs) {
		super();
		this.id = id;
		this.user = user;
		this.fav_songs = fav_songs;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<Song> getFav_songs() {
		return fav_songs;
	}
	public void setFav_songs(List<Song> fav_songs) {
		this.fav_songs = fav_songs;
	}
	@Override
	public String toString() {
		return "Favourite [id=" + id + ", user=" + user;// + ", fav_songs=" + fav_songs + "]";
	}
	
	

}
