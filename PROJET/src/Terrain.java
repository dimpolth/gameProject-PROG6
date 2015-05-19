import java.awt.Point;
import java.util.ArrayList;


public class Terrain {
	public enum Direction{
		haut,
		bas,
		gauche,
		droite,
		hautGauche,
		hautDroite,
		basGauche,
		basDroite
	}
	
	public enum ChoixPrise{
		parPercussion,
		parAspiration
	}
	
	private final static int LIGNES = 5;
	private final static int COLONNES = 9;
	private Case tableau[][] = new Case[5][9];
	
	public Terrain() 
	{	
		for(int ligne = 0 ; ligne < Terrain.LIGNES; ligne++)
			for(int colonne = 0 ; colonne < Terrain.COLONNES; colonne++) {
				if(ligne == 0 || ligne == 1) 
					this.tableau[ligne][colonne] = new Case(new Point(ligne,colonne), Case.Etat.joueur1);
				else if(ligne == 3 || ligne == 4)
					this.tableau[ligne][colonne] = new Case(new Point(ligne,colonne), Case.Etat.joueur2);
				else {
					if(colonne == 0 || colonne == 2 || colonne == 5 || colonne == 7)
						this.tableau[ligne][colonne] = new Case(new Point(ligne,colonne), Case.Etat.joueur1);
					else if(colonne == 1 || colonne == 3 || colonne == 6 || colonne == 8)
						this.tableau[ligne][colonne] = new Case(new Point(ligne,colonne), Case.Etat.joueur2);
					else
						this.tableau[ligne][colonne] = new Case(new Point(ligne,colonne), Case.Etat.vide);
				}
			}
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

	public void dessineTableauAvecIntersections()
	{
		for(int ligne = 0 ; ligne < Terrain.LIGNES; ligne++){
			for(int colonne = 0 ; colonne < Terrain.COLONNES; colonne++){
				if(this.tableau[ligne][colonne].getOccupation() == Case.Etat.joueur1)
					System.out.print("X");
				else if(this.tableau[ligne][colonne].getOccupation() == Case.Etat.joueur2)
					System.out.print("O");
				else 
					System.out.print(" ");
				
				if(colonne < Terrain.COLONNES - 1)
					System.out.print("-");
			}
			System.out.println();
			
			if(ligne < Terrain.LIGNES - 1)
				if(ligne % 2 == 0)
					System.out.println("|\\|/|\\|/|\\|/|\\|/|");
			
				else 
					System.out.println("|/|\\|/|\\|/|\\|/|\\|");
		}

		
		System.out.println();
	}
	
	public int deplacement(Point depart, Point arrive) {
		if(tableau[depart.x][depart.y].getOccupation() == Case.Etat.vide) {
			return 3;
		}
		else {
			if(tableau[arrive.x][arrive.y].getOccupation() != Case.Etat.vide) {
				return 2;

			} else {
				ArrayList<Point> l = tableau[depart.x][depart.y].getSucc(); // on regarde si la case d'arrivée est bien un successeur
				for(int it = 0; it < l.size(); it++) {
					Point p = l.get(it);
					if(arrive.equals(p)) {
						tableau[arrive.x][arrive.y].setOccupation(tableau[depart.x][depart.y].getOccupation());
						tableau[depart.x][depart.y].setOccupation(Case.Etat.vide);
						return 0;
					}
				}
				return 1;	
			}
		}
	}
	
	public Direction recupereDirection(Point depart, Point arrive){
		Direction dir = null;
		
		if((arrive.x == depart.x - 1) && (arrive.y == depart.y - 1))
			dir = Direction.hautGauche;
		if((arrive.x == depart.x - 1) && (arrive.y == depart.y))
			dir = Direction.haut;
		if((arrive.x == depart.x - 1) && (arrive.y == depart.y + 1))
			dir = Direction.hautDroite;
		if((arrive.x == depart.x) && (arrive.y == depart.y + 1))
			dir = Direction.droite;
		if((arrive.x == depart.x + 1) && (arrive.y == depart.y + 1))
			dir = Direction.basDroite;
		if((arrive.x == depart.x + 1) && (arrive.y == depart.y))
			dir = Direction.bas;
		if((arrive.x == depart.x + 1) && (arrive.y == depart.y - 1))
			dir = Direction.basGauche;
		if((arrive.x == depart.x) && (arrive.y == depart.y - 1))
			dir = Direction.gauche;
		
		return dir;
	}
	
	public void manger(Case.Etat joueurCourant, Direction dir, Point pDepart, Point pArrivee) {
		
		Case.Etat joueurOppose;
		Point offsetPercu, offsetAspi;
		Point pTestOffset = new Point(0,0);
		boolean priseParPercussion = false, priseParAspiration = false;
		
		
		// On indique dans une variable qui est l'adversaire pour reconnaître ses pions
		if(joueurCourant == Case.Etat.joueur1)
			joueurOppose = Case.Etat.joueur2;
		else if(joueurCourant == Case.Etat.joueur2)
			joueurOppose = Case.Etat.joueur1;
		else
			return;
			
		// Gestion de l'offset pour une éventuelle prise par percussion
		offsetPercu = this.offsetPercussion(dir, pArrivee);
		// Gestion de l'offset pour une éventuelle prise par aspiration
		offsetAspi = this.offsetAspiration(dir, pDepart);
			
		
		if(!offsetPercu.equals(pTestOffset)){
			// Ici si la case suivante à la position d'arrivée est à l'adversaire on a une percussion
			if(this.tableau[pArrivee.x + offsetPercu.x][pArrivee.y + offsetPercu.y].getCase().getOccupation() == joueurOppose)
				priseParPercussion = true;			
		}
		
		if(!offsetAspi.equals(pTestOffset)){
			// Ici si la case précédente à la position de départ est à l'adversaire on a une aspiration
			if(this.tableau[pDepart.x + offsetAspi.x][pDepart.y + offsetAspi.y].getCase().getOccupation() == joueurOppose)
				priseParAspiration = true;
		}
			
		/*
		 * C'est ici que la mise à jour de la case mangée s'effectue et qu'on effectue un appel récursif à manger
		 */
		if(priseParAspiration && priseParPercussion){ // Si on a deux types de prise un choix s'impose
			if(this.choixPrise() == ChoixPrise.parPercussion){
				this.tableau[pArrivee.x + offsetPercu.x][pArrivee.y + offsetPercu.y].setOccupation(Case.Etat.vide);
				this.manger(joueurCourant, dir, pDepart, new Point(pArrivee.x + offsetPercu.x,pArrivee.y + offsetPercu.y));
			}
			else{
				this.tableau[pDepart.x + offsetAspi.x][pDepart.y + offsetAspi.y].setOccupation(Case.Etat.vide);
				this.manger(joueurCourant, dir, new Point(pDepart.x + offsetAspi.x, pDepart.y + offsetAspi.y), pArrivee );
			}
		}
		else if(priseParPercussion){ // Sinon on applique la prise selon le seul choix possible
			this.tableau[pArrivee.x + offsetPercu.x][pArrivee.y + offsetPercu.y].setOccupation(Case.Etat.vide);
			this.manger(joueurCourant, dir, pDepart, new Point(pArrivee.x + offsetPercu.x,pArrivee.y + offsetPercu.y));
		}
		else if(priseParAspiration){
			this.tableau[pDepart.x + offsetAspi.x][pDepart.y + offsetAspi.y].setOccupation(Case.Etat.vide);
			this.manger(joueurCourant, dir, new Point(pDepart.x + offsetAspi.x, pDepart.y + offsetAspi.y), pArrivee );
		}

	}
	
	public Point offsetPercussion(Direction dir, Point pArrivee){
		Point offsetPercu = new Point(0,0);
		
		if(pArrivee.x > 0 && pArrivee.x < 4 && pArrivee.y > 0 && pArrivee.y < 8){
			switch(dir){ // c'est ce switch qui effectue l'attribution du offset nécessaire 
				case hautGauche :
					offsetPercu.x = -1;
					offsetPercu.y = -1;
				break;
				
				case haut :
					offsetPercu.x = -1;
					offsetPercu.y = 0;
				break;
				
				case hautDroite :
					offsetPercu.x = -1;
					offsetPercu.y = 1;
				break;
				
				case droite :
					offsetPercu.x = 0;
					offsetPercu.y = 1;
				break;
				
				case basDroite :
					offsetPercu.x = 1;
					offsetPercu.y = 1;
				break;
				
				case bas :
					offsetPercu.x = 1;
					offsetPercu.y = 0;
				break;
				
				case basGauche :
					offsetPercu.x = 1;
					offsetPercu.y = -1;
				break;
					
				case gauche :
					offsetPercu.x = 0;
					offsetPercu.y = -1;
				break;
			}
		}
		return offsetPercu;
	}
	
	/*
	 * Trés similairement à la méthode "offsetPercussion" les valeurs attribuées par l'offset sont ici inversées.
	 * En effet si la direction de déplacement est par exempe hautGauche il faudra explorer la diagonale basDroite
	 */
	public Point offsetAspiration(Direction dir, Point pDepart){
		Point offsetAspi = new Point(0,0);
		
		if(pDepart.x > 0 && pDepart.x < 4 && pDepart.y > 0 && pDepart.y < 8){
			switch(dir){ 
				case hautGauche :
					offsetAspi.x = 1;
					offsetAspi.y = 1;
				break;
				
				case haut :
					offsetAspi.x = 1;
					offsetAspi.y = 0;
				break;
				
				case hautDroite :
					offsetAspi.x = 1;
					offsetAspi.y = -1;
				break;
				
				case droite :
					offsetAspi.x = 0;
					offsetAspi.y = -1;
				break;
				
				case basDroite :
					offsetAspi.x = -1;
					offsetAspi.y = -1;
				break;
				
				case bas :
					offsetAspi.x = -1;
					offsetAspi.y = 0;
				break;
				
				case basGauche :
					offsetAspi.x = -1;
					offsetAspi.y = 1;
				break;
					
				case gauche :
					offsetAspi.x = 0;
					offsetAspi.y = 1;
				break;
			}
		}
		return offsetAspi;
	}
	
	ChoixPrise choixPrise(){
		System.out.println("Choisissez votre prise.");
		return ChoixPrise.parPercussion; // prise par percussion
	}
	
	
}


