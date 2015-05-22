
public class Joueur {
	
	public enum typeJoueur{
		humain,
		ordinateur	
	}
	
	private Case.Etat joueurID;
	private boolean joueurHumain;
	
	public Joueur(Case.Etat joueurID, typeJoueur joueur){
		this.setJoueurID(joueurID);
		
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
}
