import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;

public class Moteur {

	public enum EtatTour {selectionPion, selectionDestination, attenteChoix;}

	public Communication com;

	Terrain t;
	Historique h;
	EtatTour e;
	Point pDepart, pArrive;
	Joueur joueurCourant;
	Joueur j1, j2;
	Echange ech;
	Point aspi, perc;

	Moteur() {
		t = new Terrain();
		h = new Historique();
		e = EtatTour.selectionPion;
		j1 = new Joueur(Case.Etat.joueur1, Joueur.typeJoueur.humain, "joueur 1");
		j2 = new Joueur(Case.Etat.joueur2, Joueur.typeJoueur.ordinateur, "ordinateur");
		joueurCourant = j1;
		ech = new Echange();
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

					pointPrec = listePredecesseurs.get(listePredecesseurs.size() - 1);
					Terrain.Direction dirPrec = t.recupereDirection(pointPrec, p);
					Terrain.Direction dirSuiv = t.recupereDirection(p, temp);
					if (dirPrec != dirSuiv) {
						listeSolution.add(temp);
					}
				}
			}
		}

		return listeSolution;
	}
	
	//revoit une liste de poitn d'arrive permetant une  prise 
	ArrayList<Point> prisePossible(Point p, ArrayList<Point> listePredecesseurs){ 
		ArrayList<Point> listePrise = new ArrayList<Point>();
		
		ArrayList<Point> listeSuc = t.tableau[p.x][p.y].getSucc();
		ArrayList<Point> listeMouvement = deplacementPossible(p, listeSuc);
		Iterator<Point> it = listeMouvement.iterator();
		
		while (it.hasNext()) {
			Point temp = (Point) it.next().clone();
			Terrain.Direction d= t.recupereDirection(p,temp);
			if (t.estUnePriseAspiration(p, d)||t.estUnePrisePercussion(p, d) )
				listePrise.add(temp);	
		}
		
		return listePrise;
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

	ArrayList<Point> listePionsJouables(Joueur j) {
		// ArrayList<Point> listePions = new ArrayList<Point>();
		ArrayList<Point> listePions = t.couplibre(j.getJoueurID());
		if (listePions.isEmpty()) {

			for (int ligne = 0; ligne < Terrain.LIGNES; ligne++)
				for (int colonne = 0; colonne < Terrain.COLONNES; colonne++)
					if (this.t.tableau[ligne][colonne].getOccupation() == j.getJoueurID())
						if (this.deplacementPossible((Point) new Point(ligne, colonne).clone(), new ArrayList<Point>()).size() > 0)
							listePions.add((Point) new Point(ligne, colonne).clone());
		}
		return listePions;
	}

	boolean partieTerminee() {
		if (j1.scoreNul() || j2.scoreNul())
			return true;
		else
			return false;
	}

	boolean selectionPion(Point p) {
		if ((t.getCase(p.x, p.y).getOccupation() != joueurCourant.getJoueurID()) || (e != EtatTour.selectionPion))
			return false;
		else {
			ArrayList<Point> l = listePionsJouables(joueurCourant);
			if (l.contains(p)) {
				pDepart = p;
				e = EtatTour.selectionDestination;
				return true;
			} else
				return false;
		}
	}

	boolean selectionDestination(Point p) {
		ArrayList<Point> l = deplacementPossible(pDepart, h.histoTour);
		if (l.contains(p)) {
			pArrive = p;
			if (t.deplacement(pDepart, pArrive, joueurCourant, h.histoTour) == 0) {
				Point[] tabPts = {pDepart, pArrive};
				ech.vider();
				ech.ajouter("deplacement", tabPts);
				prise();
				return true;
			} else
				return false;
		} else
			return false;
	}

	void prise() {
		Terrain.Direction d = t.recupereDirection(pDepart, pArrive);
		ArrayList<Point> l = new ArrayList<Point>();
		if (t.estUnePriseAspiration(pDepart, d) && t.estUnePrisePercussion(pDepart, d)) {
			Terrain.ChoixPrise choix;
			if ((joueurCourant.getJoueurID() == Case.Etat.joueur1 && j1.isJoueurHumain()) || (joueurCourant.getJoueurID() == Case.Etat.joueur2 && j2.isJoueurHumain())) {
				System.out.println("Choix");
				Point offA = t.offsetAspiration(d, pDepart);
				aspi = new Point(offA.x + pDepart.x, offA.y + pDepart.y);
				Point offP = t.offsetPercussion(d, pArrive);
				perc = new Point(offP.x + pArrive.x, offP.y + pArrive.y);
				ech.ajouter("aspiration", aspi);
				ech.ajouter("percussion", perc);
				com.envoyer(ech);
				e = EtatTour.attenteChoix;
			} else {
				choix = IntelligenceArtificielle.choixPriseIAFacile();
				t.manger(joueurCourant, d, pDepart, pArrive, l, choix);
				Joueur[] tabJoueur = {j1, j2};
				ech.ajouter("pionsManges", l);
				ech.ajouter("joueurs", tabJoueur);
				com.envoyer(ech);
			//if (prisePossible(p, h.histoTour))
					
			
			}
		} else if (t.estUnePriseAspiration(pDepart, d) && !t.estUnePrisePercussion(pDepart, d)) {
			System.out.println("aspi");
			t.manger(joueurCourant, d, pDepart, pArrive, l, Terrain.ChoixPrise.parAspiration);
			Joueur[] tabJoueur = {j1, j2};
			ech.ajouter("pionsManges", l);
			ech.ajouter("joueurs", tabJoueur);
			com.envoyer(ech);
			
		} else if (!t.estUnePriseAspiration(pDepart, d) && t.estUnePrisePercussion(pDepart, d)) {
			System.out.println("percu");
			t.manger(joueurCourant, d, pDepart, pArrive, l, Terrain.ChoixPrise.parPercussion);
			Joueur[] tabJoueur = {j1, j2};
			ech.ajouter("pionsManges", l);
			ech.ajouter("joueurs", tabJoueur);
			com.envoyer(ech);
		}

	}	

	void finTour() {
		if (joueurCourant.getJoueurID() == Case.Etat.joueur1)
			joueurCourant = j2;
		else
			joueurCourant = j1;
		h.effacerHistoTour();
		h.ajouterTour(t);
		e = EtatTour.selectionPion;
	}

	void action(Echange ech) {

		System.out.println(e);
		for (String dataType : ech.getAll()) {
			Object dataValue = ech.get(dataType);
			
			
			switch(dataType){
				case "point" : 
					if (e == EtatTour.selectionPion ){selectionPion((Point)dataValue);}
					if (e == EtatTour.selectionDestination){selectionDestination((Point)dataValue);}
					if (e == EtatTour.attenteChoix){
						System.out.println((Point)dataValue);
						System.out.println("perc : "+perc);
						Terrain.Direction d = t.recupereDirection(pDepart, pArrive);
						ArrayList<Point> l = new ArrayList<Point>();
						int nbPionsManges = 0;
						if(perc.equals((Point)dataValue))
							nbPionsManges = t.manger(joueurCourant, d, pDepart, pArrive, l, Terrain.ChoixPrise.parPercussion);
						else if(aspi.equals((Point)dataValue))
							nbPionsManges = t.manger(joueurCourant, d, pDepart, pArrive, l, Terrain.ChoixPrise.parAspiration);
						joueurCourant.setScore(nbPionsManges);
						Joueur[] tabJoueur = {j1, j2};
						ech.vider();
						ech.ajouter("pionsManges",l);
						ech.ajouter("joueurs", tabJoueur);
					com.envoyer(ech);
					}
					break;
				case "terrain":
					ech.vider();
					ech.ajouter("terrain", t.getTableau());
					com.envoyer(ech);
					break;
					
				case "annuler" :
					break;
			}
		}
	}
}
