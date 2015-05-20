import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;


public class Moteur {
	
	Terrain t;
	Historique h;
	
	Moteur() {
		t = new Terrain();
		h = new Historique();
	}
	
	Moteur(Terrain t){
		this.t = t;
		h = new Historique();
	}
	
	ArrayList<Point> deplacementPossible (Point p){
		ArrayList<Point> l = t.tableau[p.x][p.y].getSucc();
		ArrayList<Point> listeSucPossibles = new ArrayList<Point>();
		
		Iterator<Point> it = l.iterator();
		
		
		while(it.hasNext()){
			Point temp = (Point) it.next().clone();
			if(t.tableau[temp.x][temp.y].getOccupation() == Case.Etat.vide)
				listeSucPossibles.add(temp);
		}
		
		/*
		for(int i = 0; i < l.size(); i++) {
			Point temp = l.get(i);
			if(t.tableau[temp.x][temp.y].getOccupation() != Case.Etat.vide)
				l.remove(i);
		}
		*/
		
		return listeSucPossibles;
	}
}
