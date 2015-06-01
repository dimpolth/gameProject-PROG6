package modele;

import java.awt.Point;

/*
 * Un coup est un couple de case jouées : une case de départ et une case d'arrivée
 */
public class Coup {
	
	private Point pDepart;
	private Point pArrivee;
	private Terrain.ChoixPrise choixPrise;
	
	public Coup(Point pDepart, Point pArrivee) {
		this.setpDepart(pDepart);
		this.setpArrivee(pArrivee);
		this.setChoixPrise(Terrain.ChoixPrise.parPercussion); // par défaut prise par percussion
	}
	
	public Coup(Point pDepart, Point pArrivee, Terrain.ChoixPrise choixPrise) {
		this.setpDepart(pDepart);
		this.setpArrivee(pArrivee);
		this.setChoixPrise(choixPrise); // par défaut prise par percussion
	}

	public Point getpDepart() {
		return pDepart;
	}

	public void setpDepart(Point pDepart) {
		this.pDepart = pDepart;
	}

	public Point getpArrivee() { 
		return pArrivee;
	}

	public void setpArrivee(Point pArrivee) {
		this.pArrivee = pArrivee;
	}
	
	public Coup clone(){
		Coup copie = new Coup((Point) this.pDepart.clone(), (Point) this.pArrivee.clone());
		return copie;
	}

	public Terrain.ChoixPrise getChoixPrise() {
		return choixPrise;
	}

	public void setChoixPrise(Terrain.ChoixPrise choixPrise) {
		this.choixPrise = choixPrise;
	}
}
