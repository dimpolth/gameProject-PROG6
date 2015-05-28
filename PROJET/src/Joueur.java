
public class Joueur {
	
	public enum typeJoueur{
		humain,
		ordinateur;
	}
	
	private Case.Etat joueurID;
	private boolean joueurHumain;
	private int score;
	private String nom;
	
	public Joueur(Case.Etat joueurID, typeJoueur joueur, String nom){
		this.setJoueurID(joueurID);
		score = 22;
		this.nom = nom;
		if(joueur == typeJoueur.humain)
			this.setJoueurHumain(true);
		else 
			this.setJoueurHumain(false);
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
}
