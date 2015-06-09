package modele;

import java.io.Serializable;

/**
 * Classe contenant les paramètres de la partie.
 */
public class Parametres implements Serializable{
	/**
	 * Variable de sérialisation.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Définit la difficulté de l'IA ou si le joueur est humain.
	 */
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
	
	/**
	 * Nom du joueur 1
	 */
	public String j1_identifiant=null;
	/**
	 * Nom du joueur 2.
	 */
	public String j2_identifiant=null;
	/**
	 * Type du joueur 1.
	 */
	public NiveauJoueur j1_type = null;
	/**
	 * Type du joueur 2.
	 */
	public NiveauJoueur j2_type=null;
	
	/**
	 * Renvoie sous forme de chaîne de caractère le contenu de la classe
	 */
	public String toString(){
		return j1_identifiant+"-"+j2_identifiant+"  -  "+j1_type+"-"+j2_type;
	}

}
