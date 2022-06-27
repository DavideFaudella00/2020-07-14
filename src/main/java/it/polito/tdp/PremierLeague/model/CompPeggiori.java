package it.polito.tdp.PremierLeague.model;

import java.util.Comparator;

public class CompPeggiori implements Comparator<Team> {

	@Override
	public int compare(Team o1, Team o2) {
		// TODO Auto-generated method stub
		return o2.getPunti() - o1.getPunti();
	}

}
