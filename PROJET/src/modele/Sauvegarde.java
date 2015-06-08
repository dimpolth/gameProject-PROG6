package modele;
import java.io.Serializable;

/**
 * Classe contenant les éléments à sauvegarder.
 */
public class Sauvegarde implements Serializable {
	/**
	 * Variable de sérialisation.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Terrin de jeu.
	 */
	public Terrain plateau;
	/**
	 * Joueur 1.
	 */
	public Joueur joueur1;
	/**
	 * Joueur 2.
	 */
	public Joueur joueur2;
	/**
	 * Joueur en cours.
	 */
	public Joueur joueurCourant;
	/**
	 * Historique de la partie.
	 */
	public Historique histo;
	
	/**
	 * Constructeur.
	 * @param t Le terrain à sauvegarder.
	 * @param h L'historique à sauvegarder.
	 * @param j1 Le joueur 1 à sauvegarder.
	 * @param j2 Le joueur 2 à sauvegarder.
	 * @param joueurCourant Le joueur courant à sauvegarder;
	 */
	public Sauvegarde(Terrain t, Historique h,Joueur j1,Joueur j2 ,Joueur joueurCourant){
		this.plateau = t.copie();
		this.joueur1 = j1; 
		this.joueur2 = j2;
		this.joueurCourant = joueurCourant;
		this.histo = h;
	}

	
}
