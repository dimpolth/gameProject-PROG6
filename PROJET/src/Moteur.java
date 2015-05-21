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
	
	ArrayList<Point> deplacementPossible (Point p, ArrayList<Point> listePredecesseurs){
		ArrayList<Point> l = t.tableau[p.x][p.y].getSucc();
		ArrayList<Point> listeSucPossibles = new ArrayList<Point>();
		
		Iterator<Point> it = l.iterator();

		while(it.hasNext()){
			Point temp = (Point) it.next().clone();
			if(t.tableau[temp.x][temp.y].getOccupation() == Case.Etat.vide && (!listePredecesseurs.contains(temp)))
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
	
	ArrayList<Point> listePionsJouables(Case.Etat joueur){
		ArrayList<Point> listePions = new ArrayList<Point>();
		
		for(int ligne = 0; ligne < 5; ligne++)
			for(int colonne = 0; colonne < 9; colonne++)
				if(this.t.tableau[ligne][colonne].getOccupation() == joueur)
					if(this.deplacementPossible((Point) new Point(ligne,colonne).clone(),new ArrayList<Point>()).size() > 0)
						listePions.add((Point) new Point(ligne,colonne).clone());
		
		return listePions;
	}
	
	boolean partieTerminee(){
		int nbPionsJoueur1 = 0, nbPionsJoueur2 = 0;
		
		for(int ligne = 0; ligne < 5; ligne++)
			for(int colonne = 0; colonne < 9; colonne++){
				if(this.t.tableau[ligne][colonne].getOccupation() == Case.Etat.joueur1)
					nbPionsJoueur1++;
				else if(this.t.tableau[ligne][colonne].getOccupation() == Case.Etat.joueur2)
					nbPionsJoueur2++;
			}
		
		if(nbPionsJoueur1 == 0 || nbPionsJoueur2 == 0){
			return true;
		}
		
		return false;
	}
}
