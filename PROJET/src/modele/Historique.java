package modele;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Classe permettant de gérer l'historique de la partie.
 */
public class Historique implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Historique de la partie.
	 */
	public ArrayList<Terrain> histoPrincipal;
	/**
	 * Historique du tour en cours.
	 */
	public ArrayList<Point> histoTour;

	public ArrayList<Coup> hisTourCoup;

	/**
	 * Indice du Terrain affiché à l'écran.
	 */
	private int itPrincipal;
	/**
	 * Indice du point de déplacement.
	 */
	private int itTour;
	
	private int itCoup;
	
	/**
	 * Constructeur par défaut.
	 */
	public Historique() {
		histoPrincipal = new ArrayList<Terrain>();
		histoTour = new ArrayList<Point>();
		hisTourCoup =new ArrayList<Coup>();
 		itPrincipal = 0;
		itTour = 0;
		itCoup = 0 ;
	}
	
	/**
	 * Constructeur par copie.
	 * @param h L'historique à copier.
	 */
	public Historique(Historique h) {
		histoPrincipal = h.histoPrincipal;
		histoTour = h.histoTour;
		itPrincipal = h.itPrincipal;
		hisTourCoup = h.hisTourCoup;
		itTour = h.itTour;
		itCoup = h.itCoup;
	}
	
	/**
	 * Permet de savoir quel état du Terrain est affiché.
	 * @return L'itérateur de l'historique de la partie.
	 */
	public int getItPrincipal(){
		return itPrincipal;
	}
	
	/**
	 * Ajoute un Terrain à l'historique de la partie.
	 * @param t Le Terrain à ajouter.
	 */
	public void ajouterTour(Terrain t) {
		if(itPrincipal < histoPrincipal.size()-1) {
			for(int i = histoPrincipal.size()-1; i > itPrincipal; i--) { 
				histoPrincipal.remove(i);
			}
		}
		Terrain c = t.copie();
		histoPrincipal.add(c);
		itPrincipal = histoPrincipal.size()-1;
		
		itTour=0;
		itCoup=0;
		hisTourCoup = new ArrayList<Coup>() ;
		
		//;//System.out.println("itprincipal"+itPrincipal);
		
	}
	
	/**
	 * Ajoute un coup à l'historique du tour.
	 * @param pDepart Le point de départ du pion.
	 * @param pArrive Le point d'arrivé du point.
	 */
	public void ajouterCoup(Point pDepart,Point pArrive) {
		histoTour.add((Point)pDepart.clone());
		hisTourCoup.add(new Coup(pDepart,pArrive));
		itTour++;
		itCoup++;
		
	}
	
	public Coup getDernierCoup(){
		return hisTourCoup.get(hisTourCoup.size()-1);
	}
	
	
	public Terrain getDernierTerrain(){
		return histoPrincipal.get(itPrincipal-1);
	}
	
	
	/**
	 * Fait revenir le terrain au tour précédant.
	 * @return Terrain du tour précédant.
	 */
	public Terrain annuler() {
		if(itPrincipal<=0){
			//;//System.out.println("PAS de ANNULER POSSIBLE");
			return null;}
		else {
			
			Terrain temp =histoPrincipal.get(--itPrincipal);
			//temp.dessineTableauAvecIntersections();
			return temp;
		}
	}
	
	/**
	 * Fait passer le terrain au tour suivant.
	 * @return Terrain du tour suivant.
	 */
	public Terrain refaire() {
		if(itPrincipal == histoPrincipal.size()-1)
			return null;
		else {
			return histoPrincipal.get(++itPrincipal);
		}
	}
	
	/**
	 * Efface l'historique du tour quand il est fini.
	 */
	public void effacerHistoTour() {
		histoTour = new ArrayList<Point>();
		itTour=0;
		itCoup=0;
		
	}
	
	/**
	 * Affiche l'historique en console. Utilisé pour le debug.
	 */
	public void afficher() {
		;//System.out.println("----------------");
		for(int it = 0; it < histoPrincipal.size(); it++) {
			Terrain tmp = histoPrincipal.get(it);
			//Terrain tmp = histoPrincipal.get(itPrincipal);
			//tmp.dessineTableauAvecIntersections();
		}
		;//System.out.println("----------------");
		;//System.out.println(itPrincipal);
		;//System.out.println("----------------");
	}
	
}
