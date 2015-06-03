package ia;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import java.util.Iterator;

import modele.*;
import moteur.*;

public class IntelligenceArtificielle implements Runnable {
	public enum difficulteIA{
		facile,
		normal,
		difficile
	}
	
	/* Variables temporaires pour tests */
	public double tempsExe = 0; 
	public double tempsMax = 0;
	public int nbExe = 0; 		
	public double profondeurExploree = 0;
	public double nbExplorations = 0;
	public boolean victoire = false;
	/* 									*/
	
	private difficulteIA niveauDifficulte;
	private Joueur joueurIA, joueurAdversaire;
	private Moteur moteur;
	private TourDeJeu tourDeJeuCourant;
	private boolean tourEnCours;
	private static final int MAX = 1000;
	private static final int MIN = -1000;
	
	public IntelligenceArtificielle(difficulteIA niveauDifficulte, Joueur joueurIA, Joueur joueurAdversaire, Moteur m){
		this.setNiveauDifficulte(niveauDifficulte);
		this.setJoueurIA(joueurIA);
		this.setJoueurAdv(joueurAdversaire);
		this.setMoteur(m); 
		this.setTourDeJeuCourant(new TourDeJeu()); // Ces deux variables servent pour la difficulté 
		this.setTourEnCours(false); 						   // intermédiaire (normal) et difficile qui renvoyent une 
	}														   // liste de points


	public Coup jouerIA(){
		Coup coupSolution = new Coup(new Point(-1,-1), new Point(-1,-1));
		Coup coupTemp;
		ArrayList<Coup> listeCoupsDuTour;
		
		
		switch(this.getNiveauDifficulte()){
			case facile :
				if(!this.tourEnCours){
					this.setTourDeJeuCourant(this.coupFacile());
					this.setTourEnCours(true);
				}
			break;
			
			case normal :
				if(!this.tourEnCours){
					this.setTourDeJeuCourant(this.coupNormal());
					this.setTourEnCours(true);
				}	
			break;
			
			case difficile :
				//coupSolution = this.coupDifficile();
			break;
			
			default : // difficulté normale
				if(!this.tourEnCours){
					this.setTourDeJeuCourant(this.coupNormal());
					this.setTourEnCours(true);
				}	
			break;
		}
	//	Iterator<Coup> it = this.getTourDeJeuCourant().getListeCoups().iterator();
		/*System.out.println("\n\n ****\t Résultat \t**** \n\n");
		this.moteur.t.dessineTableauAvecIntersections();
		while(it.hasNext()){
			Coup coupT = it.next();
			Point pDep = coupT.getpDepart(), pArr = coupT.getpArrivee();
			this.moteur.t.deplacement(pDep, pArr, this.joueurIA, new ArrayList<Point>());
			this.moteur.t.manger(this.joueurIA, this.moteur.t.recupereDirection(pDep, pArr), pDep, pArr, coupT.getChoixPrise());
			this.moteur.t.dessineTableauAvecIntersections();
		}
		*/
		
		listeCoupsDuTour = this.getTourDeJeuCourant().getListeCoups();
		
		if(!listeCoupsDuTour.isEmpty()){
			coupTemp = listeCoupsDuTour.get(0);
			coupSolution = coupTemp.clone();
			listeCoupsDuTour.remove(coupTemp);
			this.getTourDeJeuCourant().setListeCoups(listeCoupsDuTour);
		}

		if(listeCoupsDuTour.isEmpty()) // Si la liste est vide on a terminé le tour
			this.setTourEnCours(false);
		
		
		return coupSolution;
	}
	
	/*
	 * Applique l'algorithme permettant à l'ordinateur de jouer un coup en difficulté "facile"
	 * Paramètres : 	listePredecesseurs -> contient la liste des prédécesseurs et donc des points que l'on a pas le droit
	 * 					de retraverser pour ce tour
	 * 					pDep 			   -> dans le cadre d'un tour qui se prolonge (prises multiples) on indique le 
	 * 					point de départ qui est le point d'arrivée du coup précédent
	 */
	private TourDeJeu coupFacile(){
		ArrayList<TourDeJeu> listeToursJouables = new ArrayList<TourDeJeu>();
		Iterator<TourDeJeu> it;
		TourDeJeu tourSolution = new TourDeJeu(), tourTemp;
		int max = 0;
		Random rand = new Random();
		
		// Récupération de tous les tours jouables pour le terrain et le joueur courant
		listeToursJouables = getToursJouables(this.moteur.t.copie(),this.getJoueurIA());
	

		if(listeToursJouables.size() > 0)
			tourSolution = listeToursJouables.get(rand.nextInt(listeToursJouables.size()));

		/*
		it = listeToursJouables.iterator();
		
		while(it.hasNext()){
			tourTemp = it.next().clone();
			
			if(tourTemp.getValeurResultat() > max){
				max = tourTemp.getValeurResultat();
				tourSolution = tourTemp;
			}
		}
		*/
		return tourSolution;
	}
	
	public static Terrain.ChoixPrise choixPriseIAFacile(){
		Random rand = new Random();

		if(rand.nextInt(2) == 1)
			return Terrain.ChoixPrise.parAspiration;
		return Terrain.ChoixPrise.parPercussion;
	}
	
	/*
	 * Applique l'algorithme permettant à l'ordinateur de jouer un coup en difficulté "normal"
	 */
	private TourDeJeu coupNormal(){
		TourDeJeu tourSolution = new TourDeJeu();
		int profondeur = 5;
		int iterateurProf = 0;
		
		// ALPHA BETA
		tourSolution = alphaBeta(profondeur, iterateurProf); // simule x-profondeur tours
											  				 // exemple : profondeur = 3
											  				 // on va simuler un tour jCourant puis un tour jAdv puis 
		return tourSolution;				 				 // de nouveau un tour jCourant	
	}
	
	/*
	 * Application de l'algorithme alpha beta
	 */
	private TourDeJeu alphaBeta(int profondeur, int iterateurProf){
		ArrayList<TourDeJeu> listeToursJouables = new ArrayList<TourDeJeu>();
		Iterator<TourDeJeu> it;
		TourDeJeu tourCourant, tourSolution = new TourDeJeu();
		Random rand = new Random();
		int valMax = MIN, valTemp, nbPionsManges;
		Integer alpha = new Integer(MIN), beta = new Integer(MAX);
		
		double tempsDepart = (double) System.currentTimeMillis(), temp; // pour tests
		
		// Récupération de tous les tours jouables pour le terrain et le joueur courant
		listeToursJouables = getToursJouables(this.moteur.t,this.getJoueurIA());
		
		// Adaptation dynamique de la profondeur explorée
		/*
		if(listeToursJouables.size() >= 10 && profondeur > 1)
			profondeur--;
		else if(listeToursJouables.size() >= 20 && profondeur > 2)
			profondeur -= 2;
		else if(listeToursJouables.size() >= 30 && profondeur > 3)
			profondeur -= 3;
		 */
		
		
		if(listeToursJouables.size() > 0)
			tourSolution = listeToursJouables.get(0);
		
		it = listeToursJouables.iterator();
		
		
		while(it.hasNext()){
		
			tourCourant = (TourDeJeu) it.next().clone();
			
			nbPionsManges = tourCourant.getValeurResultat();
			
			valTemp = nbPionsManges + min(profondeur-1, alpha, beta, tourCourant.getTerrainFinal(), iterateurProf+1);
			
			if(valTemp > valMax){
				valMax = valTemp;
				tourSolution = (TourDeJeu) tourCourant.clone();
			}
			else if(valTemp == valMax){ // Choix randomisé si des solutions ont un résultat similaire
				if(rand.nextInt(2) == 1){
					tourSolution = (TourDeJeu) tourCourant.clone();
				}
			}
			
		}
		
		this.nbExe++;
		
		temp = (double) (System.currentTimeMillis() - tempsDepart);
		this.tempsExe += temp;
		
		tempsMax = (long) Math.max(temp, this.tempsMax);
		
		return tourSolution;
	}
	
	/*
	 * Min :
	 */
	private int min(int profondeur, Integer alpha, Integer beta, Terrain terrainCourant, int iterateurProf){
		ArrayList<TourDeJeu> listeToursJouables = new ArrayList<TourDeJeu>();
		Iterator<TourDeJeu> it;
		TourDeJeu tourCourant;
		int valTemp = MAX, valRes = MAX;
		
		if(profondeur == 0){
			this.nbExplorations++;
			this.profondeurExploree += iterateurProf;
			return 0;
		}
		
		// Récupération de tous les tours jouables pour le terrain et le joueur courant
		listeToursJouables = getToursJouables(terrainCourant, this.getJoueurAdv());
		
		if(listeToursJouables.isEmpty()) // Si il n'y a plus de tours possibles l'IA a perdu (ou plutôt on a gagné)
			return MAX;

		
		// Réduction dynamique de la profondeur explorée
		/*
		if(listeToursJouables.size() >= 15 && profondeur > 1)
			profondeur--;
		else if(listeToursJouables.size() >= 25 && profondeur > 2)
			profondeur -= 2;
		else if(listeToursJouables.size() >= 30 && profondeur > 3)
			profondeur -= 3;
		*/

		it = listeToursJouables.iterator();
		
		while(it.hasNext()){
			tourCourant = (TourDeJeu) it.next().clone();

			valTemp = -(tourCourant.getValeurResultat()); // nombre de pions perdus (mangés par l'adversaire) en négatif

			valTemp += max(profondeur-1, alpha, beta, tourCourant.getTerrainFinal(), iterateurProf+1);
			
			if(valTemp < valRes)
				valRes = valTemp;
			
			
			if(alpha >= valTemp) // élagage
				return valTemp;
			
			
			beta = Math.min(beta,valRes);	
		}

		return valRes;
	}
	
	/*
	 * Max :
	 */
	private int max(int profondeur, Integer alpha, Integer beta,  Terrain terrainCourant, int iterateurProf){
		ArrayList<TourDeJeu> listeToursJouables = new ArrayList<TourDeJeu>();
		Iterator<TourDeJeu> it;
		TourDeJeu tourCourant;
		int valTemp = MIN, valRes = MIN;
		
		if(profondeur == 0){
			this.nbExplorations++;
			this.profondeurExploree += iterateurProf;
			return 0;
		}
		
		// Récupération de tous les tours jouables pour le terrain et le joueur courant
		listeToursJouables = getToursJouables(terrainCourant, this.getJoueurIA());	
		
		if(listeToursJouables.isEmpty()) // Si il n'y a plus de tours possibles on a perdu
			return MIN;
		
		// Réduction dynamique de la profondeur explorée
		/*
		if(listeToursJouables.size() >= 10 && profondeur > 1)
			profondeur--;
		else if(listeToursJouables.size() >= 20 && profondeur > 2)
			profondeur -= 2;
		else if(listeToursJouables.size() >= 30 && profondeur > 3)
			profondeur -= 3;
		*/
		
		it = listeToursJouables.iterator();
		
		while(it.hasNext()){
			tourCourant = (TourDeJeu) it.next().clone();
			
			valTemp = tourCourant.getValeurResultat();
			valTemp += min(profondeur-1, alpha, beta, tourCourant.getTerrainFinal(), iterateurProf+1);
			
			if(valTemp > valRes)
				valRes = valTemp;
			
			if(valTemp >= beta)  // élagage 
				return valTemp;
			
			alpha = Math.max(alpha,valRes);
		}

		return valRes;
	}
	
	
	/*
	 * getToursJouables, renvoie une liste de liste de tous les tours jouables pour le joueur à un instant t
	 * paramètres : - ArrayList<Point> listePointsDeDepart
	 * 				- boolean priseObligatoire : permet un le traitement nécessaire pour les prises obligatoires
	 */
	private ArrayList<TourDeJeu> getToursJouables(Terrain terrainCourant, Joueur joueurCourant){
		Point pDepartCourant, pArriveeCourante;
		ArrayList<Point> listePointsDeDepart, listeCoupsObligatoires, listeVide = new ArrayList<Point>();
		ArrayList<TourDeJeu> listeToursJouables = new ArrayList<TourDeJeu>(), listeToursTemp, listeToursVide = new ArrayList<TourDeJeu>();
		TourDeJeu tourTemp;
		boolean priseObligatoire = false;
		
		Iterator<Point> itPointsDepart, itPointsArrivee;
		Iterator<TourDeJeu> itToursTemp;
		
		Terrain cloneTerrain = terrainCourant.copie();
		
		// Récupération de la liste des points de Départ possibles
		listeCoupsObligatoires = cloneTerrain.couplibre(joueurCourant.getJoueurID()); // On regarde si on a des coups obligatoires
		
		if(listeCoupsObligatoires.isEmpty()) // DEBUT DE TOUR - Sans coup obligatoire (mouvement libre n'amenant aucune prise)
			listePointsDeDepart = this.moteur.listePionsJouables(joueurCourant, cloneTerrain);
		else{							 // DEBUT DE TOUR - Avec coup/prise obligatoire
			listePointsDeDepart = listeCoupsObligatoires;
			priseObligatoire = true;
		}
		
		itPointsDepart = listePointsDeDepart.iterator();
		
		// Pour tous les points de départ possibles
		while(itPointsDepart.hasNext()){
			listeToursTemp = listeToursVide; // On initialise une nouvelle liste de tours de jeu
			pDepartCourant = (Point) itPointsDepart.next().clone();
			
			itPointsArrivee = this.moteur.deplacementPossible(pDepartCourant, listeVide, cloneTerrain).iterator();
				
			if(priseObligatoire){ // Si on a des prises obligatoires il faut trier les solutions disponibles
				// pour tous les successeurs du point de départ courant
				while(itPointsArrivee.hasNext()){
					pArriveeCourante = (Point) itPointsArrivee.next().clone();
					Terrain.Direction dir = cloneTerrain.recupereDirection(pDepartCourant, pArriveeCourante);
					if(cloneTerrain.estUnePriseAspiration(pDepartCourant, dir))
						getListeToursPourCoupDepart(listeToursTemp, new TourDeJeu(), new Coup(pDepartCourant, pArriveeCourante, Terrain.ChoixPrise.parAspiration), cloneTerrain, listeVide, 0, joueurCourant);
					if(cloneTerrain.estUnePrisePercussion(pDepartCourant, dir))
						getListeToursPourCoupDepart(listeToursTemp, new TourDeJeu(), new Coup(pDepartCourant, pArriveeCourante, Terrain.ChoixPrise.parPercussion), cloneTerrain, listeVide, 0, joueurCourant);
				}
			}
			else{	// Les tours ne sont ici constitués que d'un seul coup de gain 0
				while(itPointsArrivee.hasNext()){
					pArriveeCourante = (Point) itPointsArrivee.next().clone();
					if(cloneTerrain.deplacement(pDepartCourant, pArriveeCourante, joueurCourant, listeVide) == 0){
						tourTemp = new TourDeJeu(new Coup(pDepartCourant,pArriveeCourante));
						tourTemp.setTerrainFinal(cloneTerrain);
						listeToursTemp.add(tourTemp);
						cloneTerrain.deplacement(pArriveeCourante, pDepartCourant, joueurCourant, listeVide);
					}
				}
			}
			
			itToursTemp = listeToursTemp.iterator();
			
			while(itToursTemp.hasNext()){ // on décompose la liste pour la recomposer
				tourTemp = (TourDeJeu) itToursTemp.next().clone();
				listeToursJouables.add(tourTemp.clone());
			}
		}
		
		return listeToursJouables;
	}

	/*
	 * getListeToursPourCoupDepart, renvoie tous les tours de jeu possible pour le coup de départ donné en paramètre
	 *				   cette méthode n'est appelée que lors d'un tour avec plusieurs prises
	 */
	public void getListeToursPourCoupDepart(ArrayList<TourDeJeu> listeToursComplets, TourDeJeu tourTemp, Coup coupDeDepart, Terrain cloneTerrain, ArrayList<Point> listePredecesseurs, int nbPionsManges, Joueur joueurCourant){
		Iterator<Point> itPointsArriveeSuivants;
		Terrain terrainCopie;
		Point pDep, pArr, pArrTemp;

		
		pDep = coupDeDepart.getpDepart();
		pArr = coupDeDepart.getpArrivee();
	
		terrainCopie = cloneTerrain.copie();
		
		terrainCopie.deplacement(pDep, pArr, joueurCourant, listePredecesseurs);

		nbPionsManges += terrainCopie.manger(joueurCourant, terrainCopie.recupereDirection(pDep, pArr), pDep, pArr,coupDeDepart.getChoixPrise()).size();
		listePredecesseurs.add(pDep);
		
		
		// Il y a aprés chaque prise possibilité de s'arrêter
		tourTemp.addCoup(coupDeDepart);
		tourTemp.setValeurResultat(nbPionsManges);
		tourTemp.setTerrainFinal(terrainCopie);
		listeToursComplets.add(tourTemp.clone());
				
		pDep = pArr;
		
		// On récupère les successeurs possibles à la position d'arrivée du coup joué
		itPointsArriveeSuivants = this.moteur.deplacementPossible(pDep, listePredecesseurs, terrainCopie).iterator();

		while(itPointsArriveeSuivants.hasNext()){
			pArrTemp = (Point) itPointsArriveeSuivants.next().clone();
			Terrain.Direction dir = terrainCopie.recupereDirection(pDep, pArrTemp);
			if(terrainCopie.estUnePriseAspiration(pDep, dir))
				getListeToursPourCoupDepart(listeToursComplets, tourTemp.clone(), new Coup(pDep, pArrTemp, Terrain.ChoixPrise.parAspiration), terrainCopie, listePredecesseurs, nbPionsManges, joueurCourant);
			if(terrainCopie.estUnePrisePercussion(pDep, dir))
				getListeToursPourCoupDepart(listeToursComplets, tourTemp.clone(), new Coup(pDep, pArrTemp, Terrain.ChoixPrise.parPercussion), terrainCopie, listePredecesseurs, nbPionsManges, joueurCourant);
		}
	}
	
	/*
	 * Applique l'algorithme permettant à l'ordinateur de jouer un coup en difficulté "difficile"
	 */
	private Coup coupDifficile(){
		Coup pSolution = null;
		
		return pSolution;
	}
	
	public difficulteIA getNiveauDifficulte() {
		return niveauDifficulte;
	}

	public void setNiveauDifficulte(difficulteIA niveauDifficulte) {
		this.niveauDifficulte = niveauDifficulte;
	}

	public Joueur getJoueurIA() {
		return joueurIA;
	}

	public void setJoueurIA(Joueur joueurIA) {
		this.joueurIA = joueurIA;
	}

	public Joueur getJoueurAdv(){
		return joueurAdversaire;
	}

	private void setJoueurAdv(Joueur joueurAdversaire) {
		this.joueurAdversaire = joueurAdversaire;
	}

	public Moteur getMoteur() {
		return moteur;
	}

	public void setMoteur(Moteur m) {
		this.moteur = m;
	}

	public boolean isTourEnCours() {
		return tourEnCours;
	}

	public void setTourEnCours(boolean tourEnCours) {
		this.tourEnCours = tourEnCours;
	}

	private void setTourDeJeuCourant(TourDeJeu tourDeJeu) {
		this.tourDeJeuCourant = tourDeJeu;
	}
	
	private TourDeJeu getTourDeJeuCourant(){
		return this.tourDeJeuCourant;
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		switch(this.getNiveauDifficulte()){
			case facile :
				if(!this.tourEnCours){
					this.setTourDeJeuCourant(this.coupFacile());
					this.setTourEnCours(true);
				}
			break;
			
			case normal :
				if(!this.tourEnCours){
					this.setTourDeJeuCourant(this.coupNormal());
					this.setTourEnCours(true);
				}	
			break;
			
			case difficile :
				//coupSolution = this.coupDifficile();
			break;
			
			default : // difficulté normale
				if(!this.tourEnCours){
					this.setTourDeJeuCourant(this.coupNormal());
					this.setTourEnCours(true);
				}	
			break;
		}
	}
}
