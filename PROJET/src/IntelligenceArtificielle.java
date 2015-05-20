import java.awt.Point;


public class IntelligenceArtificielle {
	public enum difficulteIA{
		facile,
		normal,
		difficile
	}
	
	private difficulteIA niveauDifficulte;
	
	public IntelligenceArtificielle(difficulteIA niveauDifficulte){
		this.setNiveauDifficulte(niveauDifficulte);
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
}
