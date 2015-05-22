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

	Moteur(Terrain t) {
		this.t = t;
		h = new Historique();
	} 
  
	ArrayList<Point> deplacementPossible(Point p, ArrayList<Point> listePredecesseurs) {
		ArrayList<Point> l = t.tableau[p.x][p.y].getSucc();
		ArrayList<Point> listeSucPossibles = new ArrayList<Point>();

		Iterator<Point> it = l.iterator();

		Point pointPrec = new Point();

		while (it.hasNext()) {
			Point temp = (Point) it.next().clone();
			if (t.tableau[temp.x][temp.y].getOccupation() == Case.Etat.vide && (!listePredecesseurs.contains(temp))) {
				if (listePredecesseurs.size() == 0)
					listeSucPossibles.add(temp);
				else {
					pointPrec = listePredecesseurs.get(listePredecesseurs.size());
					Terrain.Direction d1 = t.recupereDirection(pointPrec,p);
					Terrain.Direction d2 = t.recupereDirection(p,temp);
					if (d1==d2) {
						listeSucPossibles.add(temp);
					}
				}

			}
		}

		return listeSucPossibles;
	}

	boolean memeDirection(Point p1, Point p2, Point p3) {
		Terrain.Direction d1 = t.recupereDirection(p1, p2);
		Terrain.Direction d2 = t.recupereDirection(p2, p3);
		boolean b = false;
		switch (d1) {
		case haut:
			if ((d1 == d2) || (d1 == Terrain.Direction.bas))
				b = true;
			break;
		case bas:
			if ((d1 == d2) || (d1 == Terrain.Direction.haut))
				b = true;
			break;
		case gauche:
			if ((d1 == d2) || (d1 == Terrain.Direction.droite))
				b = true;
			break;
		case droite:
			if ((d1 == d2) || (d1 == Terrain.Direction.gauche))
				b = true;
			break;
		case hautGauche:
			if ((d1 == d2) || (d1 == Terrain.Direction.basDroite))
				b = true;
			break;
		case hautDroite:
			if ((d1 == d2) || (d1 == Terrain.Direction.basGauche))
				b = true;
			break;
		case basGauche:
			if ((d1 == d2) || (d1 == Terrain.Direction.hautDroite))
				b = true;
			break;
		case basDroite:
			if ((d1 == d2) || (d1 == Terrain.Direction.hautGauche))
				b = true;
			break;
		}
		return b;

	}

	ArrayList<Point> listePionsJouables(Case.Etat joueur) {
		ArrayList<Point> listePions = new ArrayList<Point>();

		for (int ligne = 0; ligne < 5; ligne++)
			for (int colonne = 0; colonne < 9; colonne++)
				if (this.t.tableau[ligne][colonne].getOccupation() == joueur)
					if (this.deplacementPossible((Point) new Point(ligne, colonne).clone(), new ArrayList<Point>()).size() > 0)
						listePions.add((Point) new Point(ligne, colonne).clone());

		return listePions;
	}

	boolean partieTerminee() {
		int nbPionsJoueur1 = 0, nbPionsJoueur2 = 0;

		for (int ligne = 0; ligne < 5; ligne++)
			for (int colonne = 0; colonne < 9; colonne++) {
				if (this.t.tableau[ligne][colonne].getOccupation() == Case.Etat.joueur1)
					nbPionsJoueur1++;
				else if (this.t.tableau[ligne][colonne].getOccupation() == Case.Etat.joueur2)
					nbPionsJoueur2++;
			}

		if (nbPionsJoueur1 == 0 || nbPionsJoueur2 == 0) {
			return true;
		}

		return false;
	}
}
