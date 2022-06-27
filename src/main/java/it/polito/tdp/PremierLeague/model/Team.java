package it.polito.tdp.PremierLeague.model;

public class Team {
	private Integer teamID;
	private String name;
	private int punti;
	private int reporter;

	public Team(Integer teamID, String name) {
		this.punti = 0;
		this.teamID = teamID;
		this.name = name;
		reporter = 0;
	}

	public int getReporter() {
		return reporter;
	}

	public void setReporter(int reporter) {
		this.reporter = reporter;
	}

	public int getPunti() {
		return punti;
	}

	public void setPunti(int punti) {
		this.punti = punti;
	}

	public Integer getTeamID() {
		return teamID;
	}

	public void setTeamID(Integer teamID) {
		this.teamID = teamID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void vinto() {
		punti += 3;
	}

	public void pareggio() {
		punti++;
	}

	public void add() {
		reporter++;
	}

	public void remove() {
		reporter--;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((teamID == null) ? 0 : teamID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Team other = (Team) obj;
		if (teamID == null) {
			if (other.teamID != null)
				return false;
		} else if (!teamID.equals(other.teamID))
			return false;
		return true;
	}

}
