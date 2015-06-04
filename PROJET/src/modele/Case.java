package modele;
import java.awt.*;
import java.io.Serializable;
import java.util.*;

public class Case implements Serializable {
	public enum Etat {
	joueur1,
	joueur2,
	vide;
	}
	
	public ArrayList<Point> succ;
	public Etat occupation;
	public Point pos;
	
	
	
	public Case(Point p, Etat e) {
		occupation = e;
		pos = p;
		this.initSuccesseurs();
	} 
	
	public Case copie(){
		return new Case(pos,occupation); 
	}
	
	public Etat getOccupation() {
		return occupation;
	}
	
	Point getPos(){
		return pos;
	}
	
	void setOccupation(Etat e) {
		occupation = e;
	}
	
	Case getCase() {
		return this;
	}
	
	public ArrayList<Point> getSucc() {
		return succ;
	}
	
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