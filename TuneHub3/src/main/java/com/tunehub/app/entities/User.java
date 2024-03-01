package com.tunehub.app.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String name;
	private String email;
	private String password;
	private String gender;
	private String role;
	private boolean isPremium;
	@OneToMany(mappedBy = "creator")
	private List<Song> songs;
	
	@OneToMany(mappedBy = "creator")
	private List<PlayList> playlists;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Favourite userFavourite;
	
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}


	public User(int id, String name, String email, String password, String gender, String role, boolean isPremium,
			List<Song> songs, List<PlayList> playlists, Favourite userFavourite) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.gender = gender;
		this.role = role;
		this.isPremium = isPremium;
		this.songs = songs;
		this.playlists = playlists;
		this.userFavourite = userFavourite;
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


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getGender() {
		return gender;
	}


	public void setGender(String gender) {
		this.gender = gender;
	}


	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}


	public boolean isPremium() {
		return isPremium;
	}


	public void setPremium(boolean isPremium) {
		this.isPremium = isPremium;
	}


	public List<Song> getSongs() {
		return songs;
	}


	public void setSongs(List<Song> songs) {
		this.songs = songs;
	}


	public List<PlayList> getPlaylists() {
		return playlists;
	}


	public void setPlaylists(List<PlayList> playlists) {
		this.playlists = playlists;
	}


	public Favourite getUserFavourite() {
		return userFavourite;
	}


	public void setUserFavourite(Favourite userFavourite) {
		this.userFavourite = userFavourite;
	}


	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", gender="
				+ gender + ", role=" + role + ", isPremium=" + isPremium + ", userFavourite=" + userFavourite + "]";
	}


	
	
	
	
	
}
