package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Simulator {
	//coda degli eventi
	private List<Actor> intervistati;
	
	//modello del mondo
	private List<Actor> daIntervistare;
	private int gender;
	
	//parametri input
	private int n;
	private Graph<Actor, DefaultWeightedEdge> grafo;
	
	private double probConsiglio = 0.4;
	private double probPausa = 0.9;
	
	//valori output
	private int giorniPausa;
	
	public Simulator(int n, Graph<Actor, DefaultWeightedEdge> grafo) {
		this.n = n;
		this.grafo = grafo;
		
		this.giorniPausa = 0;
		
		//inizializzo il mondo
		this.gender = 0;
		this.intervistati = new ArrayList<Actor>();
		this.daIntervistare = new LinkedList<Actor>();
		for(Actor a : this.grafo.vertexSet())
			this.daIntervistare.add(a);
		
		//inizializzo la coda e scelgo il primo attore
		int primo = (int) Math.round(Math.random()*this.daIntervistare.size());
		Actor first = this.daIntervistare.get(primo);
		this.daIntervistare.remove(primo);
		this.intervistati.add(first);
		this.gender++;
		
		this.run();
	}

	private void run() {
		for(int giorno=1; giorno<this.n; giorno++) {
			if(this.gender == 2) {
				//due attori dello stesso genere
				this.gender = 0;
				if(Math.random() < this.probPausa) {
					//giorno di pausa
					this.giorniPausa++;
					continue;
				}
				else {
					//normale giorno di lavoro
					this.normaleGiornoDiLavoro();
				}
			}
			else if(this.gender == 0) {
				//giorno dopo la pausa, scelta casuale
				this.sceltaCasuale();
				this.gender++;
			}
			else {
				//normale giorno di lavoro
				this.normaleGiornoDiLavoro();
			}
		}
	}
	
	private void normaleGiornoDiLavoro() {
		if(Math.random() < this.probConsiglio) {
			//chiedo consiglio all'ultimo attore
			Set<DefaultWeightedEdge> archiAttore = this.grafo.outgoingEdgesOf(this.intervistati.get(this.intervistati.size()-1));
			if(archiAttore.isEmpty()) {
				//attore non ha consigli, scelta casuale
				this.sceltaCasuale();
				if(this.intervistati.get(this.intervistati.size()-1).getGender().equals(this.intervistati.get(this.intervistati.size()-2).getGender()))
					this.gender++;
				else
					this.gender = 0;
			}
			else {
				//seguo il consiglio
				double pesoMax = 0;
				Actor next = null;
				for(DefaultWeightedEdge arco : archiAttore) {
					if(this.daIntervistare.contains(this.grafo.getEdgeTarget(arco))) {
						double peso = this.grafo.getEdgeWeight(arco);
						if(peso > pesoMax) {
							pesoMax = peso;
							next = this.grafo.getEdgeTarget(arco);
						}
					}
				}
				if(next != null) {
					this.intervistati.add(next);
					this.daIntervistare.remove(this.daIntervistare.lastIndexOf(next));
				}
				else {
					this.sceltaCasuale();
				}
				if(this.intervistati.get(this.intervistati.size()-1).getGender().equals(this.intervistati.get(this.intervistati.size()-2).getGender()))
					this.gender++;
				else
					this.gender = 0;
			}
		}
		else {
			//scelgo casualmente
			this.sceltaCasuale();
			if(this.intervistati.get(this.intervistati.size()-1).getGender().equals(this.intervistati.get(this.intervistati.size()-2).getGender()))
				this.gender++;
			else
				this.gender = 0;
		}
	}
	
	private void sceltaCasuale() {
		int indice = (int) Math.round(Math.random()*this.daIntervistare.size());
		Actor ac = this.daIntervistare.get(indice);
		this.daIntervistare.remove(indice);
		this.intervistati.add(ac);
	}
	
	public List<Actor> getIntervistati() {
		return intervistati;
	}

	public int getGiorniPausa() {
		return giorniPausa;
	}
}
