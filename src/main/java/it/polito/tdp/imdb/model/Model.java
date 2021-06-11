package it.polito.tdp.imdb.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	private ImdbDAO dao;
	private Map<Integer, Actor> idMap;
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
		this.idMap = new HashMap<Integer, Actor>();
		for(Actor a : this.grafo.vertexSet()) {
			this.idMap.put(a.getId(), a);
		}
		
		//archi
		List<Adiacenza> archi = this.dao.getAdiacenze(genere, idMap);
		for(Adiacenza arco : archi) {
			Graphs.addEdge(this.grafo, arco.getA1(), arco.getA2(), arco.getPeso());
		}
		
		return String.format("Grafo creato con %d vertici e %d archi\n", this.grafo.vertexSet().size(), this.grafo.edgeSet().size());
	}
}
