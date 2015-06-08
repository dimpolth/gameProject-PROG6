package modele;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;


public class Historique implements Serializable {

	public ArrayList<Terrain> histoPrincipal;
	public ArrayList<Point> histoTour;
	public ArrayList<Coup> hisTourCoup;
	private int itPrincipal, itTour, itCoup;

	
	public Historique() {
		histoPrincipal = new ArrayList<Terrain>();
		histoTour = new ArrayList<Point>();
		hisTourCoup =new ArrayList<Coup>();
 		itPrincipal = 0;
		itTour = 0;
		itCoup = 0 ;
	}
	
	public Historique(Historique h) {
		histoPrincipal = h.histoPrincipal;
		histoTour = h.histoTour;
		itPrincipal = h.itPrincipal;
		itTour = h.itTour;
		itCoup = h.itCoup;
	}
	
	public int getItPrincipal(){
		return itPrincipal;
	}
	
	public void ajouterTour(Terrain t) {
		if(itPrincipal < histoPrincipal.size()-1) {
			for(int i = histoPrincipal.size()-1; i > itPrincipal; i--) { 
				histoPrincipal.remove(i);
			}
		}
		Terrain c = t.copie();
		histoPrincipal.add(c);
		itPrincipal = histoPrincipal.size()-1;
		
		itTour=0;
		itCoup=0;
		hisTourCoup = new ArrayList<Coup>() ;
		
		//;//System.out.println("itprincipal"+itPrincipal);
		
	}
	
	public void ajouterCoup(Point pDepart,Point pArrive) {
		histoTour.add((Point)pDepart.clone());
		hisTourCoup.add(new Coup(pDepart,pArrive));
		itTour++;
		itCoup++;
		
	}
	
	public Coup getDernierCoup(){
		return hisTourCoup.get(hisTourCoup.size()-1);
	}
	
	
	public Terrain annuler() {
		if(itPrincipal<=0){
			//;//System.out.println("PAS de ANNULER POSSIBLE");
			return null;}
		else {
			
			Terrain temp =histoPrincipal.get(--itPrincipal);
			//temp.dessineTableauAvecIntersections();
			return temp;
		}
	}
	
	public Terrain refaire() {
		if(itPrincipal == histoPrincipal.size()-1)
			return null;
		else {
			return histoPrincipal.get(++itPrincipal);
		}
	}
	
	public void effacerHistoTour() {
		histoTour = new ArrayList<Point>();
		itTour=0;
		itCoup=0;
		
	}
	
	public void afficher() {
		;//System.out.println("----------------");
		for(int it = 0; it < histoPrincipal.size(); it++) {
			Terrain tmp = histoPrincipal.get(it);
			//Terrain tmp = histoPrincipal.get(itPrincipal);
			//tmp.dessineTableauAvecIntersections();
		}
		;//System.out.println("----------------");
		;//System.out.println(itPrincipal);
		;//System.out.println("----------------");
	}
	
}
