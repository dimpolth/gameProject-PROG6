package modele;
import java.io.Serializable;

import ia.*;
import moteur.*;
public class Joueur implements Serializable {
	
	public enum typeJoueur{
		humain,
		ordinateur;
	}
	
	private Case.Etat joueurID;
	private boolean joueurHumain;
	private int score;
	private String nom;
	private IntelligenceArtificielle ia;
	
	//Constructeur pour joueur humain
	public Joueur(Case.Etat joueurID, typeJoueur joueur, String nom){
		this.setJoueurID(joueurID);
		score = 22;
		this.nom = nom;
		this.setJoueurHumain(true);
		ia = null;
	}
	
	//Constructeur pour joueur ordinateur
	public Joueur(Case.Etat joueurID, typeJoueur joueur, IntelligenceArtificielle.difficulteIA niveau, Joueur adversaire, Terrain t){
		this.setJoueurID(joueurID);
		score = 22;
		this.nom = "Ordinateur";
		this.setJoueurHumain(false);
		ia = new IntelligenceArtificielle(niveau, this, adversaire, t);
	}
	
	public Joueur() {
		
	}
	
	public Joueur(Joueur j) {
		setJoueurID(j.getJoueurID());
		score = j.getScore();
		joueurHumain = j.isJoueurHumain();
		nom = j.getNom();
		ia = j.ia;
	}

	public Case.Etat getJoueurID() {
		return joueurID;
	}

	public void setJoueurID(Case.Etat joueurID) {
		this.joueurID = joueurID;
	}

	public boolean isJoueurHumain() {
		return joueurHumain;
	}

	public void setJoueurHumain(boolean joueurHumain) {
		this.joueurHumain = joueurHumain;
	}
	
	public Case.Etat opposant(){
		if (joueurID == Case.Etat.joueur1)
			return Case.Etat.joueur2;
		else
			return Case.Etat.joueur1;
	}
	
	public static Joueur recupereJoueurOpposant(Joueur joueurCourant, Joueur J1, Joueur J2, boolean traceChangeJoueur){
		if(joueurCourant.getJoueurID() == J1.getJoueurID()){
			joueurCourant = J2;
			if(traceChangeJoueur)
				System.out.println("Au tour du joueur 2 ! ");
		}
		else {
			joueurCourant = J1;
			if(traceChangeJoueur)
				System.out.println("Au tour du joueur 1 ! ");
		}
		
		return joueurCourant;
	}
	
	public void setScore(int nbPionsManges) {
		score -= nbPionsManges;
	}
	
	public void chargerScore(int score) {
		this.score = score;
	}
	
	public int getScore() {
		return score;
	}
	
	public boolean scoreNul() {
		return (score == 0);
	}
	
	public String getNom() {
		return nom;
	}
	
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public Coup jouer(Terrain t) {
		return ia.jouerIA(t);
	}
	
	public boolean IaContinue(){
		return ia.isTourEnCours();
	}
	
	public void chargerIa(IntelligenceArtificielle.difficulteIA typeIA, Joueur adversaire, Terrain t) {
		ia = new IntelligenceArtificielle(typeIA, this, adversaire, t);
	}
	
	public void viderIa() {
		ia = null;
	}
}
