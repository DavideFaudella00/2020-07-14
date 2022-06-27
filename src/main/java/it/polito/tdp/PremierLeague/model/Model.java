package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;
import it.polito.tdp.PremierLeague.model.Event.EventType;

public class Model {
	PremierLeagueDAO dao = new PremierLeagueDAO();
	List<Team> squadre;
	SimpleDirectedWeightedGraph<Team, DefaultWeightedEdge> grafo;
	Map<Integer, Team> team;

	public void creaGrafo() {
		listTeam();
		team = new HashMap<Integer, Team>();
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, squadre);

		for (Team t : squadre) {
			team.put(t.getTeamID(), t);
		}

		assegnaPunti();
		List<Adiacenza> adiacenze = this.getAdiacenze();

		for (Adiacenza a : adiacenze) {
			if (a.getPeso() > 0) {
				Graphs.addEdgeWithVertices(grafo, a.getT1(), a.getT2(), a.getPeso());
			} else if (a.getPeso() < 0) {
				Graphs.addEdgeWithVertices(grafo, a.getT2(), a.getT1(), a.getPeso());
			}
		}
		System.out.println(grafo.vertexSet().size());
		System.out.println(grafo.edgeSet().size());
	}

	public void listTeam() {
		squadre = dao.listAllTeams();
	}

	public List<Adiacenza> getAdiacenze() {
		List<Adiacenza> result = new ArrayList<>();
		for (Team t1 : squadre) {
			for (Team t2 : squadre) {
				if ((t1.getPunti() - t2.getPunti()) != 0) {
					Adiacenza a = new Adiacenza(t1, t2, (t1.getPunti() - t2.getPunti()));
					result.add(a);
				}
			}
		}
		return result;
	}

	public void assegnaPunti() {
		List<Match> match = dao.listAllMatches();
		for (Match m : match) {
			if (m.getResultOfTeamHome() == 1) {
				team.get(m.getTeamHomeID()).vinto();
			} else if (m.getResultOfTeamHome() == 0) {
				team.get(m.getTeamHomeID()).pareggio();
				team.get(m.getTeamAwayID()).pareggio();
			} else if (m.getResultOfTeamHome() == -1) {
				team.get(m.getTeamAwayID()).vinto();
			}

		}
	}

	public List<Team> getPeggiori(Team t) {
		List<Team> peggiori = new ArrayList<>();
		for (Team k : squadre) {
			if (t.getPunti() > k.getPunti()) {
				peggiori.add(k);
			}
		}
		peggiori.sort(new CompPeggiori());
		return peggiori;
	}

	public List<Team> getMigliori(Team t) {
		List<Team> migliori = new ArrayList<>();
		for (Team k : squadre) {
			if (t.getPunti() < k.getPunti()) {
				migliori.add(k);
			}
		}
		migliori.sort(new CompMigliori());
		return migliori;
	}

	public String getString(Team t) {
		String s = "SQUADRE MIGLIORI\n";
		for (Team k : getMigliori(t)) {
			s += k.getName() + " (" + (-1 * (t.getPunti() - k.getPunti()) + ")\n");
		}
		s += "\nSQUADRE PEGGIORI\n";
		for (Team k : getPeggiori(t)) {
			s += k.getName() + " (" + (t.getPunti() - k.getPunti() + ")\n");
		}
		return s;
	}

	public List<Team> getSquadre() {
		return squadre;
	}

	public List<Match> getMatchOrdinati() {
		dao = new PremierLeagueDAO();
		List<Match> result = dao.listAllMatches();
		Collections.sort(result, new ComparatoreDataMatches());
		return result;
	}

	private PriorityQueue<Event> queue;
	List<Match> matchList;
	Map<Integer, Team> map;
	double somma;

	private void creaEventi(int n) {
		matchList = getMatchOrdinati();
		map = new HashMap<>();
		EventType tipo = null;

		for (Team t : squadre) {
			map.put(t.getTeamID(), t);
			t.setReporter(n);
		}

		for (Match m : matchList) {
			if (m.getResultOfTeamHome() == 1) {
				tipo = EventType.VITTORIA;
			} else if (m.getResultOfTeamHome() == 0) {
				tipo = EventType.PAREGGIO;
			} else {
				tipo = EventType.SCONFITTA;
			}
			Event e = new Event(tipo);
			queue.add(e);
		}
	}

	public void init(int n) {
		queue = new PriorityQueue<>();
		somma = 0;
		creaEventi(n);
	}

	public String run() {
		String result = "";
		int i = 0;
		while (!queue.isEmpty()) {
			Event e = queue.poll();
			processaEvento(e, matchList.get(i));
			i++;
		}
		result = "media = " + (somma / matchList.size());
		return result;
	}

	public void processaEvento(Event e, Match m) {
		switch (e.getTipo()) {
		case PAREGGIO:
			somma += map.get(m.getTeamAwayID()).getReporter() + map.get(m.getTeamHomeID()).getReporter();
			break;
		case SCONFITTA:
			somma += map.get(m.getTeamAwayID()).getReporter() + map.get(m.getTeamHomeID()).getReporter();
			Team tp1 = map.get(m.getTeamHomeID());
			Team tv1 = map.get(m.getTeamAwayID());
			if (tv1.getReporter() > 0 && Math.random() > 0.5) {
				int x = getMigliori(tv1).size();
				if (x > 0) {
					tv1.remove();
					getMigliori(tv1).get((int) (Math.random() * x)).add();
				}
			}
			if (tp1.getReporter() > 0 && Math.random() < 0.2) {
				int y = getPeggiori(tp1).size();
				if (y > 0) {
					int i;
					for (i = 0; i < (Math.random() * y); i++) {
						tp1.remove();
						getPeggiori(tp1).get((int) (Math.random() * y)).add();
					}
				}
			}

			break;
		case VITTORIA:
			somma += map.get(m.getTeamAwayID()).getReporter() + map.get(m.getTeamHomeID()).getReporter();
			Team tv = map.get(m.getTeamHomeID());
			Team tp = map.get(m.getTeamAwayID());
			if (tv.getReporter() > 0 && Math.random() > 0.5) {
				int x = getMigliori(tv).size();
				if (x > 0) {
					tv.remove();
					getMigliori(tv).get((int) (Math.random() * x)).add();
				}
			}
			if (tp.getReporter() > 0 && Math.random() < 0.2) {
				int y = getPeggiori(tp).size();
				if (y > 0) {
					int i;
					for (i = 0; i < (Math.random() * y); i++) {
						tp.remove();
						getPeggiori(tp).get((int) (Math.random() * y)).add();
					}
				}
			}
			break;
		}
	}
}
