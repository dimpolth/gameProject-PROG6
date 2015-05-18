import java.awt.Point;
import java.util.ArrayList;


public class Terrain {
	
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
	}
	
	public boolean deplacement(Point depart, Point arrive) {
		if(tableau[depart.x][depart.y].getOccupation() == Case.Etat.vide) {
			return false;
		}
		else {
			if(tableau[arrive.x][arrive.y].getOccupation() != Case.Etat.vide) {
				return false;
			} else {
				ArrayList<Point> l = tableau[depart.x][depart.y].getSucc();
				for(int it = 0; it < l.size(); it++) {
					Point p = l.get(it);
					if(arrive.equals(p)) {
						tableau[arrive.x][arrive.y].setOccupation(tableau[depart.x][depart.y].getOccupation());
						tableau[depart.x][depart.y].setOccupation(Case.Etat.vide);
						return true;
					}
				}
				return false;	
			}
		}
	}
}
