package it.polito.tdp.imdb.model;

import java.util.List;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	private ImdbDAO dao;
	
	public Model() {
		this.dao = new ImdbDAO();
	}
	
	public List<String> listAllGenres(){
		return this.dao.listAllGenres();
	}
}
