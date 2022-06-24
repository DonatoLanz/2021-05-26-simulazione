package it.polito.tdp.yelp.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	
	private Graph<Business,DefaultWeightedEdge> grafo;
	private YelpDao dao;
	private Map<String,Business> mapId;
	private List<Business> best;
	
	public Model() {
		this.dao = new YelpDao();
		this.mapId = new HashMap<>();
		this.best = new LinkedList<>();
	}
	
	public List<String> getCities(){
		return dao.getCities();
	}
	
	public String creaGrafo(String city, int anno) {
		this.grafo = new SimpleDirectedWeightedGraph<Business,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		List<Business> vertici = dao.getBusinessAnnoCitta(city, anno, this.mapId);
		Graphs.addAllVertices(this.grafo, vertici);
		
		
		for(Coppia c : dao.getCoppie(city, anno)) {
			if(c.getM1()>c.getM2()) {
				Graphs.addEdgeWithVertices(this.grafo, this.mapId.get(c.getB2()), this.mapId.get(c.getB1()), c.getM1()-c.getM2());
			}else {
				Graphs.addEdgeWithVertices(this.grafo, this.mapId.get(c.getB1()), this.mapId.get(c.getB2()), c.getM2()-c.getM1());
			}
		}
		
		return this.grafo.vertexSet().size()+" "+this.grafo.edgeSet().size();
	}
	
	public double entrantiMenoUscenti(Business b) {
		double entranti = 0.0;
		double uscenti = 0.0;
		
		for(DefaultWeightedEdge a : this.grafo.incomingEdgesOf(b)) {
			entranti += this.grafo.getEdgeWeight(a);
		}
		
		for(DefaultWeightedEdge a : this.grafo.outgoingEdgesOf(b)) {
			uscenti += this.grafo.getEdgeWeight(a);
		}
		
		return entranti-uscenti;
		
	}
	
	public Business migliore() {
		
		double max = -100;
		Business migliore = null;
		
		for(Business b : this.grafo.vertexSet()) {
			System.out.println(b.getBusinessName()+" "+entrantiMenoUscenti(b));
			
			if(entrantiMenoUscenti(b) > max) {
				max = entrantiMenoUscenti(b);
			}
		}
		

		for(Business bb : this.grafo.vertexSet()) {
			if(entrantiMenoUscenti(bb) == max) {
				migliore = bb;
				break;
			}
		}
		return migliore;
		
	}
	
	
	public List<Business> calcolarePercorso(Business partenza, Business arrivo, double soglia){
		List<Business> parziale = new LinkedList<Business>();
		parziale.add(partenza);
		ricorsione(arrivo,soglia,parziale);
		return best;
		
	}

	private void ricorsione(Business arrivo, double soglia, List<Business> parziale) {
	
		//sonoArrivatoADestinazione
		if(parziale.get(parziale.size()-1).equals(arrivo)==true) {
			if(best.size()==0) {
				best = new LinkedList<>(parziale);
                return;
			}else {
				if(parziale.size() < best.size()) {
					best = new LinkedList<>(parziale);
				}
			}
			return;
		}
		
		for(Business b : successiviPossibili(parziale, soglia)) {
			if(!parziale.contains(b)) {
				parziale.add(b);
				ricorsione(arrivo, soglia, parziale);
				parziale.remove(parziale.size()-1);
			}
		}
		
		
	}
	
	public List<Business> successiviPossibili(List<Business> parziale, double soglia){
		
		List<Business> successivi = new LinkedList<>();
		
		for(DefaultWeightedEdge d : this.grafo.outgoingEdgesOf(parziale.get(parziale.size()-1))) {
			if(this.grafo.getEdgeWeight(d)>soglia) {
				successivi.add(Graphs.getOppositeVertex(this.grafo, d, parziale.get(parziale.size()-1)));
			}
		}
		
		return successivi;
	}

	
	public List<Business> vertici(){
		List<Business> v = new LinkedList<>(this.grafo.vertexSet());
		return v;
	}
	
}
