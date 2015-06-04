package moteur;

import java.awt.Point;
import java.io.*;
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
		i.com = new Communication(i, m, Communication.IHM);
		m.com = new Communication(i, m, Communication.MOTEUR);
		// m.init();
		i.lancer();
	}

	public enum EtatTour {
		selectionPion, selectionDestination, attenteChoix, jeuxIa, partieFinie;
	}

	public Communication com;
	public Terrain t;
	Historique h;
	EtatTour e;
	Point pDepart, pArrive;
	Joueur joueurCourant;
	public Joueur j1, j2;
	Echange ech;
	Point aspi, perc;
	Boolean tourEnCours;
	ArrayList<Point> listePointDebut;
	Coup jeuIa;

	public Moteur() {
	}

	/**
	 * Constructeur utilisé dans le cas d'un chargement de partie.
	 * 
	 * @param t
	 * Le terrain à charger pour reprendre la partie. 
	 */
	Moteur(Terrain t) {
		this.t = t;
		h = new Historique();
		ech = new Echange();
	}

	/**
	 * Initialise le moteur.
	 * Cette methode n'est pas dans le constructeur car si une nouvelle partie est lancée par l'utilisateur, le moteur ne peut pas se construir lui-même.
	 */
	public void init() {
		t = new Terrain();
		
		h = new Historique();
		h.ajouterTour(t);
		ech = new Echange();
		listePointDebut = new ArrayList<Point>();
		j1 = new Joueur(Case.Etat.joueur1, Joueur.typeJoueur.humain, "Joueur 1");
		// j2 = new Joueur(Case.Etat.joueur2, Joueur.typeJoueur.humain,
		// "Joueur 2");
		// j1 = new Joueur(Case.Etat.joueur1, Joueur.typeJoueur.ordinateur,
		// IntelligenceArtificielle.difficulteIA.facile, j2, this);
		j2 = new Joueur(Case.Etat.joueur2, Joueur.typeJoueur.ordinateur,
				IntelligenceArtificielle.difficulteIA.normal, j1, this);
		joueurCourant = j1;
		if (joueurCourant.isJoueurHumain()) {
			e = EtatTour.selectionPion;
		} else {
			e = EtatTour.jeuxIa;
			jouerIa();
		}
		message("bandeauSup", joueurCourant.getNom());
		message("bandeauInf", "Selection du pion");
	}
	/**
	 * Détermine quels sont les déplacements possibles
	 * @param p
	 * Point à partir du quel on veut déterminer les déplacements possibles.
	 * @param listePredecesseurs
	 * ArrayList de Points. Liste des points par lesquels est passé le pion durant le tour.
	 * @param copieTerrainEventuelle
	 * Terrain. Utilisé par l'IA pour simuler des coups.
	 * @return
	 * ArrayList de Points. Liste des emplacements vers lequel le pion courant peut se déplacer.
	 */
	public ArrayList<Point> deplacementPossible(Point p, ArrayList<Point> listePredecesseurs, Terrain copieTerrainEventuelle) {
		ArrayList<Point> listeSuc = t.tableau[p.x][p.y].getSucc();
		ArrayList<Point> listeSolution = new ArrayList<Point>();
		Iterator<Point> it = listeSuc.iterator();
		Point pointPrec = new Point();
		Terrain terr = t;
		if (copieTerrainEventuelle != null)
			terr = copieTerrainEventuelle;
		while (it.hasNext()) {
			Point temp = (Point) it.next().clone();
			if (terr.tableau[temp.x][temp.y].getOccupation() == Case.Etat.vide
					&& (!listePredecesseurs.contains(temp))) {
				if (listePredecesseurs.size() == 0)
					listeSolution.add(temp);
				else {
					pointPrec = listePredecesseurs.get(listePredecesseurs
							.size() - 1);
					Terrain.Direction dirPrec = t.recupereDirection(pointPrec,
							p);
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
		ArrayList<Point> listeMouvement = deplacementPossible(p,
				listePredecesseurs, null);
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

	public ArrayList<Point> listePionsJouables(Joueur j,
			Terrain copieTerrainEventuelle) {
		Terrain terr = t;
		if (copieTerrainEventuelle != null) // Utile à l'IA pour travailler sur
		// une copie de terrain modifiée
			terr = copieTerrainEventuelle;

		ArrayList<Point> listePions = t.couplibre(j.getJoueurID());
		if (listePions.isEmpty()) {
			for (int ligne = 0; ligne < Terrain.LIGNES; ligne++)
				for (int colonne = 0; colonne < Terrain.COLONNES; colonne++)
					if (terr.tableau[ligne][colonne].getOccupation() == j
							.getJoueurID())
						if (this.deplacementPossible(
								(Point) new Point(ligne, colonne).clone(),
								new ArrayList<Point>(), null).size() > 0)
							listePions.add((Point) new Point(ligne, colonne)
									.clone());
		}
		return listePions;
	}

	boolean partieTerminee(boolean aucunDeplacement) {
		ech.vider();
		if (joueurCourant.scoreNul() || aucunDeplacement) {
			ech.ajouter("bandeauSup", "<html><font color=#FF0000>" + joueurCourant.recupereJoueurOpposant(joueurCourant, j1, j2, false).getNom() + "</font></html>");
			ech.ajouter("bandeauInf", "<html><font color=#FF0000>à remporté la partie</font></html>");
			com.envoyer(ech);
			return true;
		} else
			return false;
	}

	boolean selectionPion(Point p) {
		tourEnCours = false;
		if (t.getCase(p.x, p.y).getOccupation() != joueurCourant.getJoueurID()) {
			return false;
		} else {
			listePointDebut = listePionsJouables(joueurCourant, null);
			if (listePointDebut.isEmpty())
				partieTerminee(true);
			if (listePointDebut.contains(p)) {
				pDepart = p;
				if (joueurCourant.isJoueurHumain()) {
					ech.vider();
					ech.ajouter("pionSelectionne", pDepart);
					com.envoyer(ech);
				}
				message("bandeauInf", "Choisir la destination");
				e = EtatTour.selectionDestination;
				return true;
			} else {
				return false;
			}
		}
	}

	void selectionDestination(Point p) {
		ArrayList<Point> l = deplacementPossible(pDepart, h.histoTour, null);
		if (listePointDebut.contains(p) && !tourEnCours) {
			pDepart = p;
			if (joueurCourant.isJoueurHumain()) {
				ech.vider();
				ech.ajouter("pionDeselectionne", true);
				ech.ajouter("pionSelectionne", pDepart);
				com.envoyer(ech);
			}
		} else if (l.contains(p)) {
			pArrive = p;
			Terrain.Direction d = t.recupereDirection(pDepart, pArrive);
			boolean priseAspi = t.estUnePriseAspiration(pDepart, d);
			boolean prisePercu = t.estUnePrisePercussion(pDepart, d);
			if (t.deplacement(pDepart, pArrive, joueurCourant, h.histoTour) == 0) {
				Point[] tabPts = { pDepart, pArrive };
				ech.vider();
				gestionCoupGraphique(tabPts, null, null, null, "selectionpoint");
				prise(priseAspi, prisePercu);
				tourEnCours = true;
			}
		}
	}

	void prise(boolean priseAspi, boolean prisePercu) {
		Terrain.Direction d = t.recupereDirection(pDepart, pArrive);
		ArrayList<Point> l = new ArrayList<Point>();
		h.ajouterCoup(pDepart);
		if (priseAspi && prisePercu) {
			Terrain.ChoixPrise choix;
			if (joueurCourant.isJoueurHumain()) {
				//System.out.println("Choix");
				Point offA = t.offsetAspiration(d, pDepart);
				aspi = new Point(offA.x + pDepart.x, offA.y + pDepart.y);
				Point offP = t.offsetPercussion(d, pArrive);
				perc = new Point(offP.x + pArrive.x, offP.y + pArrive.y);
				Point[] tabPts = { aspi, perc };
				gestionCoupGraphique(null, tabPts, null, null, "choixJoueur");
				e = EtatTour.attenteChoix;
			} else {
				choix = jeuIa.getChoixPrise();
				// System.out.println("passage choix de prise IA");
				l = t.manger(joueurCourant, d, pDepart, pArrive, choix);
				majScore(l.size());
				int[] score = { j1.getScore(), j2.getScore() };
				gestionCoupGraphique(null, null, l, score, "PrisechoixIA");
				//t.dessineTableauAvecIntersections();
			}
		} else if (priseAspi && !prisePercu) {
			// System.out.println("aspi");
			l = t.manger(joueurCourant, d, pDepart, pArrive,
					Terrain.ChoixPrise.parAspiration);
			majScore(l.size());
			int[] score = { j1.getScore(), j2.getScore() };
			gestionCoupGraphique(null, null, l, score, "priseAspi");
			//t.dessineTableauAvecIntersections();
			if (joueurCourant.isJoueurHumain())
				testFinTour();
		} else if (!priseAspi && prisePercu) {
			// System.out.println("percu");
			l = t.manger(joueurCourant, d, pDepart, pArrive,
					Terrain.ChoixPrise.parPercussion);
			majScore(l.size());
			int[] score = { j1.getScore(), j2.getScore() };
			gestionCoupGraphique(null, null, l, score, "prisePercu");
			//t.dessineTableauAvecIntersections();
			if (joueurCourant.isJoueurHumain())
				testFinTour();
		} else {
			finTour();
		}
	}

	void finTour() {
		if (joueurCourant.getJoueurID() == Case.Etat.joueur1)
			joueurCourant = j2;
		else
			joueurCourant = j1;
		h.effacerHistoTour();
		h.ajouterTour(t);
		ech.vider();
		ech.ajouter("pionDeselectionne", true);
		ech.ajouter("annuler", true);
		ech.ajouter("refaire", false);
		com.envoyer(ech);
		if (partieTerminee(false)) {
			e = EtatTour.partieFinie;
			//System.out.println("FINI");
		} else {
			// System.out.println("FIN DE TOUR ");
			if (joueurCourant.isJoueurHumain()) {
				e = EtatTour.selectionPion;
			} else {
				e = EtatTour.jeuxIa;
				jouerIa();
			}
		}
		message("bandeauSup", joueurCourant.getNom());
		message("bandeauInf", "Selection du pion");
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
		Joueur.recupereJoueurOpposant(joueurCourant, j1, j2, false).setScore(
				nbPionsManges);
	}

	void message(String destination, String message) {
		ech.vider();
		ech.ajouter(destination, message);
		com.envoyer(ech);
	}

	void gestionCoupGraphique(Point[] deplacement, Point[] choixPrise, ArrayList<Point> pionsManges, int[] score, String s) {
		ech.vider();
		CoupGraphique cg = new CoupGraphique(deplacement, choixPrise, pionsManges, score, calculChemin());
		ech.ajouter("coup", cg);
		com.envoyer(ech);
	}

	// surchage de gestionGraphique pour le cas ou in coup inclue in changement
	// de bandeau

	void gestionCoupGraphique(Point[] deplacement, Point[] choixPrise, ArrayList<Point> pionsManges, int[] score, String chaine1, String chaine2) {
		ech.vider();
		CoupGraphique cg = new CoupGraphique(deplacement, choixPrise, pionsManges, score, chaine1, chaine2, calculChemin());
		ech.ajouter("coup", cg);
		com.envoyer(ech);
	}

	public void calculerScore() {
		int scoreJ1 = 0;
		int scoreJ2 = 0;
		for (int i = 0; i < Terrain.LIGNES; i++) {
			for (int j = 0; j < Terrain.COLONNES; j++) {
				if (t.tableau[i][j].getOccupation() == Case.Etat.joueur1)
					scoreJ1++;
				if (t.tableau[i][j].getOccupation() == Case.Etat.joueur2)
					scoreJ2++;
			}
		}
		j1.chargerScore(scoreJ1);
		j2.chargerScore(scoreJ2);
	}

	ArrayList<Point> calculChemin() {
		ArrayList<Point> chemin = (ArrayList<Point>) h.histoTour.clone();
		if (!chemin.contains(pDepart))
			chemin.add(pDepart);
		if (!chemin.contains(pArrive))
			chemin.add(pArrive);
		return chemin;
	}

	void jouerIa() {
		System.out.println(e);
		
		// System.out.println("DEBUT TOUR IA");
		Thread th = new Thread(){
			public void run(){
				do {
					// System.out.println(joueurCourant.getNom());
					// System.out.println("boucle IA");
					jeuIa = joueurCourant.jouer();
					// System.out.println("depart "+jeuIa.getpDepart()+" arrivé "+jeuIa.getpArrivee());
					selectionPion(jeuIa.getpDepart());
					// System.out.println("point depart moteur :"+pDepart);
					selectionDestination(jeuIa.getpArrivee());
					// t.dessineTableauAvecIntersections();
				} while (joueurCourant.IaContinue());
					// System.out.println(" FIN DU JEU IA");
		
				finTour();
			}
		};
		
		th.start();
;
	}

	void gestionBouton() {
		ech.vider();
		int i = h.getItPrincipal();
		if (joueurCourant.recupereJoueurOpposant(joueurCourant, j1, j2, false).isJoueurHumain()) {
			if (i <= 0) {
				ech.ajouter("annuler", false);
				ech.ajouter("refaire", true);
			} else {
				ech.ajouter("annuler", true);
				if (i == h.histoPrincipal.size() - 1) {
					ech.ajouter("refaire", false);
				}
				else {
					ech.ajouter("refaire", true);
				}
			}
		} else {
			if (i <= 1) {
				ech.ajouter("annuler", false);
				ech.ajouter("refaire", true);
			} else {
				ech.ajouter("annuler", true);
				if (i == h.histoPrincipal.size() - 1) {
					ech.ajouter("refaire", false);
				} else {
					ech.ajouter("refaire", true);
				}
			}
		}
		com.envoyer(ech);

	}

	public void action(Echange echange) {
		for (String dataType : echange.getAll()) {
			Object dataValue = echange.get(dataType);
			// System.out.println(dataType);
			// System.out.println("e : " + e);
			switch (dataType) {
			case "nouvellePartie":
				init();
				break;
			case "point":
				if (e == EtatTour.selectionPion) {
					// System.out.println("e : " + e);
					selectionPion((Point) dataValue);
				} else if (e == EtatTour.selectionDestination) {
					// System.out.println("e : " + e);
					selectionDestination((Point) dataValue);
				} else if (e == EtatTour.attenteChoix) {
					// System.out.println("e : " + e);
					Terrain.Direction d = t.recupereDirection(pDepart, pArrive);
					ArrayList<Point> l = new ArrayList<Point>();
					boolean tperc = perc.equals((Point) dataValue);
					boolean taspi = aspi.equals((Point) dataValue);
					//System.out.println("perc " + tperc + " aspi " + taspi);
					if (tperc || taspi) {
						if (tperc) {
							// System.out.println("choix percu");
							l = t.manger(joueurCourant, d, pDepart, pArrive, Terrain.ChoixPrise.parPercussion);
						} else if (taspi) {
							// System.out.println("choix aspi");
							l = t.manger(joueurCourant, d, pDepart, pArrive, Terrain.ChoixPrise.parAspiration);
						}
						majScore(l.size());
						int[] score = { j1.getScore(), j2.getScore() };
						gestionCoupGraphique(null, null, l, score, "PriseselonchoixJoueur");
						//t.dessineTableauAvecIntersections();
						testFinTour();
					}
				}
				break;
			case "terrain":
				ech.vider();
				ech.ajouter("terrain", t.getTableau());
				com.envoyer(ech);
				break;
			case "annuler":
				if (e != EtatTour.partieFinie) {
					ech.vider();
					Terrain annulation = h.annuler();
					if (annulation != null) {
						joueurCourant = joueurCourant.recupereJoueurOpposant(joueurCourant, j1, j2, false);
						if (!joueurCourant.isJoueurHumain()) {
							annulation = h.annuler();
							if (annulation != null) {
								joueurCourant = joueurCourant.recupereJoueurOpposant(joueurCourant, j1, j2, false);
								t.setTableau(annulation.getTableau());
								ech.ajouter("terrain", annulation.getTableau());

							} 
						} else {
							t.setTableau(annulation.getTableau());
							ech.ajouter("terrain", annulation.getTableau());
						}
					}
					calculerScore();
					int[] tabScore = {j1.getScore(), j2.getScore()};
					ech.ajouter("score", tabScore);
					com.envoyer(ech);
					gestionBouton();
					message("bandeauSup", joueurCourant.getNom());
					message("bandeauInf", "Selection du pion");
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
			case "refaire":
				if (e != EtatTour.partieFinie) {
					ech.vider();
					Case[][] refaire = h.refaire().getTableau();
					if (refaire != null) {
						joueurCourant = joueurCourant.recupereJoueurOpposant(joueurCourant, j1, j2, false);
						if (!joueurCourant.isJoueurHumain()) {
							refaire = h.refaire().getTableau();
							if(refaire != null) {
								joueurCourant = joueurCourant.recupereJoueurOpposant(joueurCourant, j1, j2, false);
								t.setTableau(refaire);
								ech.ajouter("terrain", refaire);
							}
						} else {
							t.setTableau(refaire);
							ech.ajouter("terrain", refaire);
						}
					}
					calculerScore();
					int[] tabScore = {j1.getScore(), j2.getScore()};
					ech.ajouter("score", tabScore);
					com.envoyer(ech);
					gestionBouton();
					message("bandeauSup", joueurCourant.getNom());
					message("bandeauInf", "Selection du pion");
				}
				break;
			case "finTour":
				finTour();
				break;
			case "sauvegarder":
				Sauvegarde s = new Sauvegarde(t, h, j1, j2, joueurCourant);
				ObjectOutputStream oos = null;
				try {
					final FileOutputStream fichier = new FileOutputStream(
							(File) dataValue);
					oos = new ObjectOutputStream(fichier);
					oos.writeObject(s);
				} catch (final java.io.IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (oos != null) {
							oos.flush();
							oos.close();
						}
					} catch (final IOException ex) {
						ex.printStackTrace();
					}
				}
				break;
			case "charger":
				ObjectInputStream ois = null;
				try {
					final FileInputStream fichier = new FileInputStream(
							(File) dataValue);
					ois = new ObjectInputStream(fichier);
					Sauvegarde chargement = (Sauvegarde) ois.readObject();
					j1 = new Joueur(chargement.joueur1);
					j2 = new Joueur(chargement.joueur2);
					joueurCourant = new Joueur(chargement.joueurCourant);
					t = new Terrain(chargement.plateau);
					h = new Historique(chargement.histo);
				} catch (final java.io.IOException e) {
					e.printStackTrace();
				} catch (final ClassNotFoundException e) {
					e.printStackTrace();
				} finally {
					try {
						if (ois != null) {
							ois.close();
						}
					} catch (final IOException ex) {
						ex.printStackTrace();
					}
				}
				ech.vider();
				ech.ajouter("terrain", t.getTableau());
				Joueur[] tab = { j1, j2 };
				ech.ajouter("joueurs", tab);
				com.envoyer(ech);
				break;
			}
		}
	}
}
