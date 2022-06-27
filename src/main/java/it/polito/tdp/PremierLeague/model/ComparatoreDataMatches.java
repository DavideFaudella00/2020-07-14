package it.polito.tdp.PremierLeague.model;

import java.util.Comparator;

public class ComparatoreDataMatches implements Comparator<Match> {

	@Override
	public int compare(Match o1, Match o2) {
		return o1.getDate().compareTo(o2.getDate());
	}

}
