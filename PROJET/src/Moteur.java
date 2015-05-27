import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;

public class Moteur {


	public enum EtatTour {
		selectionPion, selectionDestination, choixPrise;
	}
	
	public enum Joueur {
		humain, IA;
	}

	public Communication com;

	Terrain t;
	Historique h;
	EtatTour e;
	Point pDepart, pArrive;
	Case.Etat joueur;
	Joueur j1, j2;
	int scoreJ1, scoreJ2;
	Echange ech;

	Moteur() {
		t = new Terrain();
		h = new Historique();
		e = EtatTour.selectionPion;
		j1 = Joueur.humain;
		j2 = Joueur.IA;
		scoreJ1 = 0;
		scoreJ2 = 0;
		joueur = Case.Etat.joueur1;
	}

	Moteur(Terrain t) {
		this.t = t;
		h = new Historique();
	}

	ArrayList<Point> deplacementPossible(Point p, ArrayList<Point> listePredecesseurs) {
		ArrayList<Point> listeSuc = t.tableau[p.x][p.y].getSucc();
		ArrayList<Point> listeSolution = new ArrayList<Point>();

		Iterator<Point> it = listeSuc.iterator();

		Point pointPrec = new Point();

		while (it.hasNext()) {
			Point temp = (Point) it.next().clone();
			if (t.tableau[temp.x][temp.y].getOccupation() == Case.Etat.vide && (!listePredecesseurs.contains(temp))) {
				if (listePredecesseurs.size() == 0)
					listeSolution.add(temp);
				else {

					pointPrec = listePredecesseurs.get(listePredecesseurs.size()-1);
					Terrain.Direction dirPrec = t.recupereDirection(pointPrec,p);
					Terrain.Direction dirSuiv = t.recupereDirection(p,temp);
					if (dirPrec != dirSuiv) {
						listeSolution.add(temp);
					}
				}

			}
		}

		return listeSolution;
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
		// ArrayList<Point> listePions = new ArrayList<Point>();
		ArrayList<Point> listePions = t.couplibre(joueur);
		if (listePions.isEmpty()) {

<<<<<<< HEAD
			for (int ligne = 0; ligne < 5; ligne++)
				for (int colonne = 0; colonne < 9; colonne++)
					if (this.t.tableau[ligne][colonne].getOccupation() == joueur)
						if (this.deplacementPossible((Point) new Point(ligne, colonne).clone(), new ArrayList<Point>()).size() > 0)
							listePions.add((Point) new Point(ligne, colonne).clone());
		}
=======
		for (int ligne = 0; ligne < Terrain.LIGNES; ligne++)
			for (int colonne = 0; colonne < Terrain.COLONNES; colonne++)
				if (this.t.tableau[ligne][colonne].getOccupation() == joueur)
					if (this.deplacementPossible((Point) new Point(ligne, colonne).clone(), new ArrayList<Point>()).size() > 0)
						listePions.add((Point) new Point(ligne, colonne).clone());

>>>>>>> branch 'master' of https://github.com/dimpolth/gameProject-PROG6.git
		return listePions;
	}

	boolean partieTerminee() {
		if(scoreJ1 == 22 || scoreJ2 == 22)
			return true;
		else
			return false;
	}

<<<<<<< HEAD
	boolean selectionPion(Point p) {
		if ((t.getCase(p.x, p.y).getOccupation() != joueur) || (e != EtatTour.selectionPion))
			return false;
		else {
			ArrayList<Point> l = listePionsJouables(joueur);
			if (l.contains(p)) {
				pDepart = p;
				e = EtatTour.selectionDestination;
				return true;
			} else
				return false;
		}
	}
	
	boolean selectionDestination(Point p) {
		ArrayList<Point> l = deplacementPossible(pDepart,h.histoTour );
		if(l.contains(p)) {
			pArrive = p;
			if(t.deplacement(pDepart, pArrive, joueur, l) == 0) {
				prise();
				return true;
			} else
				return false;
		}
		else
			return false;
	}
	
	void prise() {
		Terrain.Direction d = t.recupereDirection(pDepart, pArrive);
		ArrayList<Point> l = new ArrayList<Point>();
		if(t.estUnePriseAspiration(pDepart, d) && t.estUnePrisePercussion(pDepart, d)) {
			Terrain.ChoixPrise choix;
			if((joueur == Case.Etat.joueur1 && j1 == Joueur.humain) || (joueur == Case.Etat.joueur2 && j2 == Joueur.humain)) {
				//choix = ech.getChoix();
			} else {
				choix = IntelligenceArtificielle.choixPriseIAFacile();
			}
			//t.manger(joueur, d, pDepart, pArrive, l, choix);
		} else if(t.estUnePriseAspiration(pDepart, d) && !t.estUnePrisePercussion(pDepart, d)) {
			t.manger(joueur, d, pDepart, pArrive, l, Terrain.ChoixPrise.parAspiration);
		} else if(!t.estUnePriseAspiration(pDepart, d) && t.estUnePrisePercussion(pDepart, d)) {
			t.manger(joueur, d, pDepart, pArrive, l, Terrain.ChoixPrise.parPercussion);
		}
		
	}
	
	void calculerScore() {
		for (int ligne = 0; ligne < 5; ligne++)
			for (int colonne = 0; colonne < 9; colonne++) {
=======
		for (int ligne = 0; ligne < Terrain.LIGNES; ligne++)
			for (int colonne = 0; colonne < Terrain.COLONNES; colonne++) {
>>>>>>> branch 'master' of https://github.com/dimpolth/gameProject-PROG6.git
				if (this.t.tableau[ligne][colonne].getOccupation() == Case.Etat.joueur1)
					scoreJ1++;
				else if (this.t.tableau[ligne][colonne].getOccupation() == Case.Etat.joueur2)
					scoreJ2++;
			}
	}
	
	void finTour() {
		if(joueur == Case.Etat.joueur1)
			joueur = Case.Etat.joueur2;
		else
			joueur = Case.Etat.joueur1;
		h.effacerHistoTour();
		e = EtatTour.selectionPion;
	}
	

	void action(Echange e){
		
		if(e.getAnnuler()){
			//this.annuler();
		}
			
		
	}
		

}
