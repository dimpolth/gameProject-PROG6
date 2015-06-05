package moteur;

import java.awt.Point;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

import javax.jws.soap.SOAPBinding.ParameterStyle;

import ia.*;
import ihm.*;
import modele.*;
import modele.Case.Etat;
//import modele.Joueur.typeJoueur;
import reseau.*;

/**
 *Classe contenant l'ensemble des règles du jeu.
 *Fait office de contrôleur et donne les instructions d'affichage à l'IHM.
 *Instancie les classes Terrain et IHM.
 *Fonctionne comme un automate.
 */
public class Moteur {
	public static void main(String[] args) {
		Moteur m = new Moteur();
		IHM i = new IHM();
		i.setVisible(true);
		i.com = new Communication(i, m, Communication.IHM);
		m.com = new Communication(i, m, Communication.MOTEUR);
		// m.init();
		i.lancer();
	}

	/**
	 *Définit les différents états de l'automate
	 */
	public enum EtatTour {
		selectionPion, selectionDestination, attenteChoix, jeuxIa, partieFinie;
	}

	/**
	 * Canal de communication entre le moteur et l'IHM.
	 */
	public Communication com;
	/**
	 * Terrain de jeu.
	 */
	public Terrain t;
	/**
	 * Historique de la partie.
	 */
	private Historique h;
	/**
	 * Etat du tour en cours (de l'automate).
	 */
	private EtatTour e;
	/**
	 * Point de départ du coup en cours.
	 */
	private Point pDepart;
	/**
	 * Point d'arrivé du coup en cours.
	 */
	private Point pArrive;
	/**
	 * Joueur du tour en cours.
	 */
	private Joueur joueurCourant;
	/**
	 * Joueur 1 (blanc).
	 */
	public Joueur j1;
	/**
	 * Joueur 2 (noir).
	 */
	public Joueur j2;
	/**
	 * Réception et envoi de l'echange sur le canal de communication.
	 */
	private Echange ech;
	/**
	 * Point d'aspiration si prise avec choix.
	 */
	private Point aspi;
	/**
	 * Point de percussion si prise avec choix.
	 */
	private Point perc;
	/**
	 * Vrai si un enchaînement de coup est en cours. Faux sinon.
	 */
	private Boolean tourEnCours;
	
	private ArrayList<Point> listePointDebut;
	/**
	 * Coup joué par l'IA.
	 */
	private Coup jeuIa;
	/**
	 * Vrai si affichage sur console. Faux sinon. Utilisé en debug.
	 */
	private boolean trace = true;

	/**
	 * Constructeur par défaut.
	 */
	public Moteur() {
	}

	/**
	 * Constructeur utilisé dans le cas d'un chargement de partie.
	 * @param t
	 * Le terrain à charger pour reprendre la partie.
	 */
	public Moteur(Terrain t) {
		this.t = t;
		h = new Historique();
		ech = new Echange();
	}

	/**
	 * Initialise le moteur. Cette methode n'est pas dans le constructeur car si
	 * une nouvelle partie est lancée par l'utilisateur, le moteur ne peut pas
	 * se construir lui-même.
	 */
	public void init() {
		t = new Terrain();

		//t.TerrainTest(11);
		

		h = new Historique();
		h.ajouterTour(t);
		ech = new Echange();
		listePointDebut = new ArrayList<Point>();
		j1 = new Joueur(Case.Etat.joueur1, Joueur.typeJoueur.humain, "Joueur 1");
		// j2 = new Joueur(Case.Etat.joueur2, Joueur.typeJoueur.humain,
		// "Joueur 2");
		// j1 = new Joueur(Case.Etat.joueur1, Joueur.typeJoueur.ordinateur,
		// IntelligenceArtificielle.difficulteIA.facile, j2, this);
		j2 = new Joueur(Case.Etat.joueur2, Joueur.typeJoueur.ordinateur, IntelligenceArtificielle.difficulteIA.normal, j1, t);
		joueurCourant = j1;
		
		//calculerScore();
		
		message("bandeauSup", joueurCourant.getNom());
		message("bandeauInf", "Selection du pion");
		if (joueurCourant.isJoueurHumain()) {
			e = EtatTour.selectionPion;
		} else {
			e = EtatTour.jeuxIa;
			jouerIa();
		}

	}

	// Renvoie une liste de points d'arrive permettant une prise
	/**
	 * Détermine si des prises sont réalisables parmis les déplacements
	 * possibles.
	 * 
	 * @param p
	 *            Point à partir duquel on essaye de déterminer des prises
	 * @param listePredecesseurs
	 *            ArrayList de Points. Liste des points par lesquels est passé
	 *            le pion durant le tour.
	 * @return ArrayList de Points. Liste des arrivées possibles pour lesquelles
	 *         une prise sera effectuée.
	 */
	public ArrayList<Point> prisePossible(Point p, ArrayList<Point> listePredecesseurs) {
		ArrayList<Point> listePrise = new ArrayList<Point>();
		ArrayList<Point> listeMouvement = t.deplacementPossible(p, listePredecesseurs, null);
		Iterator<Point> it = listeMouvement.iterator();
		while (it.hasNext()) {
			Point temp = (Point) it.next().clone();
			Terrain.Direction d = t.recupereDirection(p, temp);
			if (t.estUnePriseAspiration(p, d) || t.estUnePrisePercussion(p, d))
				listePrise.add(temp);
		}
		return listePrise;
	}
	
	/**
	 * Test à chaque fin de tour si la partie est terminée.
	 * @param aucunDeplacement
	 * Permet de savoir si la partie est bloquée.
	 * @return
	 * Vrai si la partie a été gagnée par un joueur ou si elle est bloquée.
	 * Faux sinon.
	 */
	public boolean partieTerminee(boolean aucunDeplacement) {
		ech.vider();
		if (joueurCourant.scoreNul() || aucunDeplacement) {
			ech.ajouter("bandeauSup", "<html><font color=#FF0000>" + joueurCourant.recupereJoueurOpposant(joueurCourant, j1, j2, false).getNom() + "</font></html>");
			ech.ajouter("bandeauInf", "<html><font color=#FF0000>à remporté la partie</font></html>");
			com.envoyer(ech);
			return true;
		} else
			return false;
	}
	/**
	 * Etat de l'automate où le moteur reçoit le pion sélectionné par le joueur ou l'IA.
	 * Test si le pion sélectionné correspond aux règles. 
	 * @param p
	 * Pion sélectionné
	 */
	public void selectionPion(Point p) {
		tourEnCours = false;
		
		if (t.getCase(p.x, p.y).getOccupation() != joueurCourant.getJoueurID()) {
		} else {
			listePointDebut = t.listePionsJouables(joueurCourant, null);
			if (listePointDebut.isEmpty())
				partieTerminee(true);
			if (listePointDebut.contains(p)) {
				pDepart = p;
				if (joueurCourant.isJoueurHumain()) {
					ech.vider();
					;//System.out.println("ON est passé a selection direction");
					ech.ajouter("pionSelectionne", pDepart);
					com.envoyer(ech);
				}
				message("bandeauInf", "Choisir la destination");
				e = EtatTour.selectionDestination;
			}
		}
	}
	
	/**
	 * Etat de l'automate où le moteur reçoit la destination sélectionnée par le joueur ou l'IA
	 * Test si la destination est conforme aux règles.
	 * @param p
	 * Point sélectionné pour effectuer un déplacement.
	 */
	public void selectionDestination(Point p) {
		if (!tourEnCours && listePointDebut.contains(p)) {
			pDepart = p;
			if (joueurCourant.isJoueurHumain()) {
				ech.vider();
				ech.ajouter("pionDeselectionne", true);
				ech.ajouter("pionSelectionne", pDepart);
				com.envoyer(ech);
			}
		} else {
			ArrayList<Point> l = prisePossible(pDepart, h.histoTour);
			if (l.isEmpty()) {
				l = t.deplacementPossible(pDepart, h.histoTour, null);
			}
			if (l.contains(p)) {
				pArrive = p;
				Terrain.Direction d = t.recupereDirection(pDepart, pArrive);
				boolean priseAspi = t.estUnePriseAspiration(pDepart, d);
				boolean prisePercu = t.estUnePrisePercussion(pDepart, d);
				if (t.deplacement(pDepart, pArrive, joueurCourant, h.histoTour) == 0) {
					Point[] tabPts = { pDepart, pArrive };
					gestionCoupGraphique(tabPts, null, null, null);
					prise(priseAspi, prisePercu);
					tourEnCours = true;
				}
			}
		}

	}
	 /**
	  * Effectue une prise en fonction des points de départ et d'arrivé en attribut.
	  * @param priseAspi
	  * Vrai si une prise par aspiration est disponible. Faux sinon.
	  * @param prisePercu
	  * Vrai si une prise par percusion est disponible. Faux sinon.
	  */
	public void prise(boolean priseAspi, boolean prisePercu) {
		Terrain.Direction d = t.recupereDirection(pDepart, pArrive);
		ArrayList<Point> l = new ArrayList<Point>();
		h.ajouterCoup(pDepart);
		if (priseAspi && prisePercu) {
			Terrain.ChoixPrise choix;
			if (joueurCourant.isJoueurHumain()) {
				;//System.out.println("Choix");
				Point offA = t.offsetAspiration(d, pDepart);
				aspi = new Point(offA.x + pDepart.x, offA.y + pDepart.y);
				Point offP = t.offsetPercussion(d, pArrive);
				perc = new Point(offP.x + pArrive.x, offP.y + pArrive.y);
				Point[] tabPts = { aspi, perc };
				gestionCoupGraphique(null, tabPts, null, null, null, "choisissez votre prise");
				e = EtatTour.attenteChoix;
			} else {
				choix = jeuIa.getChoixPrise();
				l = t.manger(joueurCourant, d, pDepart, pArrive, choix);
				majScore(l.size());
				int[] score = { j1.getScore(), j2.getScore() };

				gestionCoupGraphique(null, null, l, score);
				traceTerrain();

			}
		} else if (priseAspi && !prisePercu) {
			// ;//System.out.println("aspi");
			l = t.manger(joueurCourant, d, pDepart, pArrive, Terrain.ChoixPrise.parAspiration);
			majScore(l.size());
			int[] score = { j1.getScore(), j2.getScore() };

			gestionCoupGraphique(null, null, l, score);
			traceTerrain();

			if (joueurCourant.isJoueurHumain()) {
				testFinTour();
				ech.vider();
				ech.ajouter("finTour", true);
				com.envoyer(ech);
			}

		} else if (!priseAspi && prisePercu) {
			// ;//System.out.println("percu");
			l = t.manger(joueurCourant, d, pDepart, pArrive, Terrain.ChoixPrise.parPercussion);
			majScore(l.size());
			int[] score = { j1.getScore(), j2.getScore() };

			gestionCoupGraphique(null, null, l, score);
			traceTerrain();
			if (joueurCourant.isJoueurHumain()) {
				testFinTour();
				ech.vider();
				ech.ajouter("finTour", true);
				com.envoyer(ech,joueurCourant.getJoueurID().getNum());
			}
		} else {
			if (joueurCourant.isJoueurHumain())
				finTour();
		}
	}
	 /**
	  * Termine le tour en cours et change le joueur courant.
	  * Peut être appelée automatiquement si le joueur courant ne peut plus effectuer de prise,
	  * ou manuellement s'il décide de s'arrêter pendant un enchaînement.
	  */
	public void finTour() {
		// traceTerrain();
		if (joueurCourant.getJoueurID() == Case.Etat.joueur1)
			joueurCourant = j2;
		else
			joueurCourant = j1;
		h.effacerHistoTour();
		h.ajouterTour(t);
		ech.vider();
		ech.ajouter("pionDeselectionne", true);
		ech.ajouter("annuler", false);
		ech.ajouter("refaire", false);
		ech.ajouter("finTour", false);
		com.envoyer(ech);
		ech.vider();
		ech.ajouter("annuler", true);
		com.envoyer(ech, joueurCourant.getJoueurID().getNum());
		if (partieTerminee(false)) {
			e = EtatTour.partieFinie;
			;//System.out.println("FIN DE PARTIE");
		} else {
			// ;//System.out.println("FIN DE TOUR ");
			gestionCoupGraphique();
			if (joueurCourant.isJoueurHumain()) {
				e = EtatTour.selectionPion;
			} else {
				e = EtatTour.jeuxIa;
				jouerIa();
			}
		}
	}

	/**
	 * Test après chaque prise si le tour peut se terminer ou si un enchaînement est réalisable.
	 */
	public void testFinTour() {
		pDepart = pArrive;
		if (prisePossible(pDepart, h.histoTour).isEmpty()) {
			finTour();
		} else {
			e = EtatTour.selectionDestination;
			message("bandeauInf", "Selection destination");
		}
	}
	
	/**
	 * Met à jour le score de l'adversaire du joueur courant après une prise.
	 * @param nbPionsManges
	 * Nombre de pions mangés à l'adversaire
	 */
	public void majScore(int nbPionsManges) {
		Joueur.recupereJoueurOpposant(joueurCourant, j1, j2, false).setScore(nbPionsManges);
	}

	/**
	 * Envoie un message à afficher sur un bandeau de l'IHM.
	 * @param destination
	 * Bandeau de destination.
	 * @param message
	 * Message à afficher.
	 */
	public void message(String destination, String message) {
		ech.vider();
		ech.ajouter(destination, message);
		com.envoyer(ech);
	}

	/**
	 * Envoie toutes les informations necessaires à l'IHM pour réaliser l'actualisation de l'affichage lié à un coup.
	 * @param deplacement
	 * Tableau de deux Points contenant le point de départ et le point d'arrivé.
	 * Peut être à null en fonction de la situation.
	 * @param choixPrise
	 * Tableau de deux Points contenant un choix à faire entre une prise par aspiration ou par percussion.
	 * Peut être null s'il n'y a pas de choix à faire.
	 * @param pionsManges
	 * Liste des pions mangés pendant le coup.
	 * @param score
	 * Score des joueurs mis à jour en fonctions des pions mangés.
	 */
	public void gestionCoupGraphique(Point[] deplacement, Point[] choixPrise, ArrayList<Point> pionsManges, int[] score) {
		ech.vider();
		CoupGraphique cg = new CoupGraphique(deplacement, choixPrise, pionsManges, score, calculChemin());
		ech.ajouter("coup", cg);
		com.envoyer(ech);
	}

	/**
	 * Surchage de gestionCoupGraphique pour le cas ou on coup inclut un changement de bandeau.
	 * @param deplacement
	 * Tableau de deux Points contenant le point de départ et le point d'arrivé.
	 * Peut être à null en fonction de la situation.
	 * @param choixPrise
	 * Tableau de deux Points contenant un choix à faire entre une prise par aspiration ou par percussion.
	 * Peut être null s'il n'y a pas de choix à faire.
	 * @param pionsManges
	 * Liste des pions mangés pendant le coup.
	 * @param score
	 * Score des joueurs mis à jour en fonctions des pions mangés.
	 * @param chaine1
	 * Définie sur quel bandeau ira le message.
	 * @param chaine2
	 * Le message à afficher sur le bandeau.
	 */
	public void gestionCoupGraphique(Point[] deplacement, Point[] choixPrise, ArrayList<Point> pionsManges, int[] score, String chaine1, String chaine2) {
		ech.vider();
		CoupGraphique cg = new CoupGraphique(deplacement, choixPrise, pionsManges, score, chaine1, chaine2, calculChemin());
		ech.ajouter("coup", cg);
		com.envoyer(ech);
	}

	/**
	 * Surchage de gestionCoupGraphique pour le cas ou l'on envoie un terrain uniquement.
	 */
	public void gestionCoupGraphique() {
		ech.vider();
		CoupGraphique cg = new CoupGraphique();
		ech.ajouter("coup", cg);
		com.envoyer(ech);
	}

	/**
	 * Surchage de gestion gestionCoupGraphique pour le cas ou l'on ne met à jour que les bandeaux.
	 * @param chaine1
	 * Définie sur quel bandeau ira le message.
	 * @param chaine2
	 * Le message à afficher sur le bandeau.
	 */
	public void gestionCoupGraphique(String chaine1, String chaine2) {
		ech.vider();
		CoupGraphique cg = new CoupGraphique(null, null, null, null, chaine1, chaine2, null);
		ech.ajouter("coup", cg);
		com.envoyer(ech);
	}

	/**
	 * Permet de recalculer les scores des joueurs.
	 * Utilisée dans les cas de annuler/refaire et lors d'un chargement de partie.
	 */
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

	/**
	 * Permet de calculer le chemin du pion pendant le tour.
	 * @return
	 * Liste de Points correspondant aux positions occupées par le pion durant l'enchaînement.
	 */
	public ArrayList<Point> calculChemin() {
		ArrayList<Point> chemin = (ArrayList<Point>) h.histoTour.clone();
		if (!chemin.contains(pDepart))
			chemin.add(pDepart);
		if (!chemin.contains(pArrive))
			chemin.add(pArrive);
		return chemin;
	}

	/**
	 * Fait jouer l'IA lors de son tour et gère les échanges entre l'IA et le moteur.
	 */
	public void jouerIa() {
		Thread th = new Thread() {
			public void run() {
				do {
					// ;//System.out.println(joueurCourant.getNom());
					// ;//System.out.println("boucle IA");
					/*
					 * if(jeuIa != null) ;//System.out.println(jeuIa.getpDepart() +
					 * ";" + jeuIa.getpArrivee());
					 */
					jeuIa = joueurCourant.jouer();
					// ;//System.out.println(jeuIa.getpDepart() + ";" +
					// jeuIa.getpArrivee());
					// ;//System.out.println("depart "+jeuIa.getpDepart()+" arrivé "+jeuIa.getpArrivee());
					selectionPion(jeuIa.getpDepart());
					// ;//System.out.println("point depart moteur :"+pDepart);
					selectionDestination(jeuIa.getpArrivee());

					// t.dessineTableauAvecIntersections();

					// traceTerrain();

				} while (joueurCourant.IaContinue());
				// ;//System.out.println(" FIN DU JEU IA");

				finTour();
			}
		};
		th.start();
	}

	/**
	 * Permet de griser ou d'afficher les boutons annuler/refaire en fonction de l'état d'affiche de l'historique.
	 */
	public void gestionBouton() {
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
				} else {
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
		com.envoyer(ech,joueurCourant.getJoueurID().getNum());
	}

	/**
	 * Affiche l'état actuel du terrain en console. Utilisée uniquement en debug.
	 */
	public void traceTerrain() {
		if (trace)
			t.dessineTableauAvecIntersections();
	}

	/**
	 * Dirige l'automate en fonction de ce que l'IHM envoie et qui est lié à la seléction de pions.
	 * @param dataValue
	 * Point reçu de l'IHM via la méthode action.
	 */
	public void actionPoint(Object dataValue) {
		if (e == EtatTour.selectionPion) {
			// ;//System.out.println("e : " + e);
			selectionPion((Point) dataValue);
		} else if (e == EtatTour.selectionDestination) {
			// ;//System.out.println("e : " + e);
			selectionDestination((Point) dataValue);
		} else if (e == EtatTour.attenteChoix) {
			// ;//System.out.println("e : " + e);
			Terrain.Direction d = t.recupereDirection(pDepart, pArrive);
			ArrayList<Point> l = new ArrayList<Point>();
			boolean tperc = perc.equals((Point) dataValue);
			boolean taspi = aspi.equals((Point) dataValue);
			if (tperc || taspi) {
				if (tperc) {
					l = t.manger(joueurCourant, d, pDepart, pArrive, Terrain.ChoixPrise.parPercussion);
				} else if (taspi) {
					l = t.manger(joueurCourant, d, pDepart, pArrive, Terrain.ChoixPrise.parAspiration);
				}
				majScore(l.size());
				int[] score = { j1.getScore(), j2.getScore() };
				gestionCoupGraphique(null, null, l, score);
				ech.vider();
				ech.ajouter("finTour", true);
				com.envoyer(ech,joueurCourant.getJoueurID().getNum());
				traceTerrain();
				testFinTour();
			}
		}
	}
	
	/**
	 * Réalise une annulation sur commande de l'IHM et lui envoi les modifications necessaires.
	 */
	public void actionAnnuler() {
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
			int[] tabScore = { j1.getScore(), j2.getScore() };
			ech.ajouter("score", tabScore);
			com.envoyer(ech);
			gestionBouton();
			message("bandeauSup", joueurCourant.getNom());
			message("bandeauInf", "Selection du pion");
		}
	}
	
	/**
	 * Refait un coup sur commande de l'IHM et lui envoi les modifications necessaires.
	 */
	public void actionRefaire() {
		if (e != EtatTour.partieFinie) {
			ech.vider();
			Case[][] refaire = h.refaire().getTableau();
			if (refaire != null) {
				joueurCourant = joueurCourant.recupereJoueurOpposant(joueurCourant, j1, j2, false);
				if (!joueurCourant.isJoueurHumain()) {
					refaire = h.refaire().getTableau();
					if (refaire != null) {
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
			int[] tabScore = { j1.getScore(), j2.getScore() };
			ech.ajouter("score", tabScore);
			com.envoyer(ech);
			gestionBouton();
			message("bandeauSup", joueurCourant.getNom());
			message("bandeauInf", "Selection du pion");
		}
	}
	
	/**
	 * Sérialise et sauvegarde la partie dans un fichier.
	 * @param dataValue
	 * Référence du fichier sur lequel la sauvegarde sera effectuée.
	 */
	public void actionSauvegarder(Object dataValue) {
		Sauvegarde s = new Sauvegarde(t, h, j1, j2, joueurCourant);
		ObjectOutputStream oos = null;
		try {
			final FileOutputStream fichier = new FileOutputStream((File) dataValue);
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
	}
	
	/**
	 * Déserialise et charge un partie à partir d'un fichier.
	 * @param dataValue
	 * Référence du fichier à partir duquel charger.
	 */
	public void actionCharger(Object dataValue) {
		ObjectInputStream ois = null;
		try {
			final FileInputStream fichier = new FileInputStream((File) dataValue);
			ois = new ObjectInputStream(fichier);
			Sauvegarde chargement = (Sauvegarde) ois.readObject();
			t = new Terrain(chargement.plateau);
			h = new Historique(chargement.histo);
			if (chargement.joueur1.isJoueurHumain())
				j1 = new Joueur(chargement.joueur1);
			else {
				j1 = new Joueur(Case.Etat.joueur1, Joueur.typeJoueur.ordinateur, IntelligenceArtificielle.difficulteIA.normal, chargement.joueur2, t);
				j1.chargerScore(chargement.joueur1.getScore());
			}
			if (chargement.joueur2.isJoueurHumain())
				j2 = new Joueur(chargement.joueur2);
			else {
				j2 = new Joueur(Case.Etat.joueur2, Joueur.typeJoueur.ordinateur, IntelligenceArtificielle.difficulteIA.normal, chargement.joueur1, t);
				j2.chargerScore(chargement.joueur2.getScore());
			}
			joueurCourant = new Joueur(chargement.joueurCourant);
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
		calculerScore();
		int[] tabScore = { j1.getScore(), j2.getScore() };
		ech.ajouter("score", tabScore);
		com.envoyer(ech);
	}
	
	/**
	 * Met à jour les paramètres de la partie en fonction de ce qui est envoyé par l'IHM.
	 * @param dataValue
	 * Paramètres de la partie.
	 */
	public void actionParametre(Object dataValue) {
		Parametres p = (Parametres) dataValue;
		
		
		
		System.out.println("1 "+p.j1_identifiant);
		System.out.println("2 "+p.j2_identifiant);
		System.out.println("3 "+p.j1_type);
		System.out.println("4 "+p.j2_type);
		
		if (p.j1_identifiant != null)
			j1.setNom(p.j1_identifiant);
		if (p.j2_identifiant != null)
			j2.setNom(p.j2_identifiant);
		if(p.j1_type != null ){
			if (p.j1_type == Parametres.NiveauJoueur.HUMAIN) {
				j1.setJoueurHumain(true);
				j1.viderIa();
			} else {
				j1.setJoueurHumain(false);
				if (p.j1_type == Parametres.NiveauJoueur.FACILE)
					j1.chargerIa(IntelligenceArtificielle.difficulteIA.facile, j2, t);
				else if (p.j1_type == Parametres.NiveauJoueur.MOYEN)
					j1.chargerIa(IntelligenceArtificielle.difficulteIA.normal, j2, t);
				else if (p.j1_type == Parametres.NiveauJoueur.DIFFICILE)
					j1.chargerIa(IntelligenceArtificielle.difficulteIA.difficile, j2, t);
			}
		}
		if(p.j2_type != null ){
			if (p.j2_type == Parametres.NiveauJoueur.HUMAIN) {
				j2.setJoueurHumain(true);
				j2.viderIa();
			} else {
				j2.setJoueurHumain(false);
				if (p.j2_type == Parametres.NiveauJoueur.FACILE)
					j2.chargerIa(IntelligenceArtificielle.difficulteIA.facile, j1, t);
				else if (p.j2_type == Parametres.NiveauJoueur.MOYEN)
					j2.chargerIa(IntelligenceArtificielle.difficulteIA.normal, j1, t);
				else if (p.j1_type == Parametres.NiveauJoueur.DIFFICILE)
					j2.chargerIa(IntelligenceArtificielle.difficulteIA.difficile, j1, t);
			}
		}
		ech.vider();
		ech.ajouter("parametres", p);
		com.envoyer(ech);
		message("bandeauSup", joueurCourant.getNom());
	}

	/**
	 * Réalise les différentes actions en fonctions des commandes envoyées par l'IHM.
	 * @param o
	 * Contient la commande ainsi qu'un objet qui sera traité dans les actions.
	 * @param j
	 * Identifiant de joueur pour le réseau.
	 */
	public void action(Object o, int j) {
		Echange echange = (Echange)o;
		Case.Etat joueurReception = null;
		if (j == 1)
			joueurReception = Etat.joueur1;
		else if (j == 2)
			joueurReception = Etat.joueur2;

		if (Communication.enReseau() && trace) {
			//System.out.println("reception :" + joueurReception);
			//System.out.println("courant :" + joueurCourant.getJoueurID());
			//System.out.println("comparaison "+!joueurCourant.getJoueurID().equals(joueurReception));
		}

		for (String dataType : echange.getAll()) {
			Object dataValue = echange.get(dataType);
			
			if (Communication.enReseau() && (joueurCourant.getJoueurID() != joueurReception) && (dataType.equals("point")  || dataType.equals("annuler") || dataType.equals("refaire") || dataType.equals("finTour")))
				return;
			// System.out.println(dataType);
			// System.out.println("e : " + e);
			switch (dataType) {
			case "nouvellePartie":
				init();
				break;
			case "point":
				actionPoint(dataValue);
				break;
			case "terrain":
				ech.vider();
				ech.ajouter("terrain", t.getTableau());
				com.envoyer(ech);
				break;
			case "annuler":
				actionAnnuler();
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
				actionRefaire();
				break;
			case "finTour":
				if (tourEnCours)
					finTour();
				break;
			case "sauvegarder":
				actionSauvegarder(dataValue);
				break;
			case "charger":
				actionCharger(dataValue);
				break;

			case "parametres":
				actionParametre(dataValue);
				break;
			}
		}
	}
}
