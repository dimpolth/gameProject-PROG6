import java.awt.Point;
import java.util.ArrayList;


public class Moteur {
	
	Terrain t;
	Historique h;
	
	Moteur() {
		t = new Terrain();
		h = new Historique();
	}
	
	ArrayList<Point> deplacementPossible (Point p){
		ArrayList<Point> l = t.tableau[p.x][p.y].getSucc();
		for(int i = 0; i < l.size(); i++) {
			Point temp = l.get(i);
			if(t.tableau[temp.x][temp.y].getOccupation() != Case.Etat.vide)
				l.remove(i);
		}
		return l;
	}
}
