package modele;
import java.awt.*;
import java.io.Serializable;
import java.util.*;

/**
 * Classe définissant l'occupation d'une case.
 */
public class Case implements Serializable {
	/**
	 * Définit l'occupation d'une case. 
	 */
	public enum Etat {
		joueur1(1),
		joueur2(2),
		vide(0);
		
		int entier;
		Etat(int i){
			entier=i;
		}
		/**
		 * Permet d'obtenir l'entier associé à une occupation.
		 * @return L'entier correspondant à l'occupation de la case.
		 */
		public int getNum(){
			return entier;
		}
	}
	
	/**
	 * Liste des cases accessibles depuis la case courante.
	 */
	public ArrayList<Point> succ;
	/**
	 * Occupation de la case courante.
	 */
	public Etat occupation;
	/**
	 * Position de la case sur le terrain.
	 */
	public Point pos;
	
	
	/**
	 * Constructeur unique.
	 * @param p Position de la case à construir.
	 * @param e Occupation de la case.
	 */
	public Case(Point p, Etat e) {
		occupation = e;
		pos = p;
		this.initSuccesseurs();
	} 
	
	/**
	 * Réalise une copie de la case courante.
	 * @return La copie de la case.
	 */
	public Case copie(){
		return new Case(pos,occupation); 
	}
	
	/**
	 * Permet d'obtenir l'occupation de la case.
	 * @return L'occuopation de la case courante.
	 */
	public Etat getOccupation() {
		return occupation;
	}
	
	/**
	 * Permet d'obtenir la position de la case.
	 * @return Un point représentant la position de la case.
	 */
	Point getPos(){
		return pos;
	}
	
	/**
	 * Modifie l'occupation de la case. Utilisé lors d'une prise.
	 * @param e Nouvelle occupation de la case.
	 */
	void setOccupation(Etat e) {
		occupation = e;
	}
	
	/**
	 * Permet d'obtenir la référence de la case.
	 * @return La référence de la case;
	 */
	Case getCase() {
		return this;
	}
	
	/**
	 * Permet d'obtenir la liste des positions atteignables depuis la case courante.
	 * @return Liste de points atteignables.
	 */
	public ArrayList<Point> getSucc() {
		return succ;
	}
	
	/**
	 * Créer la liste des cases atteignables depuis la case courante.
	 */
	void initSuccesseurs(){
		this.succ = new ArrayList<Point>();
		boolean diagonales = false;
		
		/* Dans tous les cas nous avons les successeurs haut,bas,droite,gauche
		 * on créé donc si possible ces 4 successeurs
		 */
		if(this.pos.x - 1 >= 0) // successeur haut
			this.succ.add(new Point(this.pos.x - 1,this.pos.y));
		if(this.pos.x + 1  < Terrain.LIGNES) // successeur bas
			this.succ.add(new Point(this.pos.x + 1,this.pos.y));
		if(this.pos.y - 1 >= 0) // successeur gauche
			this.succ.add(new Point(this.pos.x,this.pos.y - 1));
		if(this.pos.y + 1 < Terrain.COLONNES) // successeur droite
			this.succ.add(new Point(this.pos.x,this.pos.y + 1));
		
		if((this.pos.x % 2) == 0){ // on est sur une ligne impaire (en admettant que la ligne 0 est en fait la première ligne)
			if((this.pos.y % 2) == 0) // on est sur une colonne impaire (en admettant que la colonne 0 est en fait la première colonne)
				diagonales = true;
		}	
		else{ // on est sur une ligne paire
			if((this.pos.y % 2) == 1) // on est sur une colonne paire
				diagonales = true;
		}
		
		if(diagonales){ // on doit ajouter SI POSSIBLE les 4 successeurs présents en diagonale
			if( (this.pos.x - 1 >= 0) && (this.pos.y - 1 >= 0) ) // successeur diagonale haut/gauche
				this.succ.add(new Point(this.pos.x - 1,this.pos.y - 1));
			if( (this.pos.x - 1 >= 0) && (this.pos.y + 1 < Terrain.COLONNES) ) // successeur diagonale haut/droite
				this.succ.add(new Point(this.pos.x - 1,this.pos.y + 1));
			if( (this.pos.x + 1 < Terrain.LIGNES) && (this.pos.y - 1 >= 0) ) // successeur diagonale bas/gauche
				this.succ.add(new Point(this.pos.x + 1,this.pos.y - 1));
			if( (this.pos.x + 1 < Terrain.LIGNES) && (this.pos.y + 1 < Terrain.COLONNES) ) // successeur diagonale bas/droite
				this.succ.add(new Point(this.pos.x + 1,this.pos.y + 1));
		}
		
	}
}