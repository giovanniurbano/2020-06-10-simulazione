package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	private ImdbDAO dao;
	private Graph<Actor, DefaultWeightedEdge> grafo;
	private List<Actor> vertici;
	private Map<Integer, Actor> idMap;
	
	public Model() {
		this.dao = new ImdbDAO();
	}
	
	public List<String> listAllGenres() {
		return this.dao.listAllGenres();
	}
	
	public String creaGrafo(String genere) {
		this.grafo = new SimpleWeightedGraph<Actor, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//vertici
		this.vertici = this.dao.getActorsByGenre(genere);
		this.idMap = new HashMap<Integer, Actor>();
		for(Actor a : this.vertici)
			this.idMap.put(a.getId(), a);
		Graphs.addAllVertices(this.grafo, this.vertici);
		
		//archi
		List<Adiacenza> archi = this.dao.getAdiacenze(genere, this.idMap);
		for(Adiacenza a : archi)
			Graphs.addEdge(this.grafo, a.getA1(), a.getA2(), a.getPeso());
		
		return String.format("Grafo creato con %d vertici e %d archi\n", this.grafo.vertexSet().size(), this.grafo.edgeSet().size());
	}

	public Graph<Actor, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}

	public List<Actor> getVertici() {
		return vertici;
	}

	public List<Actor> getAttoriSimili(Actor a) {
		ConnectivityInspector<Actor, DefaultWeightedEdge> ci = new ConnectivityInspector<>(this.grafo);
		List<Actor> r = new ArrayList<Actor>();
		for(Actor aa : ci.connectedSetOf(a))
			if(!aa.equals(a))
				r.add(aa);
		Collections.sort(r);
		return r;
	}
}
