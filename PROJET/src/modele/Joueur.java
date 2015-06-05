package modele;
import java.io.Serializable;

import ia.*;
import moteur.*;
/**
 *Classe contenant tout ce qui définit un joueur. 
 *Instancie l'IA.
 */
public class Joueur implements Serializable {
	
	/**
	 * Définit si le joueur est un humain ou un IA.
	 */
	public enum typeJoueur{
		humain,
		ordinateur;
	}
	
	/**
	 * Associe le joueur avec un type d'occupation sur le Terrain.
	 */
	private Case.Etat joueurID;
	/**
	 * Vrai si le joueur est un humain. Faux sinon.
	 */
	private boolean joueurHumain;
	/**
	 * Score du joueur. Définit sur [22,0].
	 */
	private int score;
	/**
	 * Nom du joueur.
	 */
	private String nom;
	/**
	 * Instance de l'IA si le joueur en est une.
	 */
	private IntelligenceArtificielle ia;
	
	/**
	 * Constructeur pour joueur humain.
	 * @param joueurID
	 * Occupation sur le Terrain (j1 ou j2).
	 * @param joueur
	 * Type de joueur (humain).
	 * @param nom
	 * Nom du joueur donné par les paramètres.
	 */
	public Joueur(Case.Etat joueurID, typeJoueur joueur, String nom){
		this.setJoueurID(joueurID);
		score = 22;
		this.nom = nom;
		this.setJoueurHumain(true);
		ia = null;
	}
	
	/**
	 * Constructeur pour joueur ordinateur
	 * @param joueurID
	 * Occupation sur le Terrain (j1 ou j2).
	 * @param joueur
	 * Type de joueur (ordinateur).
	 * @param niveau
	 * Niveau de l'IA.
	 * @param adversaire
	 * Joueur adverse.
	 * @param t
	 * Terrain courant transmis par le Moteur.
	 */
	public Joueur(Case.Etat joueurID, typeJoueur joueur, IntelligenceArtificielle.difficulteIA niveau, Joueur adversaire, Terrain t){
		this.setJoueurID(joueurID);
		score = 22;
		this.nom = "Ordinateur";
		this.setJoueurHumain(false);
		ia = new IntelligenceArtificielle(niveau, this, adversaire, t);
	}
	
	/**
	 * Constructeur par défaut.
	 */
	public Joueur() {
		
	}
	
	/**
	 * Constructeur par copie.
	 * @param j
	 * Jouer à copier.
	 */
	public Joueur(Joueur j) {
		setJoueurID(j.getJoueurID());
		score = j.getScore();
		joueurHumain = j.isJoueurHumain();
		nom = j.getNom();
		ia = j.ia;
	}

	/**
	 * Permet de connaître quels sont les pions du joueur sur le terrain.
	 * @return
	 * Type d'occupation sur le Terrain(joueur1 ou joueur2).
	 */
	public Case.Etat getJoueurID() {
		return joueurID;
	}

	/**
	 * Permet de modifier les pions du joueur.
	 * @param joueurID
	 * Type d'occupation sur le Terrain (joueur1 ou joueur2).
	 */
	public void setJoueurID(Case.Etat joueurID) {
		this.joueurID = joueurID;
	}

	/**
	 * Permet de savoir si le joueur est humain.
	 * @return
	 * Vrai si le joueur est humain, faux sinon.
	 */
	public boolean isJoueurHumain() {
		return joueurHumain;
	}

	/**
	 * Modifie le booléen qui identifie le joueur comme étant humain.
	 * @param joueurHumain
	 * Vria si le joueur doit être humain, faux sinon.
	 */
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
				;//System.out.println("Au tour du joueur 2 ! ");
		}
		else {
			joueurCourant = J1;
			if(traceChangeJoueur)
				;//System.out.println("Au tour du joueur 1 ! ");
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
	
	public Coup jouer() {
		return ia.jouerIA();
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
