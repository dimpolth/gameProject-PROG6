package modele;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *Classe contenant tout ce qui touche au terrain de jeu. 
 */
public class Terrain implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Définit les directions des déplacements. 
	 */
	public enum Direction {
		haut, bas, gauche, droite, hautGauche, hautDroite, basGauche, basDroite
	}
	/**
	 * Définit une prise lors d'un choix. 
	 */
	public enum ChoixPrise {
		parPercussion, parAspiration
	}

	/**
	 * Largeur du terrain de jeu.
	 */
	public final static int LIGNES = 5;
	/**
	 * Longueur du terrain dej= jeu.
	 */
	public final static int COLONNES = 9;
	/**
	 * Indice maximal en largeur du tableau représentant le terrain;
	 */
	public final static int INDICE_MAX_LIGNES = LIGNES - 1;
	/**
	 * Indice maximal en longueur du tableau représentant le terrain.
	 */
	public final static int INDICE_MAX_COLONNES = COLONNES - 1;
	/**
	 * Représente le terrain de jeu.
	 */
	public Case tableau[][] = new Case[LIGNES][COLONNES];

	/**
	 * Constructeur par défaut.
	 */
	public Terrain() {
		for (int ligne = 0; ligne < Terrain.LIGNES; ligne++)
			for (int colonne = 0; colonne < Terrain.COLONNES; colonne++) {
				if (ligne == 0 || ligne == 1)
					this.tableau[ligne][colonne] = new Case(new Point(ligne, colonne), Case.Etat.joueur1);
				else if (ligne == 3 || ligne == 4)
					this.tableau[ligne][colonne] = new Case(new Point(ligne, colonne), Case.Etat.joueur2);
				else {
					if (colonne == 0 || colonne == 2 || colonne == 5 || colonne == 7)
						this.tableau[ligne][colonne] = new Case(new Point(ligne, colonne), Case.Etat.joueur1);
					else if (colonne == 1 || colonne == 3 || colonne == 6 || colonne == 8)
						this.tableau[ligne][colonne] = new Case(new Point(ligne, colonne), Case.Etat.joueur2);
					else
						this.tableau[ligne][colonne] = new Case(new Point(ligne, colonne), Case.Etat.vide);
				}
			}
	}

	/**
	 * Constructeur par copie.
	 * @param t Terrain à copier.
	 */
	public Terrain(Terrain t) {
		tableau = t.getTableau();
	}

	/**
	 * Génère des terrains alternatifs pour les tests.
	 * @param numTest Numéro du terrain de test à générer.
	 */
	public void TerrainTest(int numTest) {
		for (int ligne = 0; ligne < Terrain.LIGNES; ligne++)
			for (int colonne = 0; colonne < Terrain.COLONNES; colonne++)
				this.tableau[ligne][colonne].setOccupation(Case.Etat.vide);

		switch (numTest) { // Pour tous les tests le joueur 1 doit commencer
		case 1: // Test deux prises dans direction identique d'affilée
				// impossible
			this.tableau[0][4].setOccupation(Case.Etat.joueur1);
			this.tableau[1][3].setOccupation(Case.Etat.joueur1);
			this.tableau[2][5].setOccupation(Case.Etat.joueur1);
			this.tableau[3][0].setOccupation(Case.Etat.joueur2);
			this.tableau[3][1].setOccupation(Case.Etat.joueur1);
			this.tableau[3][4].setOccupation(Case.Etat.joueur2);
			// Joueur 2 DOIT gagner en un seul tour
			break;

		case 2: // Test impossible de revenir sur ses pas
			this.tableau[2][0].setOccupation(Case.Etat.joueur2);
			this.tableau[2][1].setOccupation(Case.Etat.joueur1);
			this.tableau[2][3].setOccupation(Case.Etat.joueur2);
			// Joueur 2 DOIT gagner
			break;

		case 3: // Test IA Moyenne
			this.tableau[0][2].setOccupation(Case.Etat.joueur2);
			this.tableau[0][5].setOccupation(Case.Etat.joueur2);
			this.tableau[0][6].setOccupation(Case.Etat.joueur1);
			this.tableau[1][2].setOccupation(Case.Etat.joueur2);
			this.tableau[3][1].setOccupation(Case.Etat.joueur1);
			this.tableau[3][2].setOccupation(Case.Etat.joueur1);
			this.tableau[3][4].setOccupation(Case.Etat.joueur2);
			this.tableau[4][0].setOccupation(Case.Etat.joueur1);
			this.tableau[4][3].setOccupation(Case.Etat.joueur2);
			break;

		case 4: // test aspiration percussion IA moyenne
			this.tableau[0][0].setOccupation(Case.Etat.joueur2);
			this.tableau[0][1].setOccupation(Case.Etat.joueur2);
			this.tableau[0][2].setOccupation(Case.Etat.joueur2);
			this.tableau[0][3].setOccupation(Case.Etat.joueur1);
			this.tableau[0][5].setOccupation(Case.Etat.joueur2);
			break;

		case 5: // Test IA Moyenne
			this.tableau[0][2].setOccupation(Case.Etat.joueur2);
			this.tableau[0][5].setOccupation(Case.Etat.joueur2);
			this.tableau[0][6].setOccupation(Case.Etat.joueur1);
			this.tableau[1][2].setOccupation(Case.Etat.joueur2);
			this.tableau[1][4].setOccupation(Case.Etat.joueur2);
			this.tableau[1][5].setOccupation(Case.Etat.joueur2);
			this.tableau[1][6].setOccupation(Case.Etat.joueur2);
			this.tableau[2][0].setOccupation(Case.Etat.joueur1);
			this.tableau[3][1].setOccupation(Case.Etat.joueur1);
			this.tableau[3][2].setOccupation(Case.Etat.joueur1);
			this.tableau[3][4].setOccupation(Case.Etat.joueur2);
			this.tableau[4][0].setOccupation(Case.Etat.joueur1);
			this.tableau[4][4].setOccupation(Case.Etat.joueur2);
			break;

		case 6: // Test IA Moyenne
			this.tableau[0][2].setOccupation(Case.Etat.joueur1);
			this.tableau[1][1].setOccupation(Case.Etat.joueur1);
			this.tableau[1][3].setOccupation(Case.Etat.joueur2);
			this.tableau[1][4].setOccupation(Case.Etat.joueur2);
			this.tableau[2][2].setOccupation(Case.Etat.joueur1);
			this.tableau[3][2].setOccupation(Case.Etat.joueur2);
			this.tableau[4][1].setOccupation(Case.Etat.joueur1);
			this.tableau[4][3].setOccupation(Case.Etat.joueur2);
			//this.tableau[4][4].setOccupation(Case.Etat.joueur2);
			this.tableau[4][5].setOccupation(Case.Etat.joueur2);
			this.tableau[4][6].setOccupation(Case.Etat.joueur2);
			break;

		case 7:
			this.tableau[1][1].setOccupation(Case.Etat.joueur1);
			this.tableau[1][2].setOccupation(Case.Etat.joueur2);
			break;

		case 8: // petit terrain équivalent pour simulations plus rapides
			for (int i = 0; i < 5; i++)
				for (int j = 0; j < 5; j++) {
					if (i == 0 || i == 1)
						this.tableau[i][j + 2].setOccupation(Case.Etat.joueur1);
					else
						this.tableau[i][j + 2].setOccupation(Case.Etat.joueur2);

				}

			this.tableau[2][2].setOccupation(Case.Etat.joueur1);
			this.tableau[2][4].setOccupation(Case.Etat.vide);
			this.tableau[2][6].setOccupation(Case.Etat.joueur1);
			break;

		case 9:
			this.tableau[1][1].setOccupation(Case.Etat.joueur1);
			this.tableau[1][2].setOccupation(Case.Etat.joueur2);
		break;
		
		case 10:
			this.tableau[1][1].setOccupation(Case.Etat.joueur1);
			this.tableau[2][4].setOccupation(Case.Etat.joueur1);
			this.tableau[3][1].setOccupation(Case.Etat.joueur2);
			this.tableau[4][4].setOccupation(Case.Etat.joueur2);
			this.tableau[0][1].setOccupation(Case.Etat.joueur2);
			this.tableau[0][4].setOccupation(Case.Etat.joueur2);
		break;
		
		case 11:
			this.tableau[1][0].setOccupation(Case.Etat.joueur1);
			this.tableau[2][0].setOccupation(Case.Etat.joueur1);
			this.tableau[0][1].setOccupation(Case.Etat.joueur2);
			this.tableau[3][0].setOccupation(Case.Etat.joueur2);
			this.tableau[4][0].setOccupation(Case.Etat.joueur2);
			this.tableau[4][4].setOccupation(Case.Etat.joueur2);
			this.tableau[3][1].setOccupation(Case.Etat.joueur2);
		}
	}

	/**
	 * Réalise une copie du terrain actuel.
	 * @return La copie du terrain.
	 */
	public Terrain copie() {
		Terrain copieTerrain = new Terrain();
		for (int i = 0; i < LIGNES; i++) {
			for (int j = 0; j < COLONNES; j++) {
				copieTerrain.tableau[i][j].occupation = this.tableau[i][j].occupation;
				copieTerrain.tableau[i][j].pos = this.tableau[i][j].pos;
				copieTerrain.tableau[i][j].succ = this.tableau[i][j].succ;
			}
		}
		return copieTerrain;
	}

	/**
	 * Permet d'obtenir le terrain de jeu.
	 * @return La matrice du terrain de jeu.
	 */
	public Case[][] getTableau() {
		return tableau;
	}

	/**
	 * Remplace le terrain de jeu.
	 * @param tableau Terrain de jeu à charger.
	 */
	public void setTableau(Case tableau[][]) {
		this.tableau = tableau;
	}

	/**
	 * Permet d'obtenir le contenu d'une case de la matrice.
	 * @param ligne Ligne de la case.
	 * @param colonne Colonne de la case.
	 * @return Case correspondante.
	 */
	public Case getCase(int ligne, int colonne) {
		return tableau[ligne][colonne];
	}

	/**
	 * Modifie l'occupation d'une case.
	 * @param e La nouvelle occupation de la case.
	 * @param ligne Ligne de la case à modifier.
	 * @param colonne Colonne de la case à modifier.
	 */
	public void setCase(Case.Etat e, int ligne, int colonne) {
		tableau[ligne][colonne].setOccupation(e);
	}

	/**
	 * Dessine le terrain en console. Utilisé pour le debug.
	 */
	public void dessineTableauAvecIntersections() {
		for (int ligne = 0; ligne < Terrain.LIGNES; ligne++) {
			for (int colonne = 0; colonne < Terrain.COLONNES; colonne++) {
				if (this.tableau[ligne][colonne].getOccupation() == Case.Etat.joueur1)
					System.out.print("X");
				else if (this.tableau[ligne][colonne].getOccupation() == Case.Etat.joueur2)
					System.out.print("O");
				else
					System.out.print(" ");

				if (colonne < Terrain.INDICE_MAX_COLONNES)
					System.out.print("-");
			}
			System.out.println();

			if (ligne < Terrain.INDICE_MAX_LIGNES)
				if (ligne % 2 == 0)
					System.out.println("|\\|/|\\|/|\\|/|\\|/|");

				else
					System.out.println("|/|\\|/|\\|/|\\|/|\\|");
		}

		System.out.println();
	}
	
	/**
	 * Réalise le déplacement d'un pion.
	 * @param depart Point de départ du pion.
	 * @param arrive Point d'arrivé du pion;
	 * @param joueurCourant Joueur qui à réaliser le déplacement.
	 * @param listePredecesseurs Liste des coups précédents du tour en cours.
	 * @return 0 si déplacement réalisé avec succès. 1 si le point d'arrivé n'est pas un successeur. 2 si la case d'arrivé n'est pas vide.
	 * 3 si le point de départ n'appartient pas au joueur courant. 4 si le point d'arrivé a déjà servit pendant le tour.
	 */
	public int deplacement(Point depart, Point arrive, Joueur joueurCourant, ArrayList<Point> listePredecesseurs) {
		if (!listePredecesseurs.isEmpty()) {
			Iterator<Point> iterator = listePredecesseurs.iterator();
			while (iterator.hasNext()) {
				Point pNext = iterator.next();
				if (arrive.equals(pNext))
					return 4;
			}
		}
		if (tableau[depart.x][depart.y].getOccupation() != joueurCourant.getJoueurID()) {
			return 3;
		} else {
			if (tableau[arrive.x][arrive.y].getOccupation() != Case.Etat.vide) {
				return 2;
			} else {
				ArrayList<Point> l = tableau[depart.x][depart.y].getSucc(); // on regarde si la case d'arrivée est bien un successeur
				for (int it = 0; it < l.size(); it++) {
					Point p = l.get(it);
					if (arrive.equals(p)) {
						tableau[arrive.x][arrive.y].setOccupation(tableau[depart.x][depart.y].getOccupation());
						tableau[depart.x][depart.y].setOccupation(Case.Etat.vide);
						return 0;
					}
				}
				return 1;
			}
		}
	}

	/**
	 * Définit la direction du déplacement.
	 * @param depart Point de départ du déplacement.
	 * @param arrive Point d'arrivé du déplacement.
	 * @return Direction du déplacement.
	 */
	public Direction recupereDirection(Point depart, Point arrive) {
		Direction dir = null;
		if ((arrive.x == depart.x - 1) && (arrive.y == depart.y - 1))
			dir = Direction.hautGauche;
		if ((arrive.x == depart.x - 1) && (arrive.y == depart.y))
			dir = Direction.haut;
		if ((arrive.x == depart.x - 1) && (arrive.y == depart.y + 1))
			dir = Direction.hautDroite;
		if ((arrive.x == depart.x) && (arrive.y == depart.y + 1))
			dir = Direction.droite;
		if ((arrive.x == depart.x + 1) && (arrive.y == depart.y + 1))
			dir = Direction.basDroite;
		if ((arrive.x == depart.x + 1) && (arrive.y == depart.y))
			dir = Direction.bas;
		if ((arrive.x == depart.x + 1) && (arrive.y == depart.y - 1))
			dir = Direction.basGauche;
		if ((arrive.x == depart.x) && (arrive.y == depart.y - 1))
			dir = Direction.gauche;

		return dir;
	}

	/**
	 * Effectue la prise de pions.
	 * @param joueurCourant Le joueur qui réalise la prise.
	 * @param dir La direction du coup qui effectue la prise.
	 * @param pDepart Point d edépart du coup.
	 * @param pArrivee Point d'arrivé du coup.
	 * @param c La type de prise à réaliser. 
	 * @return La liste des pions qui ont été mangés.
	 */
	public ArrayList<Point> manger(Joueur joueurCourant, Direction dir, Point pDepart, Point pArrivee, ChoixPrise c) {

		ArrayList<Point> listePionsManges = new ArrayList<Point>();
		Case.Etat joueurOppose;

		// On indique dans une variable qui est l'adversaire pour reconnaître ses pions

		if (joueurCourant.getJoueurID() == Case.Etat.joueur1)
			joueurOppose = Case.Etat.joueur2;
		else
			joueurOppose = Case.Etat.joueur1;

		if (c == ChoixPrise.parPercussion) // Sinon on applique la prise selon
											// le
			this.prisePercussion(pArrivee, dir, joueurOppose, listePionsManges); // seul
																					// choix
																					// possible
		else if (c == ChoixPrise.parAspiration)
			this.priseAspiration(pDepart, dir, joueurOppose, listePionsManges);

		return listePionsManges;

	}

	/*
	 *  paramètres : - pArrivee,  - dir,  -  (joueur1 ou joueur2)
	 */
	/**
	 * Méthode récursive permettant la prise de pions par percussion.
	 * @param pArrivee Le point précédent le point testé (potentiellement mangé).
	 * @param dir La direction explorée permettant de trouver l'offset nécessaire.
	 * @param joueurOppose le type de pion du joueur opposé.
	 * @param listePionsManges Retour par référence. Liste des pions mangés par la prise.
	 */
	public void prisePercussion(Point pArrivee, Direction dir, Case.Etat joueurOppose, ArrayList<Point> listePionsManges) {

		Point offsetPercu = this.offsetPercussion(dir, pArrivee);

		// Ici si la case suivante à la position d'arrivée est à l'adversaire on
		// a une percussion
		if (this.tableau[pArrivee.x + offsetPercu.x][pArrivee.y + offsetPercu.y].getCase().getOccupation() == joueurOppose) {
			this.tableau[pArrivee.x + offsetPercu.x][pArrivee.y + offsetPercu.y].setOccupation(Case.Etat.vide);
			this.prisePercussion(new Point(pArrivee.x + offsetPercu.x, pArrivee.y + offsetPercu.y), dir, joueurOppose, listePionsManges);
			listePionsManges.add(new Point((pArrivee.x + offsetPercu.x), (pArrivee.y + offsetPercu.y)));
		}

	}

	/**
	 * Méthode récursive permettant la prise de pions par aspiration.
	 * @param pDepart Le point précédent le point testé (potentiellement mangé).
	 * @param dir La direction explorée permettant de trouver l'offset nécessaire.
	 * @param joueurOppose le type de pion du joueur opposé.
	 * @param listePionsManges Retour par référence. Liste des pions mangés par la prise.
	 */
	public void priseAspiration(Point pDepart, Direction dir, Case.Etat joueurOppose, ArrayList<Point> listePionsManges) {

		Point offsetAspi = this.offsetAspiration(dir, pDepart);

		// Ici si la case suivante à la position d'arrivée est à l'adversaire on
		// a une percussion
		if (this.tableau[pDepart.x + offsetAspi.x][pDepart.y + offsetAspi.y].getCase().getOccupation() == joueurOppose) {
			this.tableau[pDepart.x + offsetAspi.x][pDepart.y + offsetAspi.y].setOccupation(Case.Etat.vide);
			this.priseAspiration(new Point(pDepart.x + offsetAspi.x, pDepart.y + offsetAspi.y), dir, joueurOppose, listePionsManges);
			listePionsManges.add(new Point((pDepart.x + offsetAspi.x), (pDepart.y + offsetAspi.y)));
		}

	}

	/**
	 * Calcul le décalage nécessaire à une prise par percussion.
	 * @param dir Direction de la prise.
	 * @param pArrivee Point d'arrivé de la prise.
	 * @return Un décalage sous forme de point.
	 */
	public Point offsetPercussion(Direction dir, Point pArrivee) {
		Point offsetPercu = new Point(0, 0);

		switch (dir) { // c'est ce switch qui effectue l'attribution du

		// offset nécessaire
		case hautGauche:
			offsetPercu.x = -1;
			offsetPercu.y = -1;
			break;

		case haut:
			offsetPercu.x = -1;
			offsetPercu.y = 0;
			break;

		case hautDroite:
			offsetPercu.x = -1;
			offsetPercu.y = 1;
			break;

		case droite:
			offsetPercu.x = 0;
			offsetPercu.y = 1;
			break;

		case basDroite:
			offsetPercu.x = 1;
			offsetPercu.y = 1;
			break;

		case bas:
			offsetPercu.x = 1;
			offsetPercu.y = 0;
			break;

		case basGauche:
			offsetPercu.x = 1;
			offsetPercu.y = -1;
			break;

		case gauche:
			offsetPercu.x = 0;
			offsetPercu.y = -1;
			break;
		}

		if ((pArrivee.x + offsetPercu.x) < 0 || (pArrivee.x + offsetPercu.x > INDICE_MAX_LIGNES) || (pArrivee.y + offsetPercu.y < 0) || (pArrivee.y + offsetPercu.y > INDICE_MAX_COLONNES))
			return new Point(0, 0);

		return offsetPercu;
	}

	/**
	 * Trés similairement à la méthode "offsetPercussion" les valeurs attribuées par l'offset sont ici inversées.
	 * En effet si la direction de déplacement est par exempe hautGauche il faudra explorer la diagonale basDroite
	 * @param dir Direction de la prise.
	 * @param pDepart Point de départ de la prise.
	 */
	public Point offsetAspiration(Direction dir, Point pDepart) {
		Point offsetAspi = new Point(0, 0);

		switch (dir) {
		case hautGauche:
			offsetAspi.x = 1;
			offsetAspi.y = 1;
			break;

		case haut:
			offsetAspi.x = 1;
			offsetAspi.y = 0;
			break;

		case hautDroite:
			offsetAspi.x = 1;
			offsetAspi.y = -1;
			break;

		case droite:
			offsetAspi.x = 0;
			offsetAspi.y = -1;
			break;

		case basDroite:
			offsetAspi.x = -1;
			offsetAspi.y = -1;
			break;

		case bas:
			offsetAspi.x = -1;
			offsetAspi.y = 0;
			break;

		case basGauche:
			offsetAspi.x = -1;
			offsetAspi.y = 1;
			break;

		case gauche:
			offsetAspi.x = 0;
			offsetAspi.y = 1;
			break;
		}

		if ((pDepart.x + offsetAspi.x) < 0 || (pDepart.x + offsetAspi.x > INDICE_MAX_LIGNES) || (pDepart.y + offsetAspi.y < 0) || (pDepart.y + offsetAspi.y > INDICE_MAX_COLONNES))
			return new Point(0, 0);

		return offsetAspi;
	}

	/**
	 * Test si une prise se fait par percussion.
	 * @param depart Point de départ de la prise.
	 * @param d Direction de la prise.
	 * @return Vrai si la prise est par percussion, faux sinon.
	 */
	public boolean estUnePrisePercussion(Point depart, Direction d) {
		Point cible = new Point();
		boolean b = false;

		switch (d) {
		case hautGauche:
			cible.x = depart.x - 2;
			cible.y = depart.y - 2;
			break;
		case haut:
			cible.x = depart.x - 2;
			cible.y = depart.y;
			break;
		case hautDroite:
			cible.x = depart.x - 2;
			cible.y = depart.y + 2;
			break;
		case droite:
			cible.x = depart.x;
			cible.y = depart.y + 2;
			break;
		case basDroite:
			cible.x = depart.x + 2;
			cible.y = depart.y + 2;
			break;
		case bas:
			cible.x = depart.x + 2;
			cible.y = depart.y;
			break;
		case basGauche:
			cible.x = depart.x + 2;
			cible.y = depart.y - 2;
			break;
		case gauche:
			cible.x = depart.x;
			cible.y = depart.y - 2;
			break;
		}
		if (!(cible.x > INDICE_MAX_LIGNES || cible.x < 0 || cible.y > INDICE_MAX_COLONNES || cible.y < 0)) {
			b = ((tableau[cible.x][cible.y].getOccupation() != tableau[depart.x][depart.y].getOccupation()) && (tableau[cible.x][cible.y].getOccupation() != Case.Etat.vide));
		}
		return b;
	}

	/**
	 * Test si une prise se fait par aspiration.
	 * @param depart Point de départ de la prise.
	 * @param d Direction de la prise.
	 * @return Vrai si la prise se fait par aspiration, faux sinon.
	 */
	public boolean estUnePriseAspiration(Point depart, Direction d) {
		Point cible = new Point();
		boolean b = false;

		switch (d) {
		case hautGauche:
			cible.x = depart.x + 1;
			cible.y = depart.y + 1;
			break;
		case haut:
			cible.x = depart.x + 1;
			cible.y = depart.y;
			break;
		case hautDroite:
			cible.x = depart.x + 1;
			cible.y = depart.y - 1;
			break;
		case droite:
			cible.x = depart.x;
			cible.y = depart.y - 1;
			break;
		case basDroite:
			cible.x = depart.x - 1;
			cible.y = depart.y - 1;
			break;
		case bas:
			cible.x = depart.x - 1;
			cible.y = depart.y;
			break;
		case basGauche:
			cible.x = depart.x - 1;
			cible.y = depart.y + 1;
			break;
		case gauche:
			cible.x = depart.x;
			cible.y = depart.y + 1;
			break;
		}
		if (!(cible.x > INDICE_MAX_LIGNES || cible.x < 0 || cible.y > INDICE_MAX_COLONNES || cible.y < 0)) {
			b = ((tableau[cible.x][cible.y].getOccupation() != tableau[depart.x][depart.y].getOccupation()) && (tableau[cible.x][cible.y].getOccupation() != Case.Etat.vide));
			// ;//System.out.println("Cible : "+tableau[cible.x][cible.y].getOccupation());
			// ;//System.out.println("Depart : "+tableau[depart.x][depart.y].getOccupation());
		}
		return b;
	}

	/**
	 * Permet de savoir quels sont les coups réalisant une prise jouables.
	 * @param joueurCourant Joueur dont le tour est en cours.
	 * @return La liste des pions qui peuvent jouer.
	 */
	public ArrayList<Point> couplibre(Case.Etat joueurCourant) {
		ArrayList<Point> reponse = new ArrayList<Point>();

		for (int x = 0; x < LIGNES; x++) {
			for (int y = 0; y < COLONNES; y++) {
				int nbSucc = tableau[x][y].getSucc().size();
				Case c = tableau[x][y].getCase();
				// for(int z=0; z< nbSucc-1 ;z++ ){
				int z = 0;
				boolean drap = true;
				while (z < nbSucc && drap) {
					Point pointSucc = c.getSucc().get(z);
					Direction d = recupereDirection(c.getPos(), pointSucc);
					if (c.getOccupation() == joueurCourant) {
						if ((estUnePriseAspiration(c.getPos(), d) || estUnePrisePercussion(c.getPos(), d)) && (tableau[pointSucc.x][pointSucc.y].getOccupation() == Case.Etat.vide)) {
							reponse.add(c.getPos());
							drap = false;
						}
					}
					z++;
				}
			}
		}
		return reponse;
	}

	/**
	 * Permet de savoir quels sont les coups réalisant une prise jouables. Utilisé par l'IA.
	 * @param joueurCourant Joueur dont le tour est en cours.
	 * @return La liste des coups qui peuvent être joués.
	 */
	public ArrayList<Coup> coupsObligatoires(Joueur joueurCourant) {
		ArrayList<Coup> listeCoupsRes = new ArrayList<Coup>();
		ArrayList<Point> listePointsDep = this.listePionsJouables(joueurCourant), listeSuc, listeVide = new ArrayList<Point>();;
		Iterator<Point> itDepart, itSuc;
		Point pDepTemp, pArrTemp;
		Direction dTemp;

		itDepart = listePointsDep.iterator();

		while (itDepart.hasNext()) {
			pDepTemp = itDepart.next();
			listeSuc = this.deplacementPossible(pDepTemp, listeVide);
			itSuc = listeSuc.iterator();

			while (itSuc.hasNext()) {
				pArrTemp = itSuc.next();

				dTemp = recupereDirection(pDepTemp, pArrTemp);

				if ((estUnePriseAspiration(pDepTemp, dTemp) || estUnePrisePercussion(pDepTemp, dTemp)) && (tableau[pArrTemp.x][pArrTemp.y].getOccupation() == Case.Etat.vide)){
					if(estUnePriseAspiration(pDepTemp, dTemp))
						listeCoupsRes.add(new Coup((Point) pDepTemp.clone(), (Point) pArrTemp.clone(), Terrain.ChoixPrise.parAspiration));
					if(estUnePrisePercussion(pDepTemp, dTemp))
						listeCoupsRes.add(new Coup((Point) pDepTemp.clone(), (Point) pArrTemp.clone(), Terrain.ChoixPrise.parPercussion));
				}
			}
		}

		return listeCoupsRes;
	}

	/**
	 * Détermine quels sont les déplacements possibles
	 * @param p Point à partir du quel on veut déterminer les déplacements possibles.
	 * @param listePredecesseurs ArrayList de Points. Liste des points par lesquels est passé le pion durant le tour.
	 * @param copieTerrainEventuelle Terrain. Utilisé par l'IA pour simuler des coups.
	 * @return ArrayList de Points. Liste des emplacements vers lequel le pion courant peut se déplacer.
	 */
	public ArrayList<Point> deplacementPossible(Point p, ArrayList<Point> listePredecesseurs) {
		ArrayList<Point> listeSuc = tableau[p.x][p.y].getSucc();
		ArrayList<Point> listeSolution = new ArrayList<Point>();
		Iterator<Point> it = listeSuc.iterator();
		Point pointPrec = new Point();
		Case[][] terr = tableau;

		while (it.hasNext()) {
			Point temp = (Point) it.next().clone();
			if (terr[temp.x][temp.y].getOccupation() == Case.Etat.vide && (!listePredecesseurs.contains(temp))) {
				if (listePredecesseurs.size() == 0)
					listeSolution.add(temp);
				else {
					pointPrec = listePredecesseurs.get(listePredecesseurs.size() - 1);
					Terrain.Direction dirPrec = recupereDirection(pointPrec, p);
					Terrain.Direction dirSuiv = recupereDirection(p, temp);
					if (dirPrec != dirSuiv) {
						listeSolution.add(temp);
					}
				}
			}
		}
		return listeSolution;
	}

	/**
	 * Génère une liste de pions jouables
	 * @param j Joueur courant.
	 * @param copieTerrainEventuelle Vrai si une copie du terrain doit être faite, faux sinon. Utilisé par l'IA.
	 * @return
	 */
	public ArrayList<Point> listePionsJouables(Joueur j) {
		Terrain terr = this;

		ArrayList<Point> listePions = couplibre(j.getJoueurID());
		if (listePions.isEmpty()) {
			for (int ligne = 0; ligne < Terrain.LIGNES; ligne++)
				for (int colonne = 0; colonne < Terrain.COLONNES; colonne++)
					if (terr.tableau[ligne][colonne].getOccupation() == j.getJoueurID())
						if (deplacementPossible((Point) new Point(ligne, colonne).clone(), new ArrayList<Point>()).size() > 0)
							listePions.add((Point) new Point(ligne, colonne).clone());
		}
		return listePions;
	}

}
