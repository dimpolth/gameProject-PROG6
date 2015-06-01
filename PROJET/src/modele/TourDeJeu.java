package modele;

import java.util.ArrayList;


public class TourDeJeu {
	
	private ArrayList<Coup> listeCoups;
	private int valeurResultat; // Valeur positive ou négative permettant d'évaluer si ce tour est bénéfique
	private Terrain terrainFinal; // Une copie de l'état du terrain après le tour
	
	public TourDeJeu(){
		this.setListeCoups(new ArrayList<Coup>());
		this.setValeurResultat(0);
	}
	
	public TourDeJeu(Coup coupUnique){ // Tours de jeu constitués d'un seul coup (déplacement libre)
		ArrayList<Coup> lCoupUnique = new ArrayList<Coup>();
		lCoupUnique.add(coupUnique);
		this.setListeCoups(lCoupUnique);
		this.setValeurResultat(0);
	}
	
	public TourDeJeu(ArrayList<Coup> listeC, int valRes, Terrain terrainFinal){
		this.setListeCoups(listeC);
		this.setValeurResultat(valRes);
		this.setTerrainFinal(terrainFinal);
	}
	
	public void addCoup(Coup c){
		this.listeCoups.add(c);
	}
	
	public ArrayList<Coup> getListeCoups() {
		return listeCoups;
	}

	public void setListeCoups(ArrayList<Coup> listeCoups) {
		this.listeCoups = listeCoups;
	}

	public int getValeurResultat() {
		return valeurResultat;
	}

	public void setValeurResultat(int valeurResultat) {
		this.valeurResultat = valeurResultat;
	}
	
	public TourDeJeu clone(){
		ArrayList<Coup> cloneListeCoups = (ArrayList<Coup>) this.getListeCoups().clone();
		TourDeJeu copie = new TourDeJeu(cloneListeCoups, this.getValeurResultat(), this.terrainFinal);
		
		return copie;
	}

	public Terrain getTerrainFinal() {
		return terrainFinal;
	}

	public void setTerrainFinal(Terrain terrainFinal) {
		this.terrainFinal = terrainFinal;
	}
}
