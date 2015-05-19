import java.awt.Point;
import java.util.ArrayList;


public class Historique {

	ArrayList<Terrain> histoPrincipal;
	ArrayList<Point> histoTour;
	int itPrincipal, itTour;
	boolean joueur; //true = j1, false = j2
	
	Historique() {
		histoPrincipal = new ArrayList<Terrain>();
		histoTour = new ArrayList<Point>();
		itPrincipal = 0;
	}
	
	void ajouterTour(Terrain t) {
		if(itPrincipal < histoPrincipal.size()-1) {
			for(int i = itPrincipal; i < histoPrincipal.size(); i++) {
				histoPrincipal.remove(i);
			}
		}
		histoPrincipal.add(t);
		itPrincipal = histoPrincipal.size()-1;
	}
	
	Terrain annuler() {
		if(histoPrincipal.isEmpty())
			return null;
		else {
			itPrincipal--;
			return histoPrincipal.get(itPrincipal+1);
		}
	}
	
	void refaire() {
		
	}
}
