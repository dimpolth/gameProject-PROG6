package modele;

import java.io.Serializable;

public class Parametres implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public enum NiveauJoueur {
		HUMAIN, FACILE, MOYEN, DIFFICILE;
		static public NiveauJoueur getFromIndex(int i) {
			switch(i) {
			case 0:
				return HUMAIN;
			case 1:
				return FACILE;
			case 2:
				return MOYEN;
			default:
				return DIFFICILE;
			}
		}
	}

	public String j1_identifiant=null;
	public String j2_identifiant=null;
	public NiveauJoueur j1_type = null;
	public NiveauJoueur j2_type=null;

}
