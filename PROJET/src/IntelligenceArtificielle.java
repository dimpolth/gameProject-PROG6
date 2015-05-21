import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import java.util.Iterator;


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
	 * Paramètres : 	listePredecesseurs -> contient la liste des prédécesseurs et donc des points que l'on a pas le droit
	 * 					de retraverser pour ce tour
	 * 					pDep 			   -> dans le cadre d'un tour qui se prolonge (prises multiples) on indique le 
	 * 					point de départ qui est le point d'arrivée du coup précédent
	 */
	private Coup coupFacile(ArrayList<Point> listePredecesseurs, Point pDep){
		
		Coup coupSolution;
		Point pDepart = null, pArrivee, pArriveeTemp;
		ArrayList<Point> listePionsJouables, listeSuccesseursPionsJouables, listeSolution = new ArrayList<Point>();
		Random rand = new Random();
		Iterator<Point> it;

		if(pDep != null)
			pDepart = pDep;
		//***** 	Sélection du point de départ et d'arrivée 
		
		if(pDep == null){ // DEBUT DE TOUR - Ce cas est présent lors du début de tour d'une IA aucun point de départ imposé
			listePionsJouables = this.moteur.listePionsJouables(this.joueurIA);
			
			do{
				pDepart = listePionsJouables.get(rand.nextInt(listePionsJouables.size()));
				listeSolution = this.moteur.deplacementPossible(pDepart, listePredecesseurs);
			}while(listeSolution.size() <= 0);
		}
		
					 // MILIEU/FIN DE TOUR - Ce cas est donc lors d'un xième coup d'un tour (x > 1) on a un point de départ 
		else {		 //	imposé qui est le point d'arrivée précédent
			listeSuccesseursPionsJouables = this.moteur.deplacementPossible(pDepart, listePredecesseurs);
			it = listeSuccesseursPionsJouables.iterator();
			
			while(it.hasNext()){
				pArriveeTemp = (Point) it.next().clone();
				Terrain.Direction dir = this.moteur.t.recupereDirection(pDepart, pArriveeTemp);
				if(this.moteur.t.estUnePriseAspiration(pDepart, dir) || this.moteur.t.estUnePrisePercussion(pDepart, dir))
					listeSolution.add(pArriveeTemp);
			}
		}
		
		// Dans le cas ci-dessous on a aucune coup jouable on renvoie donc en pArrivee (-1;-1)
		if(listeSolution.size() == 0)
			pArrivee = new Point(-1,-1);
		else
			pArrivee = listeSolution.get(rand.nextInt(listeSolution.size()));
		//***** 
		
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
