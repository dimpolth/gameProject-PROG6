import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;


public class IntelligenceArtificielle {
	public enum difficulteIA{
		facile,
		normal,
		difficile
	}
	
	private difficulteIA niveauDifficulte;
	private Case.Etat joueurIA;
	private Moteur moteur;
	
	public IntelligenceArtificielle(difficulteIA niveauDifficulte, Case.Etat joueurIA, Moteur m){
		this.setNiveauDifficulte(niveauDifficulte);
		this.setJoueurIA(joueurIA);
		this.setMoteur(m);
	}

	public Point jouerIA(){
		
		Point pSolution;
		
		switch(this.getNiveauDifficulte()){
			case facile :
				pSolution = this.coupFacile();
			break;
			
			case normal :
				pSolution = this.coupNormal();
			break;
			
			case difficile :
				pSolution = this.coupDifficile();
			break;
			
			default :
				pSolution = this.coupNormal();
			break;
		}
		
		return pSolution;
	}
	
	/*
	 * Applique l'algorithme permettant à l'ordinateur de jouer un coup en difficulté "facile"
	 */
	private Point coupFacile(){
		Point pSolution = null;
		ArrayList<Point> listePionsJouables;
		Random rand = new Random();
		
		System.out.println(this.moteur.listePionsJouables(this.joueurIA));
		
		return pSolution;
	}
	
	/*
	 * Applique l'algorithme permettant à l'ordinateur de jouer un coup en difficulté "normal"
	 */
	private Point coupNormal(){
		Point pSolution = null;
		
		return pSolution;
	}
	
	/*
	 * Applique l'algorithme permettant à l'ordinateur de jouer un coup en difficulté "difficile"
	 */
	private Point coupDifficile(){
		Point pSolution = null;
		
		return pSolution;
	}
	
	public difficulteIA getNiveauDifficulte() {
		return niveauDifficulte;
	}

	public void setNiveauDifficulte(difficulteIA niveauDifficulte) {
		this.niveauDifficulte = niveauDifficulte;
	}

	public Case.Etat getJoueurIA() {
		return joueurIA;
	}

	public void setJoueurIA(Case.Etat joueurIA) {
		this.joueurIA = joueurIA;
	}

	public Moteur getMoteur() {
		return moteur;
	}

	public void setMoteur(Moteur m) {
		this.moteur = m;
	}
}
