package it.polito.tdp.imdb.model;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	private ImdbDAO dao;
	private Graph<Actor, DefaultWeightedEdge> grafo;
	
	public Model() {
		this.dao = new ImdbDAO();
	}
	
	public List<String> listAllGenres(){
		return this.dao.listAllGenres();
	}
	
	public String creaGrafo(String genere) {
		this.grafo = new SimpleDirectedWeightedGraph<Actor, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//vertici
		Graphs.addAllVertices(this.grafo, this.dao.getActorsByGenre(genere));
		
		return String.format("Grafo creato con %d vertici e %d archi\n", this.grafo.vertexSet().size(), this.grafo.edgeSet().size());
	}
}
