package it.polito.tdp.PremierLeague.model;

import java.util.Comparator;

public class CompMigliori implements Comparator<Team>{

	@Override
	public int compare(Team o1, Team o2) {
		return o1.getPunti() - o2.getPunti();
	}

}
