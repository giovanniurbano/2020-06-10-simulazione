package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	private ImdbDAO dao;
	private Map<Integer, Actor> idMap;
	private List<Actor> vertici;
	private Graph<Actor, DefaultWeightedEdge> grafo;
	
	private Simulator sim;
	
	public Model() {
		this.dao = new ImdbDAO();
	}
	
	public List<String> listAllGenres(){
		return this.dao.listAllGenres();
	}
	
	public String creaGrafo(String genere) {
		this.grafo = new SimpleDirectedWeightedGraph<Actor, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//vertici
		this.vertici = new ArrayList<Actor>(this.dao.getActorsByGenre(genere));
		Graphs.addAllVertices(this.grafo, this.vertici);
		this.idMap = new HashMap<Integer, Actor>();
		for(Actor a : this.vertici) {
			this.idMap.put(a.getId(), a);
		}
		
		//archi
		List<Adiacenza> archi = this.dao.getAdiacenze(genere, idMap);
		for(Adiacenza arco : archi) {
			Graphs.addEdge(this.grafo, arco.getA1(), arco.getA2(), arco.getPeso());
		}
		
		return String.format("Grafo creato con %d vertici e %d archi\n", this.grafo.vertexSet().size(), this.grafo.edgeSet().size());
	}
	
	public Graph<Actor, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}

	public List<Actor> getVertici() {
		return this.vertici;
	}
	
	public List<Actor> getSimili(Actor attore) {
		ConnectivityInspector<Actor, DefaultWeightedEdge> ci = new ConnectivityInspector<>(this.grafo);
		Set<Actor> simili = ci.connectedSetOf(attore);
		List<Actor> s = new ArrayList<Actor>(simili);
		Collections.sort(s);
		return s;
	}
	
	public List<Actor> simula(int n) {
		this.sim = new Simulator(n, grafo);
		this.sim.run();
		
		return sim.getIntervistati();
	}
	public int getPause() {
		return this.sim.getnPause();
	}
}
