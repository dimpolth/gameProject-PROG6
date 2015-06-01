package moteur;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;

import ia.*;
import ihm.*;
import modele.*;
import reseau.*;

public class Moteur {

	public static void main(String[] args) {

		Moteur m = new Moteur();
		IHM i = new IHM();
		i.setVisible(true);

		i.com = new Communication(i, m, Communication.IHM);
		m.com = new Communication(i, m, Communication.MOTEUR);

		i.lancer();

	}

	public enum EtatTour {
		selectionPion, selectionDestination, attenteChoix;
	}

	public Communication com;

	public Terrain t;
	Historique h;
	EtatTour e;
	Point pDepart, pArrive;
	Joueur joueurCourant;
	Joueur j1, j2;
	Echange ech;
	Point aspi, perc;

	public Moteur() {
		t = new Terrain();
		h = new Historique();
		e = EtatTour.selectionPion;
		j1 = new Joueur(Case.Etat.joueur1, Joueur.typeJoueur.humain, "joueur 1");
		j2 = new Joueur(Case.Etat.joueur2, Joueur.typeJoueur.humain, "joueur 2");
		joueurCourant = j1;
		ech = new Echange();
		h.ajouterTour(t);
	}

	Moteur(Terrain t) {
		this.t = t;
		h = new Historique();
	}

	public ArrayList<Point> deplacementPossible(Point p, ArrayList<Point> listePredecesseurs, Terrain copieTerrainEventuelle) {
		ArrayList<Point> listeSuc = t.tableau[p.x][p.y].getSucc();
		ArrayList<Point> listeSolution = new ArrayList<Point>();

		Iterator<Point> it = listeSuc.iterator();

		Point pointPrec = new Point();

		Terrain terr = t;
		
		if(copieTerrainEventuelle != null)
			terr = copieTerrainEventuelle;
		
		while (it.hasNext()) {
			Point temp = (Point) it.next().clone();

			if (terr.tableau[temp.x][temp.y].getOccupation() == Case.Etat.vide && (!listePredecesseurs.contains(temp))) {
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

	// Renvoie une liste de points d'arrive permettant une prise
	ArrayList<Point> prisePossible(Point p, ArrayList<Point> listePredecesseurs) {
		ArrayList<Point> listePrise = new ArrayList<Point>();
		ArrayList<Point> listeSuc = t.tableau[p.x][p.y].getSucc();
		ArrayList<Point> listeMouvement = deplacementPossible(p, listePredecesseurs, null);
		Iterator<Point> it = listeMouvement.iterator();

		while (it.hasNext()) {
			Point temp = (Point) it.next().clone();
			Terrain.Direction d = t.recupereDirection(p, temp);
			if (t.estUnePriseAspiration(p, d) || t.estUnePrisePercussion(p, d))
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

	public ArrayList<Point> listePionsJouables(Joueur j, Terrain copieTerrainEventuelle) {
		
		Terrain terr = t;
		
		if(copieTerrainEventuelle != null) // Utile à l'IA pour travailler sur une copie de terrain modifiée
			terr = copieTerrainEventuelle;
		
		// ArrayList<Point> listePions = new ArrayList<Point>();
		ArrayList<Point> listePions = t.couplibre(j.getJoueurID());
		if (listePions.isEmpty()) {

			for (int ligne = 0; ligne < Terrain.LIGNES; ligne++)
				for (int colonne = 0; colonne < Terrain.COLONNES; colonne++)
					if (terr.tableau[ligne][colonne].getOccupation() == j.getJoueurID())
						if (this.deplacementPossible((Point) new Point(ligne, colonne).clone(), new ArrayList<Point>(),null).size() > 0)
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
			ArrayList<Point> l = listePionsJouables(joueurCourant, null);
			if (l.contains(p)) {
				pDepart = p;
				message("bandeauInf", "Choisir la destination");
				e = EtatTour.selectionDestination;
				return true;
			} else
				return false;
		}
	}

	boolean selectionDestination(Point p) {
		ArrayList<Point> l = deplacementPossible(pDepart, h.histoTour, null);
		if (l.contains(p)) {
			pArrive = p;
			Terrain.Direction d = t.recupereDirection(pDepart, pArrive);
			boolean priseAspi = t.estUnePriseAspiration(pDepart, d);
			boolean prisePercu = t.estUnePrisePercussion(pDepart, d);
			if (t.deplacement(pDepart, pArrive, joueurCourant, h.histoTour) == 0) {
				Point[] tabPts = { pDepart, pArrive };
				ech.vider();
				ech.ajouter("deplacement", tabPts);
				prise(priseAspi, prisePercu);
				return true;
			} else
				return false;
		} else
			return false;
	}

	void prise(boolean priseAspi, boolean prisePercu) {
		Terrain.Direction d = t.recupereDirection(pDepart, pArrive);
		ArrayList<Point> l = new ArrayList<Point>();
		h.ajouterCoup(pDepart);
		if (priseAspi && prisePercu) {
			Terrain.ChoixPrise choix;
			if ((joueurCourant.getJoueurID() == Case.Etat.joueur1 && j1.isJoueurHumain()) || (joueurCourant.getJoueurID() == Case.Etat.joueur2 && j2.isJoueurHumain())) {
				// System.out.println("Choix");
				Point offA = t.offsetAspiration(d, pDepart);
				aspi = new Point(offA.x + pDepart.x, offA.y + pDepart.y);
				Point offP = t.offsetPercussion(d, pArrive);
				perc = new Point(offP.x + pArrive.x, offP.y + pArrive.y);
				Point[] tabPts = { aspi, perc };
				ech.ajouter("choixPrise", tabPts);
				com.envoyer(ech);
				e = EtatTour.attenteChoix;
			} else {
				choix = IntelligenceArtificielle.choixPriseIAFacile();
				majScore(t.manger(joueurCourant, d, pDepart, pArrive, choix).size());
				Joueur[] tabJoueur = { j1, j2 };
				ech.ajouter("pionsManges", l);
				ech.ajouter("joueurs", tabJoueur);
				com.envoyer(ech);
			}
		} else if (priseAspi && !prisePercu) {
			// System.out.println("aspi");
			majScore(t.manger(joueurCourant, d, pDepart, pArrive, Terrain.ChoixPrise.parAspiration).size());
			Joueur[] tabJoueur = { j1, j2 };
			ech.ajouter("pionsManges", l);
			ech.ajouter("joueurs", tabJoueur);
			com.envoyer(ech);
			testFinTour();

		} else if (!priseAspi && prisePercu) {
			// System.out.println("percu");
			l = t.manger(joueurCourant, d, pDepart, pArrive, Terrain.ChoixPrise.parPercussion);
			majScore(l.size());
			Joueur[] tabJoueur = { j1, j2 };
			ech.ajouter("pionsManges", l);
			ech.ajouter("joueurs", tabJoueur);
			com.envoyer(ech);
			testFinTour();
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
		message("bandeauSup", joueurCourant.getNom());
	}

	void testFinTour() {
		pDepart = pArrive;
		if (prisePossible(pDepart, h.histoTour).isEmpty()) {
			finTour();
		} else {
			e = EtatTour.selectionDestination;
		}
	}

	void majScore(int nbPionsManges) {
		Joueur.recupereJoueurOpposant(joueurCourant, j1, j2, false).setScore(nbPionsManges);
	}

	void message(String destination, String message) {
		ech.vider();
		ech.ajouter(destination, message);
		com.envoyer(ech);
	}
	
	void calculerScore() {
		int scoreJ1 = 0;
		int scoreJ2 = 0;
		for(int i = 0; i < t.LIGNES; i++) {
			for(int j = 0; j < t.COLONNES; j++) {
				if(t.tableau[i][j].getOccupation() == Case.Etat.joueur1)
					scoreJ1++;
				if(t.tableau[i][j].getOccupation() == Case.Etat.joueur2)
					scoreJ2++;
			}
		}
		j1.chargerScore(scoreJ1);
		j2.chargerScore(scoreJ2);
	}

	public void action(Echange echange) {

		for (String dataType : echange.getAll()) {
			Object dataValue = echange.get(dataType);

			switch (dataType) {
			case "point":
				if (e == EtatTour.selectionPion) {
					System.out.println("e : " + e);
					selectionPion((Point) dataValue);
				} else if (e == EtatTour.selectionDestination) {
					System.out.println("e : " + e);
					selectionDestination((Point) dataValue);
				} else if (e == EtatTour.attenteChoix) {
					System.out.println("e : " + e);
					Terrain.Direction d = t.recupereDirection(pDepart, pArrive);
					ArrayList<Point> l = new ArrayList<Point>();
					int nbPionsManges = 0;
					if (perc.equals((Point) dataValue))
						nbPionsManges = t.manger(joueurCourant, d, pDepart, pArrive, Terrain.ChoixPrise.parPercussion).size();
					else if (aspi.equals((Point) dataValue))
						nbPionsManges = t.manger(joueurCourant, d, pDepart, pArrive,  Terrain.ChoixPrise.parAspiration).size();
					majScore(nbPionsManges);
					Joueur[] tabJoueur = { j1, j2 };
					ech.vider();
					ech.ajouter("pionsManges", l);
					ech.ajouter("joueurs", tabJoueur);
					com.envoyer(ech);
					testFinTour();
				}
				break;
			case "terrain":
				ech.vider();
				ech.ajouter("terrain", t.getTableau());
				com.envoyer(ech);
				break;

			case "annuler":
				ech.vider();
				Case[][] annulation = h.annuler().getTableau();
				if(annulation != null) {
					ech.ajouter("terrain", annulation);
					com.envoyer(ech);
				}
				break;

			case "joueurs":
				Joueur[] tabJoueurIntit = (Joueur[]) dataValue;
				// recuperer les noms et les type des joueurs.
				j1.setNom(tabJoueurIntit[0].getNom());
				j2.setNom(tabJoueurIntit[1].getNom());
				ech.vider();
				Joueur[] tabJoueur = { j1, j2 };
				ech.ajouter("joueurs", tabJoueur);
				com.envoyer(ech);
				break;
			}
		}
	}
}
