package modele;
import java.awt.Point;
import java.util.ArrayList;


public class Historique {

	ArrayList<Terrain> histoPrincipal;
	public ArrayList<Point> histoTour;
	int itPrincipal, itTour;
	boolean joueur; //true = j1, false = j2
	
	public Historique() {
		histoPrincipal = new ArrayList<Terrain>();
		histoTour = new ArrayList<Point>();
		itPrincipal = 0;
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
		//System.out.println("itprincipal"+itPrincipal);
		
	}
	
	public void ajouterCoup(Point p) {
		histoTour.add((Point)p.clone());
		itTour++;
		
	}
	
	public Terrain annuler() {
		System.out.println("Dessin moteur");
		if(histoPrincipal.isEmpty())
			return null;
		else {
			
			Terrain temp =histoPrincipal.get(itPrincipal--);
			temp.dessineTableauAvecIntersections();
			return temp;
		}
	}
	
	public Terrain refaire() {
		if(itPrincipal == histoPrincipal.size()-1)
			return null;
		else {
			return histoPrincipal.get(itPrincipal++);
		}
	}
	
	public void effacerHistoTour() {
		histoTour = new ArrayList<Point>();
	}
	
	void afficher() {
		System.out.println("----------------");
		//for(int it = 0; it < histoPrincipal.size(); it++) {
			//Terrain tmp = histoPrincipal.get(it);
			Terrain tmp = histoPrincipal.get(itPrincipal);
			tmp.dessineTableauAvecIntersections();
		//}
		System.out.println("----------------");
	}
	
}
