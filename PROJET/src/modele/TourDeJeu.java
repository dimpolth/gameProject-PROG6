package modele;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Classe contenant un tour complet. Utilisée uniquement par l'IA.
 */
public class TourDeJeu implements Serializable {
	
	/**
	 * Liste des coups joués par l'IA.
	 */
	private ArrayList<Coup> listeCoups;
	/**
	 * Valeur positive ou négative permettant d'évaluer si ce tour est bénéfique.
	 */
	private int valeurResultat;
	/**
	 * Une copie de l'état du terrain après le tour.
	 */
	private Terrain terrainFinal;
	
	/**
	 * Constructeur par défaut.
	 */
	public TourDeJeu(){
		this.setListeCoups(new ArrayList<Coup>());
		this.setValeurResultat(0);
	}
	
	/**
	 * Constructeur pour un tour de jeu constitué d'un seul coup (déplacement libre)
	 * @param coupUnique Coup joué pour ce tour.
	 */
	public TourDeJeu(Coup coupUnique){
		ArrayList<Coup> lCoupUnique = new ArrayList<Coup>();
		lCoupUnique.add(coupUnique);
		this.setListeCoups(lCoupUnique);
		this.setValeurResultat(0);
	}
	
	/**
	 * Constructeur par copie.
	 * @param listeC Liste des coups à copiés.
	 * @param valRes Valeur d'évaluation à copier.
	 * @param terrainFinal Terrain à copié.
	 */
	public TourDeJeu(ArrayList<Coup> listeC, int valRes, Terrain terrainFinal){
		this.setListeCoups(listeC);
		this.setValeurResultat(valRes);
		this.setTerrainFinal(terrainFinal);
	}
	
	/**
	 * Ajoute un coup à la liste des coups joués.
	 * @param c Coup à ajouter.
	 */
	public void addCoup(Coup c){
		this.listeCoups.add(c);
	}
	
	/**
	 * Permet d'obtenir la liste des coups joués.
	 * @return La liste des coups joués.
	 */
	public ArrayList<Coup> getListeCoups() {
		return listeCoups;
	}

	/**
	 * Modifie la liste des coups joués.
	 * @param listeCoups Nouvelle liste de coups joués.
	 */
	public void setListeCoups(ArrayList<Coup> listeCoups) {
		this.listeCoups = listeCoups;
	}

	/**
	 * Permet d'obtenir la valeur d'évaluation.
	 * @return La valeur d'évaluation du tour.
	 */
	public int getValeurResultat() {
		return valeurResultat;
	}

	/**
	 * Modifie la valeur d'évaluation.
	 * @param valeurResultat Nouvelle valeur d'évaluation du tour.
	 */
	public void setValeurResultat(int valeurResultat) {
		this.valeurResultat = valeurResultat;
	}
	
	/**
	 * Copie la classe.
	 */
	public TourDeJeu clone(){
		ArrayList<Coup> cloneListeCoups = (ArrayList<Coup>) this.getListeCoups().clone();
		TourDeJeu copie = new TourDeJeu(cloneListeCoups, this.getValeurResultat(), this.terrainFinal);
		
		return copie;
	}

	/**
	 * Permet d'obtenir le terrain modifié par le tour.
	 * @return Le terrain mis à jour.
	 */
	public Terrain getTerrainFinal() {
		return terrainFinal;
	}

	/**
	 * Modifie le terrain.
	 * @param terrainFinal Nouveau terrain.
	 */
	public void setTerrainFinal(Terrain terrainFinal) {
		this.terrainFinal = terrainFinal;
	}
}
