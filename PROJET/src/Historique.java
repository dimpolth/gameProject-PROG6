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
		Terrain c = new Terrain();
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 9; j++) {
				c.tableau[i][j] = t.tableau[i][j];
			}
		}
		histoPrincipal.add(c);
		itPrincipal = histoPrincipal.size()-1;
		System.out.println("itprincipal"+itPrincipal);
		
	}
	
	Terrain annuler() {
		if(histoPrincipal.isEmpty())
			return null;
		else {
			return histoPrincipal.get(itPrincipal--);
		}
	}
	
	Terrain refaire() {
		if(itPrincipal == histoPrincipal.size()-1)
			return null;
		else {
			return histoPrincipal.get(itPrincipal++);
		}
	}
	
	void afficher() {
		System.out.println("----------------");
		for(int it = 0; it < histoPrincipal.size(); it++) {
			Terrain tmp = histoPrincipal.get(it);
			tmp.dessineTableauAvecIntersections();
		}
		System.out.println("----------------");
	}
}
