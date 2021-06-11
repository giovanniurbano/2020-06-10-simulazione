package it.polito.tdp.imdb.model;

public class Event implements Comparable<Event>{
	private int T; //tempo
	private Actor attore; //attore intervistato
	
	public Event(int t, Actor attore) {
		T = t;
		this.attore = attore;
	}

	public int getT() {
		return T;
	}

	public void setT(int t) {
		T = t;
	}

	public Actor getAttore() {
		return attore;
	}

	public void setAttore(Actor attore) {
		this.attore = attore;
	}

	@Override
	public String toString() {
		return "Event [T=" + T + ", attore=" + attore + "]";
	}

	@Override
	public int compareTo(Event o) {
		return this.T - o.T;
	}
	
	
}
