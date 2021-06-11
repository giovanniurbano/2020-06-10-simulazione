package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Simulator {
	//coda degli eventi
	private List<Event> queue;
	
	//modello del mondo
	private int gender;
	private List<Actor> daEstrarre;
	
	//parametri di input
	private int n;
	private Graph<Actor, DefaultWeightedEdge> grafo;
	private double probSceltaCasuale = 0.6;
	private double probPausa = 0.9;
	
	//valori di output
	private List<Actor> intervistati;
	private int nPause;
	
	public Simulator(int N, Graph<Actor, DefaultWeightedEdge> grafo) {
		this.n = N;
		this.grafo = grafo;
		this.daEstrarre = new ArrayList<Actor>();
		for(Actor a : this.grafo.vertexSet()) {
			this.daEstrarre.add(a);
		}
		this.gender = 0;
		
		this.queue = new ArrayList<Event>();
		this.intervistati = new ArrayList<Actor>();
		
		//scelgo il primo casualmente
		int primo = (int) Math.floor(Math.random()*(this.daEstrarre.size()));
		Actor first = this.daEstrarre.get(primo);

		//lo aggiungo
		this.queue.add(new Event(0, first));
		this.intervistati.add(first);
		this.daEstrarre.remove(primo);
		this.gender++;
		
		//imposto allo stato iniziale la coda
		for(int giorno=1; giorno<this.n; giorno++) {
			this.queue.add(new Event(giorno, null));
		}
	}
	
	public void run() {
		for(int giorno=1; giorno<this.n; giorno++) {
			if(gender == 2 && Math.random() < this.probPausa) {
				//pausa
				
				this.nPause++;
				gender = 0;
				continue;
			}
			else if(gender == 0) {
				//giorno dopo la pausa, scelta casuale tra i non intervistati
				boolean scelto = false;
				while(scelto != true)
					scelto = this.sceltaCasuale(giorno);
				
				gender++;
			}
			else {
				//giorno di intervista ordinario
				
				if(Math.random() < this.probSceltaCasuale) {
					//scelta casuale al tempo n
					boolean scelto = false;
					while(scelto != true)
						scelto = this.sceltaCasuale(giorno);
					
					if(this.queue.get(giorno).getAttore().getGender().equals(this.queue.get(giorno-1).getAttore().getGender()))
						this.gender++;
				}
				else {
					//consiglio dell'attore
					
					if(this.grafo.degreeOf(this.queue.get(giorno-1).getAttore()) != 0) {
						//ha lavorato con qualcuno
						double max = 0;
						Actor next = null;
						
						for(DefaultWeightedEdge edge : this.grafo.edgesOf(this.queue.get(giorno-1).getAttore())) {
							if(this.grafo.getEdgeWeight(edge) > max) {
								max = this.grafo.getEdgeWeight(edge);
								next = Graphs.getOppositeVertex(this.grafo, edge, this.queue.get(giorno-1).getAttore());
							}
						}
						this.queue.get(giorno).setAttore(next);
						this.intervistati.add(next);
						if(next.getGender().equals(this.queue.get(giorno-1).getAttore().getGender()))
							this.gender++;
						
						this.daEstrarre.remove(next);
					}
					else {
						//non ha lavorato con nessuno, scelta casuale
						boolean scelto = false;
						while(scelto != true)
							scelto = this.sceltaCasuale(giorno);
						if(this.queue.get(giorno).getAttore().getGender().equals(this.queue.get(giorno-1).getAttore().getGender()))
							this.gender++;
					}
				}
			}	
		}
	}
	
	private boolean sceltaCasuale(int giorno) {
		int prossimo = (int) Math.floor(Math.random()*(this.daEstrarre.size()));
		if(this.daEstrarre.get(prossimo) != null) {
			this.queue.get(giorno).setAttore(this.daEstrarre.get(prossimo));
			this.intervistati.add(this.daEstrarre.get(prossimo));
			
			this.daEstrarre.remove(prossimo);
			return true;
		}
		else {
			return false;
		}
	}

	public List<Actor> getIntervistati() {
		return intervistati;
	}

	public int getnPause() {
		return nPause;
	}
	
}
