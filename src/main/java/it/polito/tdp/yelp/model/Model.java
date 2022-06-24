package it.polito.tdp.yelp.model;

import java.util.HashMap;
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
	
	public Model() {
		this.dao = new YelpDao();
		this.mapId = new HashMap<>();
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
		//System.out.println(b.getBusinessName()+" "+(entranti-uscenti));
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
	
	
	
}
