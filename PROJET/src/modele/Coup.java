package modele;

import java.awt.Point;
import java.io.Serializable;

/**
 * Un coup est un couple de case jouées : une case de départ et une case d'arrivée
 */
public class Coup implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Point de départ du coup.
	 */
	private Point pDepart;
	/**
	 * Point d'arrivé du coup.
	 */
	private Point pArrivee;
	/**
	 * Type de prise du coup.
	 */
	private Terrain.ChoixPrise choixPrise;
	
	/**
	 * Constructeur par défaut.
	 */
	public Coup(){
	}
	
	/**
	 * Constructeur pour un coup sans prise.
	 * @param pDepart Point de départ du coup.
	 * @param pArrivee Point d'arrivé du coup.
	 */
	public Coup(Point pDepart, Point pArrivee) {
		this.setpDepart(pDepart);
		this.setpArrivee(pArrivee);
		this.setChoixPrise(null);
	}
	
	/**
	 * Constructeur pour un coup avec prise.
	 * @param pDepart POint de départ du coup.
	 * @param pArrivee Point d'arrivé du coup.
	 * @param choixPrise Type de prise.
	 */
	public Coup(Point pDepart, Point pArrivee, Terrain.ChoixPrise choixPrise) {
		this.setpDepart(pDepart);
		this.setpArrivee(pArrivee);
		this.setChoixPrise(choixPrise);
	}

	/**
	 * Permet d'obtenir le point de départ.
	 * @return Le point de départ du coup.
	 */
	public Point getpDepart() {
		return pDepart;
	}

	/**
	 * Modifie le point de départ. 
	 * @param pDepart Nouveau point de départ.
	 */
	public void setpDepart(Point pDepart) {
		this.pDepart = pDepart;
	}

	/**
	 * Permet d'obtenir le point d'arrivé.
	 * @return Le point d'arrivé du coup.
	 */
	public Point getpArrivee() { 
		return pArrivee;
	}

	/**
	 * Modifie le point d'arrivé.
	 * @param pArrivee Nouveau point d'arrivé.
	 */
	public void setpArrivee(Point pArrivee) {
		this.pArrivee = pArrivee;
	}
	
	/**
	 * Permet d'obtenir un clone du coup sans qu'ils aient le même référencement.
	 * @return Clone du coup courant.
	 */
	public Coup clone(){
		Coup copie = new Coup((Point) this.pDepart.clone(), (Point) this.pArrivee.clone(), this.getChoixPrise());
		return copie;
	}

	/**
	 * Permet d'obtenir le type de prise.
	 * @return Type de prise du coup.
	 */
	public Terrain.ChoixPrise getChoixPrise() {
		return choixPrise;
	}

	/**
	 * Modifie le type de prise.
	 * @param choixPrise Nouveau type de prise.
	 */
	public void setChoixPrise(Terrain.ChoixPrise choixPrise) {
		this.choixPrise = choixPrise;
	}
	
	/**
	 * Test si un coup est équivalent à un autre;
	 * @param c Coup à comparer au coup courant.
	 * @return Vrai si les coups sont équivalants. Faux sinon.
	 */
	public boolean equals(Coup c){
		
		if(!this.pDepart.equals(c.pDepart))
			return false;
		if(!this.pArrivee.equals(c.pArrivee))
			return false;
		
		if(this.getChoixPrise() != null && c.getChoixPrise() != null)
			if(!this.getChoixPrise().equals(c.getChoixPrise()))
				return false;
		
		return true;
	}
}
