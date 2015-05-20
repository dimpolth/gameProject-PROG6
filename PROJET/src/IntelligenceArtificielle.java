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

	public Coup jouerIA(ArrayList<Point> listePredecesseurs, Point pDepart){
		
		Coup coupSolution;
		
		switch(this.getNiveauDifficulte()){
			case facile :
				coupSolution = this.coupFacile(listePredecesseurs,pDepart);
			break;
			
			case normal :
				coupSolution = this.coupNormal(listePredecesseurs,pDepart);
			break;
			
			case difficile :
				coupSolution = this.coupDifficile(listePredecesseurs,pDepart);
			break;
			
			default :
				coupSolution = this.coupNormal(listePredecesseurs,pDepart);
			break;
		}
		
		return coupSolution;
	}
	
	/*
	 * Applique l'algorithme permettant à l'ordinateur de jouer un coup en difficulté "facile"
	 */
	private Coup coupFacile(ArrayList<Point> listePredecesseurs, Point pDep){
		Coup coupSolution;
		Point pDepart = null, pArrivee;
		
		if(pDep != null)
			pDepart = pDep;
		
		ArrayList<Point> listePionsJouables, listeSuccesseursPionsJouables;
		Random rand = new Random();
		
		listePionsJouables = this.moteur.listePionsJouables(this.joueurIA);
		
		/*
		 * Sélection du point de départ et d'arrivée au hasard
		 */
		
		if(pDep == null){
			do{
			pDepart = listePionsJouables.get(rand.nextInt(listePionsJouables.size()));
			listeSuccesseursPionsJouables = this.moteur.deplacementPossible(pDepart, listePredecesseurs);
			}while(listeSuccesseursPionsJouables.size() <= 0);
		}
		else
			listeSuccesseursPionsJouables = this.moteur.deplacementPossible(pDepart, listePredecesseurs);
		
		// Dans le cas ci-dessous on a aucune coup jouable on renvoie donc en pArrivee (-1;-1)
		if(listeSuccesseursPionsJouables.size() == 0)
			pArrivee = new Point(-1,-1);
			
		pArrivee = listeSuccesseursPionsJouables.get(rand.nextInt(listeSuccesseursPionsJouables.size()));
		
		coupSolution = new Coup(pDepart,pArrivee);
		
		System.out.println("IA joue : Depart("+ pDepart.x +";"+ pDepart.y +") -> Arrivee("+ pArrivee.x +";"+ pArrivee.y +")");
		
		return coupSolution;
	}
	
	/*
	 * Applique l'algorithme permettant à l'ordinateur de jouer un coup en difficulté "normal"
	 */
	private Coup coupNormal(ArrayList<Point> listePredecesseurs, Point pDep){
		Coup pSolution = null;
		
		return pSolution;
	}
	
	/*
	 * Applique l'algorithme permettant à l'ordinateur de jouer un coup en difficulté "difficile"
	 */
	private Coup coupDifficile(ArrayList<Point> listePredecesseurs, Point pDep){
		Coup pSolution = null;
		
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
