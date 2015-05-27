import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;


public class Terrain {
	public enum Direction {
		haut, bas, gauche, droite, hautGauche, hautDroite, basGauche, basDroite
	}

	public enum ChoixPrise {
		parPercussion, parAspiration 
	}

	public final static int LIGNES = 5;
	public final static int COLONNES = 9;
	public final static int INDICE_MAX_LIGNES = LIGNES - 1;
	public final static int INDICE_MAX_COLONNES = COLONNES - 1;
	public Case tableau[][] = new Case[LIGNES][COLONNES];
	private Scanner sc;

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

	public void TerrainTest(int numTest){
		for (int ligne = 0; ligne < Terrain.LIGNES; ligne++)
			for (int colonne = 0; colonne < Terrain.COLONNES; colonne++)
				this.tableau[ligne][colonne].setOccupation(Case.Etat.vide);
			
		switch(numTest){ // Pour tous les tests le joueur 1 doit commencer
			case 1 : // Test deux prises dans direction identique d'affilée impossible
				this.tableau[0][4].setOccupation(Case.Etat.joueur1);
				this.tableau[1][3].setOccupation(Case.Etat.joueur1);
				this.tableau[2][5].setOccupation(Case.Etat.joueur1);
				this.tableau[3][0].setOccupation(Case.Etat.joueur2);
				this.tableau[3][1].setOccupation(Case.Etat.joueur1);
				this.tableau[3][4].setOccupation(Case.Etat.joueur2);
				// Joueur 2 DOIT gagner en un seul tour
			break;
			
			case 2 : // Test impossible de revenir sur ses pas
				this.tableau[2][0].setOccupation(Case.Etat.joueur2);
				this.tableau[2][1].setOccupation(Case.Etat.joueur1);
				this.tableau[2][3].setOccupation(Case.Etat.joueur2);
				// Joueur 2 DOIT gagner
			break;
		}
	}
	
	public Terrain copie(){
		Terrain copieTerrain = new Terrain();
		for (int i=0;i<LIGNES;i++){
			for (int j=0;j<COLONNES;j++){
				copieTerrain.tableau[i][j]=this.tableau[i][j].copie();
			}
		}
		return copieTerrain;
	}
	
	public Case[][] getTableau() {
		return tableau;
	}

	public void setTableau(Case tableau[][]) {
		this.tableau = tableau;
	}

	public Case getCase(int ligne, int colonne) {
		return tableau[ligne][colonne];
	}

	public void setCase(Case.Etat e, int ligne, int colonne) {
		tableau[ligne][colonne].setOccupation(e);
	}

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


	public int deplacement(Point depart, Point arrive, Joueur joueurCourant, ArrayList<Point> listePredecesseurs) {

		Iterator<Point> iterator = listePredecesseurs.iterator();

		if(listePredecesseurs != null)
			while (iterator.hasNext()) {
				Point pNext = iterator.next();
				if (arrive.equals(pNext))
					return 4;
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

	public ArrayList<Point> manger(Joueur joueurCourant, Direction dir, Point pDepart, Point pArrivee) {

		Case.Etat joueurOppose = null;
		Point offsetPercu, offsetAspi;
		Point pTestOffset = new Point(0, 0);
		boolean priseParPercussion = false, priseParAspiration = false;
		ArrayList<Point> listePionsManges = new ArrayList<Point>();

		// On indique dans une variable qui est l'adversaire pour reconnaître ses pions
		
		if (joueurCourant.getJoueurID() == Case.Etat.joueur1)
			joueurOppose = Case.Etat.joueur2;
		else if(joueurCourant.getJoueurID() == Case.Etat.joueur2)
			joueurOppose = Case.Etat.joueur1;


		// Gestion de l'offset pour une éventuelle prise par percussion
		offsetPercu = this.offsetPercussion(dir, pArrivee);
		// Gestion de l'offset pour une éventuelle prise par aspiration
		offsetAspi = this.offsetAspiration(dir, pDepart);

		if (!offsetPercu.equals(pTestOffset)) {
			// Ici si la case suivante à la position d'arrivée est à
			// l'adversaire on a une percussion
			if (this.tableau[pArrivee.x + offsetPercu.x][pArrivee.y + offsetPercu.y].getCase().getOccupation() == joueurOppose)
				priseParPercussion = true;
		}

		if (!offsetAspi.equals(pTestOffset)) {
			// Ici si la case précédente à la position de départ est à
			// l'adversaire on a une aspiration
			if (this.tableau[pDepart.x + offsetAspi.x][pDepart.y + offsetAspi.y].getCase().getOccupation() == joueurOppose)
				priseParAspiration = true;
		}

		/*
		 * C'est ici que la mise à jour de la case mangée s'effectue et qu'on
		 * effectue un appel récursif à manger
		 */
		if (priseParAspiration && priseParPercussion) { // Si on a deux types de
														// prise un choix													
			ChoixPrise choix;							// s'impose
			
			if(joueurCourant.isJoueurHumain())
				choix = this.choixPriseHumain();
			else
				choix = IntelligenceArtificielle.choixPriseIAFacile();
			
			if (choix == ChoixPrise.parPercussion) {
				this.prisePercussion(pArrivee, dir, joueurOppose, listePionsManges);
			} else {
				this.priseAspiration(pDepart, dir, joueurOppose, listePionsManges);
			}
		} 	
		else if (priseParPercussion)  							// Sinon on applique la prise selon le								
			this.prisePercussion(pArrivee, dir, joueurOppose, listePionsManges); // seul choix possible
			
		else if (priseParAspiration) 
			this.priseAspiration(pDepart, dir, joueurOppose, listePionsManges);
		
		return listePionsManges;
	}

	/*
	 * Méthode récursive permettant la prise de pions par percussion et de
	 * renvoyer le nombre de pions capturés paramètres : - pArrivee, le point
	 * précédent le point testé (potentiellement mangé) - dir, la direction
	 * explorée permettant de trouver l'offset nécessaire - le type de pion du
	 * joueur opposé (joueur1 ou joueur2)
	 */
	public void prisePercussion(Point pArrivee, Direction dir, Case.Etat joueurOppose, ArrayList<Point> listePionsManges) {

		Point offsetPercu = this.offsetPercussion(dir, pArrivee);

		// Ici si la case suivante à la position d'arrivée est à l'adversaire on
		// a une percussion
		if (this.tableau[pArrivee.x + offsetPercu.x][pArrivee.y + offsetPercu.y].getCase().getOccupation() == joueurOppose) {
			this.tableau[pArrivee.x + offsetPercu.x][pArrivee.y + offsetPercu.y].setOccupation(Case.Etat.vide);
			this.prisePercussion(new Point(pArrivee.x + offsetPercu.x, pArrivee.y + offsetPercu.y), dir, joueurOppose,listePionsManges);
			listePionsManges.add(new Point((pArrivee.x + offsetPercu.x),(pArrivee.y + offsetPercu.y)));		
		}

	}

	/*
	 * Idem à la méthode "prisePercussion" mais adaptée à la prise par
	 * aspiration
	 */
	public void priseAspiration(Point pDepart, Direction dir, Case.Etat joueurOppose, ArrayList<Point> listePionsManges) {

		Point offsetAspi = this.offsetAspiration(dir, pDepart);

		// Ici si la case suivante à la position d'arrivée est à l'adversaire on
		// a une percussion
		if (this.tableau[pDepart.x + offsetAspi.x][pDepart.y + offsetAspi.y].getCase().getOccupation() == joueurOppose) {
			this.tableau[pDepart.x + offsetAspi.x][pDepart.y + offsetAspi.y].setOccupation(Case.Etat.vide);
			this.priseAspiration(new Point(pDepart.x + offsetAspi.x, pDepart.y + offsetAspi.y), dir, joueurOppose,listePionsManges);
			listePionsManges.add(new Point((pDepart.x + offsetAspi.x),(pDepart.y + offsetAspi.y)));
		}

	}

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
		
		if( (pArrivee.x + offsetPercu.x) < 0 || (pArrivee.x + offsetPercu.x > INDICE_MAX_LIGNES) || (pArrivee.y + offsetPercu.y < 0) || (pArrivee.y + offsetPercu.y > INDICE_MAX_COLONNES) )
			return new Point(0,0);
					
		return offsetPercu;
	}

	/*
	 * Trés similairement à la méthode "offsetPercussion" les valeurs attribuées
	 * par l'offset sont ici inversées. En effet si la direction de déplacement
	 * est par exempe hautGauche il faudra explorer la diagonale basDroite
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

		if( (pDepart.x + offsetAspi.x) < 0 || (pDepart.x + offsetAspi.x > INDICE_MAX_LIGNES) || (pDepart.y + offsetAspi.y < 0) || (pDepart.y + offsetAspi.y > INDICE_MAX_COLONNES) )
			return new Point(0,0);
		
		return offsetAspi;
	}

	/*
	 * Cette méthode intervient lorsque le joueur peut lors d'un déplacement effectuer soit une prise par 
	 * percussion soit par aspiration, il doit donc choisir entre une des deux solutions
	 */
	ChoixPrise choixPriseHumain(){

		this.sc = new Scanner(System.in);
		char choixPrise = 'Y';

		do {
			System.out.println("Prise par percussion ? (Y/N) : ");
			choixPrise = this.sc.nextLine().charAt(0);
		} while (choixPrise != 'Y' && choixPrise != 'N');
		
		if (choixPrise == 'Y')
			return ChoixPrise.parPercussion;
		else
			return ChoixPrise.parAspiration;
	}

	boolean estUnePrisePercussion(Point depart, Direction d) {
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

	boolean estUnePriseAspiration(Point depart, Direction d) {
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

		}
		return b;
	}



	ArrayList<Point> couplibre(Joueur joueurCourant) {
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
					if (c.getOccupation() == joueurCourant.getJoueurID()) {
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
	
	
}
