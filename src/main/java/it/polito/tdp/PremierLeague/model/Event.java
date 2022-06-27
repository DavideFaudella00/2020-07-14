package it.polito.tdp.PremierLeague.model;

public class Event implements Comparable<Event> {

	private EventType tipo;

	public enum EventType {
		VITTORIA, PAREGGIO, SCONFITTA;
	}

	@Override
	public int compareTo(Event o) {
		return 0;
	}

	public Event(EventType tipo) {
		this.tipo = tipo;
	}

	public EventType getTipo() {
		return tipo;
	}

	public void setTipo(EventType tipo) {
		this.tipo = tipo;
	}

}
