package ia;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.Iterator;

import modele.*;
import moteur.*;

public class IntelligenceArtificielle implements Serializable {
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
	/*									*/

	
	private difficulteIA niveauDifficulte;
	private Joueur joueurIA, joueurAdversaire;
	private Terrain terrain;
	private TourDeJeu tourDeJeuCourant;
	private boolean tourEnCours;
	private final int coeffPionsManges = 2;
	private final int coeffPositionPions = 1;
	private final int MAX = 1000;
	private final int MIN = -1000;
	
	public IntelligenceArtificielle(difficulteIA niveauDifficulte, Joueur joueurIA, Joueur joueurAdversaire, Terrain t){
		this.setNiveauDifficulte(niveauDifficulte);
		this.setJoueurIA(joueurIA);
		this.setJoueurAdv(joueurAdversaire);
		terrain = t; 
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
				coupSolution = this.coupDifficile();
			break;
			
			default : // difficulté normale
				if(!this.tourEnCours){
					this.setTourDeJeuCourant(this.coupNormal());
					this.setTourEnCours(true);
				}	
			break;
		}
		
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
		listeToursJouables = getToursJouables(terrain.copie(),this.getJoueurIA());
	

		if(listeToursJouables.size() > 0)
			tourSolution = listeToursJouables.get(rand.nextInt(listeToursJouables.size()));

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
		int profondeur = 4;
		int iterateurProf = 0;
		
		// ALPHA BETA
		tourSolution = alphaBeta(profondeur, false, iterateurProf); // simule x-profondeur tours
											  				 // exemple : profondeur = 3
											  				 // on va simuler un tour jCourant puis un tour jAdv puis 
		return tourSolution;				 				 // de nouveau un tour jCourant	
	}
	
	/*
	 * Application de l'algorithme alpha beta
	 */
	private TourDeJeu alphaBeta(int profondeur, boolean profondeurDynamique, int iterateurProf){
		ArrayList<TourDeJeu> listeToursJouables = new ArrayList<TourDeJeu>();
		Iterator<TourDeJeu> it;
		TourDeJeu tourCourant, tourSolution = new TourDeJeu();
		Random rand = new Random();
		int valMax = MIN, valTemp, nbPionsManges, nbPionsRestantsJCourant, nbPionsRestantsJAdv, nbPionsRestantsTot;
		Integer alpha = new Integer(MIN), beta = new Integer(MAX);
		
		double tempsDepart = (double) System.currentTimeMillis(), temp; // pour tests
		
		// Récupération de tous les tours jouables pour le terrain et le joueur courant
		listeToursJouables = getToursJouables(terrain, this.getJoueurIA());
		
		if(profondeurDynamique){ 	// Adaptation dynamique de la profondeur explorée
			if(listeToursJouables.size() >= 10 && profondeur > 1)
				profondeur--;
			else if(listeToursJouables.size() >= 20 && profondeur > 2)
				profondeur -= 2;
			else if(listeToursJouables.size() >= 30 && profondeur > 3)
				profondeur -= 3;
		}
		
		nbPionsRestantsJCourant = joueurIA.getScore();
		nbPionsRestantsJAdv = joueurAdversaire.getScore();
		nbPionsRestantsTot = nbPionsRestantsJCourant + nbPionsRestantsJAdv;
	
		
		//System.out.println(nbPionsRestants);
		/*
		if((nbPionsRestantsTot <= 4) && (nbPionsRestantsJCourant <= nbPionsRestantsJAdv))
			profondeur += 3;

		else if(nbPionsRestantsTot <= 6 && (nbPionsRestantsJCourant <= nbPionsRestantsJAdv))
			profondeur += 2;
		
		else if(nbPionsRestantsTot <= 14 && (nbPionsRestantsJCourant < nbPionsRestantsJAdv))
			profondeur += 1;
		*/
		
		if(listeToursJouables.size() > 0)
			tourSolution = listeToursJouables.get(0);
		
		it = listeToursJouables.iterator();
		
		
		while(it.hasNext()){
		
			tourCourant = (TourDeJeu) it.next().clone();
			
			nbPionsManges = tourCourant.getValeurResultat() * this.coeffPionsManges;
			
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

		it = listeToursJouables.iterator();
		
		while(it.hasNext()){
			tourCourant = (TourDeJeu) it.next().clone();

			valTemp = -(tourCourant.getValeurResultat() * this.coeffPionsManges); // nombre de pions perdus (mangés par l'adversaire) en négatif

			valTemp += max(profondeur-1, alpha, beta, tourCourant.getTerrainFinal(), iterateurProf+1);

			valRes = Math.min(valTemp, valRes);
			
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
		
		it = listeToursJouables.iterator();
		
		while(it.hasNext()){
			tourCourant = (TourDeJeu) it.next().clone();
			
			valTemp = tourCourant.getValeurResultat() * this.coeffPionsManges;
			
			valTemp += min(profondeur-1, alpha, beta, tourCourant.getTerrainFinal(), iterateurProf+1);

			valRes = Math.max(valTemp, valRes);
			
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
		ArrayList<Point> listePointsDeDepart, listeCoupsObligatoires, listeVide = new ArrayList<Point>(), listePionsManges = new ArrayList<Point>();
		ArrayList<TourDeJeu> listeToursJouables = new ArrayList<TourDeJeu>(), listeToursTemp, listeToursVide = new ArrayList<TourDeJeu>();
		TourDeJeu tourTemp;
		Coup coupTemp = new Coup();
		
		boolean priseObligatoire = false;
		
		Iterator<Point> itPointsDepart, itPointsArrivee;
		Iterator<TourDeJeu> itToursTemp;
		
		Terrain cloneTerrain = terrainCourant.copie();
		
		// Récupération de la liste des points de Départ possibles
		listeCoupsObligatoires = cloneTerrain.couplibre(joueurCourant.getJoueurID()); // On regarde si on a des coups obligatoires
		
		if(listeCoupsObligatoires.isEmpty()) // DEBUT DE TOUR - Sans coup obligatoire (mouvement libre n'amenant aucune prise)
			listePointsDeDepart = terrain.listePionsJouables(joueurCourant, cloneTerrain);
		else{							 // DEBUT DE TOUR - Avec coup/prise obligatoire
			listePointsDeDepart = listeCoupsObligatoires;
			priseObligatoire = true;
		}
		
		itPointsDepart = listePointsDeDepart.iterator();
		
		// Pour tous les points de départ possibles
		while(itPointsDepart.hasNext()){
			listeToursTemp = listeToursVide; // On initialise une nouvelle liste de tours de jeu
			pDepartCourant = (Point) itPointsDepart.next().clone();
			coupTemp.setpDepart(pDepartCourant);
			
			itPointsArrivee = terrain.deplacementPossible(pDepartCourant, listeVide, cloneTerrain.getTableau()).iterator();
				
			if(priseObligatoire){ // Si on a des prises obligatoires il faut trier les solutions disponibles
				// pour tous les successeurs du point de départ courant
				while(itPointsArrivee.hasNext()){
					pArriveeCourante = (Point) itPointsArrivee.next().clone();
					Terrain.Direction dir = cloneTerrain.recupereDirection(pDepartCourant, pArriveeCourante);
					if(cloneTerrain.estUnePriseAspiration(pDepartCourant, dir))
						getListeToursPourCoupDepart(listePionsManges, listeToursTemp, new TourDeJeu(), new Coup(pDepartCourant, pArriveeCourante, Terrain.ChoixPrise.parAspiration), cloneTerrain, listeVide, 0, joueurCourant);
					if(cloneTerrain.estUnePrisePercussion(pDepartCourant, dir))
						getListeToursPourCoupDepart(listePionsManges, listeToursTemp, new TourDeJeu(), new Coup(pDepartCourant, pArriveeCourante, Terrain.ChoixPrise.parPercussion), cloneTerrain, listeVide, 0, joueurCourant);
				}
			}
			else{	// Les tours ne sont ici constitués que d'un seul coup de gain 0
				while(itPointsArrivee.hasNext()){
					pArriveeCourante = (Point) itPointsArrivee.next().clone();
					if(cloneTerrain.deplacement(pDepartCourant, pArriveeCourante, joueurCourant, listeVide) == 0){
						coupTemp.setpArrivee(pArriveeCourante);
						tourTemp = new TourDeJeu(coupTemp.clone());
						tourTemp.setTerrainFinal(cloneTerrain.copie());
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
	public void getListeToursPourCoupDepart(ArrayList<Point> listePionsManges, ArrayList<TourDeJeu> listeToursComplets, TourDeJeu tourTemp, Coup coupDeDepart, Terrain cloneTerrain, ArrayList<Point> listePredecesseurs, int nbPionsManges, Joueur joueurCourant){
		Iterator<Point> itPointsArriveeSuivants;
		Terrain terrainCopie;
		Point pDep, pArr, pArrTemp, pTemp;
		int compteur = 0, tailleListe;
		
		pDep = coupDeDepart.getpDepart();
		pArr = coupDeDepart.getpArrivee();
	
		terrainCopie = cloneTerrain;
		
		terrainCopie.deplacement(pDep, pArr, joueurCourant, listePredecesseurs);

		if(coupDeDepart.getChoixPrise() != null)
			listePionsManges = terrainCopie.manger(joueurCourant, terrainCopie.recupereDirection(pDep, pArr), pDep, pArr,coupDeDepart.getChoixPrise());
		nbPionsManges += listePionsManges.size();
		listePredecesseurs.add(pDep);
		
		
		// Il y a aprés chaque prise possibilité de s'arrêter
		tourTemp.addCoup(coupDeDepart);
		tourTemp.setValeurResultat(nbPionsManges);
		tourTemp.setTerrainFinal(terrainCopie);
		listeToursComplets.add(tourTemp.clone());
				
		pDep = pArr;
		
		// On récupère les successeurs possibles à la position d'arrivée du coup joué
		itPointsArriveeSuivants = terrain.deplacementPossible(pDep, listePredecesseurs, terrainCopie.getTableau()).iterator();

		while(itPointsArriveeSuivants.hasNext()){
			pArrTemp = (Point) itPointsArriveeSuivants.next().clone();
			Terrain.Direction dir = terrainCopie.recupereDirection(pDep, pArrTemp);
			if(terrainCopie.estUnePriseAspiration(pDep, dir))
				getListeToursPourCoupDepart(listePionsManges, listeToursComplets, tourTemp.clone(), new Coup(pDep, pArrTemp, Terrain.ChoixPrise.parAspiration), terrainCopie, listePredecesseurs, nbPionsManges, joueurCourant);
			if(terrainCopie.estUnePrisePercussion(pDep, dir))
				getListeToursPourCoupDepart(listePionsManges, listeToursComplets, tourTemp.clone(), new Coup(pDep, pArrTemp, Terrain.ChoixPrise.parPercussion), terrainCopie, listePredecesseurs, nbPionsManges, joueurCourant);
		}
		
		Joueur.recupereJoueurOpposant(joueurCourant, joueurIA, joueurAdversaire, false);
		
		tailleListe = listePionsManges.size() ;
		for(compteur = 0; compteur < tailleListe; compteur++){
			pTemp = listePionsManges.get(0);
			terrainCopie.setCase(joueurCourant.getJoueurID(), pTemp.x, pTemp.y);
			listePionsManges.remove(0);
		}
	}
	
	/*
	 * evalGeometrie : cette fonction renvoie un "score" d'évaluation de la position de force d'un 
	 * 				   joueur donné sur une map donnée
	 */
	int evalGeometrie(Terrain terrainCourant, Joueur joueurCourant){
		int resultat = 0;
		
		
		
		return resultat;
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
	
	public void setTerrain(Terrain t) {
		this.terrain = t;
	}

}
