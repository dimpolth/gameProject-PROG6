package modele;
import java.io.Serializable;

import ia.*;
import ia.IntelligenceArtificielle.difficulteIA;
/**
 *Classe contenant tout ce qui définit un joueur. 
 *Instancie l'IA.
 */
public class Joueur implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Définit si le joueur est un humain ou un IA.
	 */
	public enum typeJoueur{
		/**
		 * Le joueur est un humain.
		 */
		humain,
		/**
		 * Le joueur est une IA.
		 */
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
	 * @param joueurID Occupation sur le Terrain (j1 ou j2).
	 * @param joueur Type de joueur (humain).
	 * @param nom Nom du joueur donné par les paramètres.
	 */
	public Joueur(Case.Etat joueurID, typeJoueur joueur, String nom){
		this.setJoueurID(joueurID);
		resetScore();
		this.nom = nom;
		this.setJoueurHumain(true);
		ia = null;
	}
	
	/**
	 * Constructeur pour joueur ordinateur
	 * @param joueurID Occupation sur le Terrain (j1 ou j2).
	 * @param joueur Type de joueur (ordinateur).
	 * @param niveau Niveau de l'IA.
	 * @param adversaire sJoueur adverse.
	 * @param t Terrain courant transmis par le Moteur.
	 */
	public Joueur(Case.Etat joueurID, typeJoueur joueur, IntelligenceArtificielle.difficulteIA niveau, Joueur adversaire, Terrain t){
		this.setJoueurID(joueurID);
		resetScore();
		this.nom = "Joueur";
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
	 * @param j Jouer à copier.
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
	 * @return Type d'occupation sur le Terrain(joueur1 ou joueur2).
	 */
	public Case.Etat getJoueurID() {
		return joueurID;
	}

	/**
	 * Permet de modifier les pions du joueur.
	 * @param joueurID Type d'occupation sur le Terrain (joueur1 ou joueur2).
	 */
	public void setJoueurID(Case.Etat joueurID) {
		this.joueurID = joueurID;
	}

	/**
	 * Permet de savoir si le joueur est humain.
	 * @return Vrai si le joueur est humain, faux sinon.
	 */
	public boolean isJoueurHumain() {
		return joueurHumain;
	}

	/**
	 * Modifie le booléen qui identifie le joueur comme étant humain.
	 * @param joueurHumain Vrai si le joueur doit être humain, faux sinon.
	 */
	public void setJoueurHumain(boolean joueurHumain) {
		this.joueurHumain = joueurHumain;
	}
	
	/**
	 * Permet de connaître le type d'occupation de case de l'adversaire.
	 * @return Type d'occupation sur le terrain de l'adversaire.
	 */
	public Case.Etat opposant(){
		if (joueurID == Case.Etat.joueur1)
			return Case.Etat.joueur2;
		else
			return Case.Etat.joueur1;
	}
	
	/**
	 * Permet d'obtenir la référence de l'adversaire du joueur courant.
	 * @param joueurCourant Le joueur qui est entrain de jouer.
	 * @param J1 Le joueur 1
	 * @param J2 Le joueur 2
	 * @param traceChangeJoueur Sert aux tests de l'IA.
	 * @return Le joueur adverse.
	 */
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
	
	/**
	 * Remet le score du joueur à son état initial.
	 */
	public void resetScore(){
		score = 22;
	}
	
	/**
	 * Met à jour le score du joueur. Utilisé lors de la prise de pions par l'adversaire.
	 * @param nbPionsManges Nombre de pions mangés par l'adversaire.
	 */
	public void setScore(int nbPionsManges) {
		score -= nbPionsManges;
	}
	
	/**
	 * Charge un score sur le joueur. Utilisé lors d'un chargement de partie.
	 * @param score Score à attribuer au joueur.
	 */
	public void chargerScore(int score) {
		this.score = score;
	}
	
	/**
	 * Permet d'obtenir le score du joueur.
	 * @return Le score du joueur.
	 */
	public int getScore() {
		return score;
	}
	
	/**
	 * Permet de savoir si le score du joueur a atteint zéro.
	 * Utilisé pour savoir si le joueur à perdu la partie.
	 * @return Vrai si le score est à zéro. Faux sinon.
	 */
	public boolean scoreNul() {
		return (score == 0);
	}
	
	/**
	 * Permet d'obtenir le nom du joueur.
	 * @return Le nom du joueur.
	 */
	public String getNom() {
		String retourner="Inconnu";
		if(isJoueurHumain())
			retourner= nom;
		else{
			if(ia != null){				
				if(ia.getNiveauDifficulte() == difficulteIA.facile)
					retourner= "IA facile";
				else if(ia.getNiveauDifficulte() == difficulteIA.normal)
					retourner= "IA Moyenne";
				else if(ia.getNiveauDifficulte() == difficulteIA.difficile)				
					retourner= "IA difficile";				
				else
					retourner= "IA inconnue";
			}
		}
		return retourner;
		
	}
	
	/**
	 * Modifie le nom du joueur.
	 * @param nom Nom à attribuer au joueur.
	 */
	public void setNom(String nom) {		
		this.nom = nom;
	}
	
	/**
	 * Fait jouer un coup à l'IA.
	 * @return Le coup joué par l'IA.
	 */
	public Coup jouer() {
		return ia.jouerIA();
	}
	
	/**
	 * Permet de savoir si l'IA a encore un coup à jouer.
	 * @return Vrai si l'IA continue son tour, faux sinon.
	 */
	public boolean IaContinue(){
		return ia.isTourEnCours();
	}
	
	/**
	 * Charge une IA sur un joueur. Utilisé lors du chargement d'une partie.
	 * @param typeIA Difficulté de l'IA.
	 * @param adversaire Joueur qui affronte l'IA.
	 * @param t Le terrain de jeu actuel.
	 */
	public void chargerIa(IntelligenceArtificielle.difficulteIA typeIA, Joueur adversaire, Terrain t) {
		ia = new IntelligenceArtificielle(typeIA, this, adversaire, t);
	}
	
	/**
	 * Permet d'arrêter l'IA. Utilisé si le joueur passe d'humain à IA.
	 */
	public void viderIa() {
		ia = null;
	}
	
	public IntelligenceArtificielle getIA(){
		return ia;
	}
	
}
